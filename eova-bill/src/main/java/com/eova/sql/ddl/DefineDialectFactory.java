/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.sql.ddl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.eova.sql.ddl.dialect.DefineDialect;
import com.eova.sql.ddl.dialect.MysqlDefineDialect;
import com.jfinal.plugin.activerecord.Db;

/**
 * DDL方言工厂
 * @author Jieven
 *
 */
public class DefineDialectFactory {

	private static HashMap<String, DefineDialect> defineDialectMap = new HashMap<>();

	public static DefineDialect getDialect(String ds) {
		DefineDialect dd = defineDialectMap.get(ds);
		if (dd == null) {
			// 为了方面测试, 默认指定为Mysql
			dd = new MysqlDefineDialect();
		}
		return dd;
	}

	public static void addDialect(String ds, DefineDialect defineDialect) {
		defineDialectMap.put(ds, defineDialect);
	}

	private DefineDialect dialect = null;
	private String ds = null;

	public DefineDialectFactory(String ds) {
		this.ds = ds;
		this.dialect = getDialect(ds);
	}

	/**
	 * 执行脚本
	 * @param tables
	 */
	public void createTable(List<DefineTable> tables) {

		// 生成DDL方言
		List<String> sqls = new ArrayList<String>();
		tables.forEach(t -> {
			String s = dialect.createTable(t);
			sqls.add("DROP TABLE IF EXISTS `" + t.getEn() + "`;");
			sqls.add(s);
		});

		// 执行SQL
		sqls.forEach(x -> System.out.println(x));
		Db.use(ds).batch(sqls, sqls.size());
	}
}