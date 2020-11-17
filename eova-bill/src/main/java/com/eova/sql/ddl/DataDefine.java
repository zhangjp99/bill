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
import java.util.List;

import com.eova.sql.ddl.dialect.DefineDialect;
import com.jfinal.plugin.activerecord.Db;

public abstract class DataDefine {

	protected abstract String getDs();

	protected abstract List<DefineTable> createTable();

	protected abstract void createTableBefore(DefineDialect dd, List<String> sqls);

	protected abstract void createTableAfter(DefineDialect dd, List<String> sqls);

	public String execute() {
		List<String> sqls = new ArrayList<>();

		// 获取DDL方言
		DefineDialect dd = DefineDialectFactory.getDialect(getDs());

		// 建表前, 比如删表
		createTableBefore(dd, sqls);

		// 建表
		createTable().forEach(t -> {
			sqls.add(dd.createTable(t));
		});

		// 建表后, 比如字段调整
		createTableAfter(dd, sqls);

		// 执行SQL
		Db.use(getDs()).batch(sqls, sqls.size());

		// 生成SQL日志
		StringBuffer sb = new StringBuffer();
		sqls.forEach(x -> sb.append(x).append("\n"));// 空行格式化
		return sb.toString();
	}

	public String executeCreateTableBefore() {
		List<String> sqls = new ArrayList<>();

		// 获取DDL方言
		DefineDialect dd = DefineDialectFactory.getDialect(getDs());

		// 建表前, 比如删表
		createTableBefore(dd, sqls);

		// 执行SQL
		Db.use(getDs()).batch(sqls, sqls.size());

		// 生成SQL日志
		StringBuffer sb = new StringBuffer();
		sqls.forEach(x -> sb.append(x).append("\n"));// 空行格式化
		return sb.toString();
	}

}