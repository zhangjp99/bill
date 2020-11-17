/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.mod;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.eova.common.utils.io.FileUtil;
import com.eova.common.utils.xx;
import com.eova.common.utils.db.DbUtil;
import com.eova.common.utils.db.DsUtil;
import com.eova.common.utils.db.SqlUtil;
import com.eova.config.EovaDataSource;
import com.eova.model.Mod;
import com.eova.model.RoleBtn;
import com.eova.sql.DbDialect;
import com.jfinal.plugin.activerecord.Db;

public class EovaModUtil {

	/**
	 * 加载/WEB-INF/mod 下所有的jar
	 * @return
	 */
	public static URLClassLoader initLoader() {
		long t = System.currentTimeMillis();
		File[] jars = FileUtil.getFiles(EovaModConst.DIR_MOD);
		ArrayList<URL> list = new ArrayList<>();

		for (File jar : jars) {
			// 排除非jar文件
			if (!jar.getPath().endsWith(".jar")) {
				continue;
			}
			try {
				list.add(jar.toURI().toURL());
				if (xx.getConfig("env").equals("DEV")){
					System.out.println("find:" + jar.getName());
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(String.format("Init ModLoader in %s ms\n", System.currentTimeMillis() - t));

		URL[] urls = new URL[list.size()];
		list.toArray(urls);

		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		URLClassLoader loader = new URLClassLoader(urls, parent);

		return loader;
	}

	/**
	 * 单独加载指定的Mod
	 * @param mods
	 * @return
	 */
	public static URLClassLoader createLoader(List<Mod> mods) {

		URL[] urls = new URL[mods.size()];
		for (int i = 0; i < urls.length; i++) {
			Mod mod = mods.get(i);
			String path = String.format("%s%s-%s-%s.jar", EovaModConst.DIR_MOD, mod.getGroup(), mod.getCode(), mod.getVersion());
			// System.err.println("Load eova mod jar:" + path);
			try {
				urls[i] = new File(path).toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		URLClassLoader loader = new URLClassLoader(urls, parent);

		return loader;
	}

	/**
	 * 加载指定Mod
	 * @param group
	 * @param code
	 * @param version
	 * @return
	 */
	public static URLClassLoader createLoader(String group, String code, String version) {
		String path = String.format("%s%s-%s-%s.jar", EovaModConst.DIR_MOD, group, code, version);
		// System.err.println("Load eova mod jar:" + path);
		return loaderJar(path);
	}

	/**
	 * 加载指定Jar
	 * @param path
	 * @return
	 */
	public static URLClassLoader loaderJar(String path) {
		URL[] urls = new URL[1];
		try {
			urls[0] = new File(path).toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		URLClassLoader loader = new URLClassLoader(urls, parent);

		return loader;
	}

	public static EovaModConfig getEovaModConfig(URLClassLoader loader, String group, String code) throws Exception {
		String cs = String.format("com.eova.mod.%s.%s.ModConfig", group, code);
		Class clazz = loader.loadClass(cs);
		EovaModConfig mc = (EovaModConfig) clazz.newInstance();
		return mc;
	}
	// 无关关闭loader 会导致jar无法卸载
	//	public static <T> T loadModConfig(String group, String code) throws Exception {
	//		URLClassLoader loader = EovaModUtil.createLoader(group, code);
	//		String cs = String.format("com.eova.mod.%s.%s.ModConfig", group, code);
	//		Class clazz = loader.loadClass(cs);
	//		// xx.close(loader); 不能关闭, 还需要加载其它class
	//		return (T) clazz.newInstance();
	//	}

	/**
	 * 执行标准通用SQL
	 * @param ds
	 * @param group
	 * @param code
	 * @param fileName
	 */
	public static void executeSql(String ds, String group, String code, String fileName) {
		String sqlPath = String.format("%s%s#%s#%s", EovaModConst.DIR_MOD_VIEW, group, code, fileName).replace("#", File.separator);
		xx.info("加载SQL脚本:%s", sqlPath);
		List<String> sqls = DbUtil.loadSql(sqlPath);
		sqls.forEach(x -> System.out.println(x));
		Db.use(ds).batch(sqls, sqls.size());
	}

	/**
	 * <pre>
	 * 按数据源标准执行兼容(INSERT, UPDATE, DELETE)
	 * 1.在Oracle执行自动补充自增
	 * </pre>
	 * @param ds
	 * @param group
	 * @param code
	 * @param fileName
	 */
	public static void executeDml(String ds, String group, String code, String fileName) {
		String sqlPath = String.format("%s%s#%s#%s", EovaModConst.DIR_MOD_VIEW, group, code, fileName).replace("#", File.separator);

		executeEsql(ds, sqlPath);
	}

	/**
	 * 执行兼容SQL(按数据源规范处理)
	 * @param ds 数据源
	 * @param sqlPath sql文件
	 */
	public static void executeEsql(String ds, String sqlPath) {

		String dbType = EovaDataSource.getDbType(ds);
		// dbType = JdbcUtils.ORACLE;

		HashMap<String, String> autoMap = new HashMap<>();

		System.out.println(String.format("加载SQL脚本:%s, 转为%s规范", sqlPath, dbType));
		List<String> sqls = DbUtil.loadSql(sqlPath);
		for (String sql : sqls) {
			if (xx.isEmpty(sql)) {
				continue;
			}

			// 转义符号
			sql = DbDialect.escape(dbType, sql);

			// 跳过旧的不兼容语法
			if (sql.startsWith("SET @pid")) {
				continue;
			}
			// 维稳模式, 主动跳过风险语法
			if (xx.getConfigBool("mysql.stable", false)) {
				String TP = sql.toUpperCase();
				if (TP.startsWith("UPDATE") && TP.contains("= (SELECT")) {
					continue;
				}
			}

			// Oracle 自增的特别处理
			// else
			if (dbType.equals(JdbcUtils.ORACLE)) {
				if (sql.trim().toUpperCase().startsWith("INSERT")) {
					String tableName = SqlUtil.getInsertTableName(dbType, sql);
					String autoColumn = autoMap.get(tableName);
					if (autoColumn == null) {
						// 该表是否存在自增
						autoColumn = DsUtil.getAutoColumn(ds, tableName);
						autoMap.put(tableName, autoColumn);
					}
					// 按Eova约定添加自增列
					sql = SqlUtil.addInsertSequenceColumn(JdbcConstants.ORACLE, sql, "SEQ_" + tableName.toUpperCase(), "id");
				}
				// 更新父级的语法
				//					else if (sql.trim().startsWith("SET @pid")) {
				//						String[] ss = sql.split(";");
				//						sql = ss[2].replaceFirst("@pid", String.format("(%s)", ss[1].replaceFirst("INTO @pid", "")));
				//					}
			}

			System.out.println(sql);
		}
		Db.use(ds).batch(sqls, sqls.size());
	}
}