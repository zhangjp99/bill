/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.sql;

import com.alibaba.druid.util.JdbcUtils;

public class DbDialect {
	/**
	 * Mysql SQL 转为其它DB可执行兼容SQL
	 * @param dbType
	 * @param sql
	 * @return
	 */
	public static String escape(String dbType, String sql) {

		// 去掉Mysql转义符
		sql = sql.replaceAll("`", getEscape(dbType));

		// -- Mysql --> \' --> 单反斜杠转义单引号	
		if (JdbcUtils.ORACLE.equalsIgnoreCase(dbType)) {
			// -- Oracle --> '' --> 单引号转义单引号	
			sql = sql.replaceAll("\\\\'", "''");
		} else if (JdbcUtils.POSTGRESQL.equalsIgnoreCase(dbType)) {
			// -- Pgsql --> '' --> 字符串前面加上E(E就是Escape)进行转义
			// 偷懒做法, 如果存在多重转义,则还需要手工处理
			sql = sql.replaceAll("\\\\'", "'").replaceFirst("'", "E'");
		}
		// else if (JdbcUtils.SQL_SERVER.equalsIgnoreCase(dbType)) {
		// SqlServer 待测试
		//			sql = sql.replaceAll("\\\\'", "'").replaceFirst("'", "E'");
		//		}
		return sql;
	}


	//	/**
	//	 * Mysql单引号转义 \' 转其它DB 
	//	 * @param ds
	//	 * @return
	//	 */
	//	public static String escape(String dbType, String sql) {
	//		EovaDataSource.getEscape(ds)
	//		
	//		// -- Mysql --> \' --> 单反斜杠转义单引号	
	//		if (!sql.contains("\\'")) {
	//			return sql;
	//		}
	//		if (JdbcUtils.ORACLE.equalsIgnoreCase(dbType)) {
	//			// -- Oracle --> '' --> 单引号转义单引号	
	//			// -- UPDATE eova_xxx SET a = '	select * from eova_dict where object = ''eova_field''		';
	//			return sql.replaceAll("\\\\'", "''");
	//		}
	//		if (JdbcUtils.POSTGRESQL.equalsIgnoreCase(dbType)) {
	//			// -- Pgsql --> '' --> 字符串前面加上E(E就是Escape)进行转义
	//			// -- UPDATE eova_xxx SET a = E'	select * from eova_dict where object = 'eova_field'		';
	//			// 偷懒做法, 如果存在多重转义,则还需要手工处理
	//			return sql.replaceAll("\\\\'", "'").replaceFirst("'", "E'");
	//		}
	//		// SqlServer 待测试
	//		//		else if (JdbcUtils.SQL_SERVER.equalsIgnoreCase(dbType)) {
	//		//			return sql;
	//		//		}
	//		return sql;
	//	}

	/**
	 * 获取数据库字段表名转义符
	 * @return
	 */
	public static String getEscape(String dbType) {
		if (JdbcUtils.MYSQL.equalsIgnoreCase(dbType)) {
			return "`";
		} else if (JdbcUtils.ORACLE.equalsIgnoreCase(dbType)) {
			return "";// 待确定Oracle转义符
		} else if (JdbcUtils.POSTGRESQL.equalsIgnoreCase(dbType)) {
			return "";
		} else if (JdbcUtils.SQL_SERVER.equalsIgnoreCase(dbType)) {
			return "";
		}
		return "";
	}
}