/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.eova.common.utils.xx;
import com.eova.common.utils.io.ClassUtil;
import com.eova.core.type.MysqlConvertor;
import com.eova.core.type.SqlServerConvertor;
import com.eova.ext.jfinal.EovaDbPro;
import com.eova.ext.jfinal.EovaOracleDialect;
import com.eova.sql.ddl.DefineDialectFactory;
import com.eova.sql.ddl.dialect.DefineDialect;
import com.eova.sql.ddl.dialect.MysqlDefineDialect;
import com.eova.sql.dql.dialect.MysqlQueryDialect;
import com.eova.sql.dql.dialect.QueryDialect;
import com.eova.sql.dql.dialect.SqlServerQueryDialect;
import com.jfinal.config.Plugins;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.IDbProFactory;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.activerecord.dialect.SqlServerDialect;
import com.jfinal.plugin.druid.DruidPlugin;

public class EovaDataSource {

	/** 数据源列表<数据源名, 数据源DB类型> **/
	private static Map<String, String> dataSources = new HashMap<>();

	public static Map<String, String> map() {
		return dataSources;
	}

	/**
	 * 获取数据库类型
	 * @param ds
	 * @return
	 */
	public static String getDbType(String ds) {
		return dataSources.get(ds);
	}

	/**
	 * 注册数据源(支持多数据源)
	 * @param plugins
	 */
	protected static void create(Plugins plugins) {
		// 多数据源支持
		String datasource = xx.getConfig("db.datasource");
		if (xx.isEmpty(datasource)) {
			throw new RuntimeException("数据源配置项不存在,请检查配置jdbc.config 配置项[db.datasource]");
		}
		for (String ds : datasource.split(",")) {
			ds = ds.trim();

			String url = xx.getConfig(ds + ".url");
			String user = xx.getConfig(ds + ".user");
			String pwd = xx.getConfig(ds + ".pwd");
			if (xx.isEmpty(url)) {
				throw new RuntimeException(String.format("数据源[%s]配置异常,请检查请检查配置jdbc.config", ds));
			}
			DruidPlugin dp = EovaDataSource.initDruidPlugin(url, user, pwd);
			ActiveRecordPlugin arp = EovaDataSource.initActiveRecordPlugin(url, ds, dp);
			xx.info("create ds[%s] %s > %s", ds, user, url);

			try {
				dataSources.put(ds, JdbcUtils.getDbType(url, JdbcUtils.getDriverClassName(url)));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			EovaConfig.arps.put(ds, arp);
			plugins.add(dp).add(arp);
		}

	}

	/**
	 * init Druid
	 *
	 * @param url JDBC
	 * @param username 数据库用户
	 * @param password 数据库密码
	 * @return
	 */
	protected static DruidPlugin initDruidPlugin(String url, String username, String password) {
		// 设置方言
		WallFilter wall = new WallFilter();
		String dbType = null;
		try {
			dbType = JdbcUtils.getDbType(url, JdbcUtils.getDriverClassName(url));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		wall.setDbType(dbType);

		// 开发者模式 降低SQL安全策略, 保证应用商店安装卸载需要开发者模式.
		boolean devMode = xx.getConfigBool("devMode", false);
		if (devMode) {
			// https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE-wallfilter
			WallConfig config = new WallConfig();
			config.setMultiStatementAllow(true);// 一次执行多条语句
			config.setNoneBaseStatementAllow(false);// 非基本DDL语句
			wall.setConfig(config);
			wall.setThrowException(false);// SQL注入抛出SQLException
			wall.setLogViolation(true);/// SQL注入输出日志

		}

		DruidPlugin dp = new DruidPlugin(url, username, password);
		dp.addFilter(new StatFilter());
		dp.addFilter(wall);
		dp.setInitialSize(1);// 初始化1个
		dp.setMinIdle(2);// 最少空闲2个/ 连接上限32个
		dp.setMaxWait(10000);// 配置获取连接等待超时的时间 10s

		return dp;
	}

	/**
	 * init ActiveRecord
	 *
	 * @param url JDBC
	 * @param ds DataSource
	 * @param dp Druid
	 * @return
	 */
	protected static ActiveRecordPlugin initActiveRecordPlugin(String url, String ds, IDataSourceProvider dp) {
		// 提升事务级别保证事务回滚 MYSQL TRANSACTION_REPEATABLE_READ=4
		int lv = xx.getConfigInt("db.transaction_level", 4);
		// DB默认命名规则
		boolean isLowerCase = xx.getConfigBool("db.islowercase", true);
		// 是否输出SQL日志
		boolean isShowSql = xx.getConfigBool("db.showsql", true);

		ActiveRecordPlugin arp = new ActiveRecordPlugin(ds, dp);

		// 自定义Eova专用Db代理
		arp.setDbProFactory(new IDbProFactory() {
			public DbPro getDbPro(String configName) {
				return new EovaDbPro(configName);
			}
		});

		String dbType = "";
		// 获取DB类型
		try {
			dbType = JdbcUtils.getDbType(url, JdbcUtils.getDriverClassName(url));
			if (ds.equalsIgnoreCase(xx.DS_EOVA)) {
				// Eova的数据库类型
				EovaConfig.EOVA_DBTYPE = dbType;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// 方言选择
		Dialect dialect;
		DefineDialect dd = null;
		try {
			if (JdbcUtils.MYSQL.equalsIgnoreCase(dbType) || JdbcUtils.H2.equalsIgnoreCase(dbType)) {
				dialect = new MysqlDialect();
				EovaConfig.queryDialectMap.put(ds, new MysqlQueryDialect());
				dd = new MysqlDefineDialect();
			} else if (JdbcUtils.ORACLE.equalsIgnoreCase(dbType)) {
				dialect = new EovaOracleDialect();
				// com.eova.mod.eova.oracle.* 和驱动包放在一起, 会被 单独加载, 所以会出现和热加载, 转换异常.
				// 临时补丁, 修复加载不到的问题
				EovaConfig.queryDialectMap.put(ds, ClassUtil.newClass("com.eova.mod.eova.oracle.OracleQueryDialect"));
				((DruidPlugin) dp).setValidationQuery("select 1 FROM DUAL");
			} else if (JdbcUtils.DM.equalsIgnoreCase(dbType)) {
				// 据说和Oracle一致, 但是实际上不靠谱, 有待考察
				dialect = new EovaOracleDialect();
				EovaConfig.queryDialectMap.put(ds, (QueryDialect) ClassUtil.newClass("com.eova.mod.eova.oracle.OracleQueryDialect"));
				((DruidPlugin) dp).setValidationQuery("select 1 FROM DUAL");
			} else if (JdbcUtils.POSTGRESQL.equalsIgnoreCase(dbType)) {
				dialect = new PostgreSqlDialect();
				EovaConfig.queryDialectMap.put(ds, (QueryDialect) ClassUtil.newClass("com.eova.mod.eova.postgresql.PostgreSqlQueryDialect"));
			} else if (JdbcUtils.SQL_SERVER.equalsIgnoreCase(dbType)) {
//				dialect = new SqlServerDialect();
//				EovaConfig.queryDialectMap.put(ds, (QueryDialect) ClassUtil.newClass("com.eova.mod.eova.sqlserver.SqlServerQueryDialect"));
				dialect = new SqlServerDialect();
				EovaConfig.queryDialectMap.put(ds, new SqlServerQueryDialect());
			} else {
				throw new RuntimeException(dbType + "暂未支持该数据源, 请联系技术支持:admin@eova.cn");
			}
		} catch (Exception e) {
			throw new RuntimeException(dbType + "初始化方言失败, 请联系技术支持:admin@eova.cn", e);
		}

		arp.setTransactionLevel(lv);
		// 大小写敏感 保持字段顺序()
		// arp.setContainerFactory(new EovaContainerFactory(isLowerCase));
		// 忽略大小写敏感 , 字段无序
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(isLowerCase));
		arp.setShowSql(isShowSql);
		arp.setDialect(dialect);

		DefineDialectFactory.addDialect(ds, dd);

		return arp;
	}

	/**
	 * 构建类型转换方言
	 * TODO 多数据源独立转换器
	 */
	static void buildConvertor() {
		// 配置数据转换器
		try {
			switch (EovaConfig.EOVA_DBTYPE) {
			case JdbcUtils.MYSQL:
				EovaConfig.convertor = new MysqlConvertor();
				break;
			case JdbcUtils.ORACLE:
				EovaConfig.convertor = ClassUtil.newClass("com.eova.mod.eova.oracle.OracleConvertor");
				break;
			case JdbcUtils.POSTGRESQL:
				EovaConfig.convertor = ClassUtil.newClass("com.eova.mod.eova.postgresql.PostgreSqlConvertor");
				break;
			case JdbcUtils.SQL_SERVER:
				EovaConfig.convertor = new SqlServerConvertor();
				break;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 构建类型转换方言
	 * TODO 多数据源独立转换器
	 */
//	static void buildDefination() {
//		// 配置数据转换器
//		try {
//			switch (EovaConfig.EOVA_DBTYPE) {
//			case JdbcUtils.MYSQL:
//				EovaConfig.convertor = new MysqlConvertor();
//				break;
//			case JdbcUtils.ORACLE:
//				EovaConfig.convertor = ClassUtil.newClass("com.eova.mod.eova.OracleConvertor");
//				break;
//			case JdbcUtils.POSTGRESQL:
//				EovaConfig.convertor = ClassUtil.newClass("com.eova.mod.eova.pgsql.PostgreSqlConvertor");
//				break;
//			case JdbcUtils.SQL_SERVER:
//				EovaConfig.convertor = ClassUtil.newClass("com.eova.mod.eova.sqlserver.SqlServerConvertor");
//				break;
//			}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
}