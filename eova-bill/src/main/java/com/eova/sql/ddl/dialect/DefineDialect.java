/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.sql.ddl.dialect;

import com.eova.sql.ddl.DefineColumn;
import com.eova.sql.ddl.DefineTable;

/**
 * DQL(Data Define Language)数据定义方言
 * 关键字：create, drop, alter, truncat(冷门暂不实现) 等
 * @author Jieven
 *
 */
public abstract class DefineDialect {

	/**
	 * DbType 转 EovaType
	 * @param dbType
	 * @return
	 */
	public abstract String toEovaType(String dbType);

	/**
	 * EovaType 转 DbType
	 * @param eovaType
	 * @param size
	 * @param decimal
	 * @return
	 */
	public abstract String toDbType(String eovaType, int size, int decimal);

	/**
	 * 删除表
	 * @param tableName 表名
	 */
	public abstract String dropTable(String tableName);

	/**
	 * 创建表
	 * @param defineTable
	 * @return
	 */
	public abstract String createTable(DefineTable defineTable);

	/**
	 * 重命名表名
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public abstract String renameTable(String oldName, String newName);

	/**
	 * 添加主键
	 * @param tableName
	 * @param pk
	 * @return
	 */
	public abstract String addPrimary(String tableName, String pk);

	/**
	 * 添加字段
	 * @param tableName
	 * @param o
	 * @return
	 */
	public abstract String addColumn(String tableName, DefineColumn o);

	/**
	 * 构建列语法
	 * @param o
	 * @return
	 */
	public abstract String buildColumn(DefineColumn o);

	/**
	 * 更新字段
	 * @param tableName
	 * @param oldColumn
	 * @param o
	 * @return
	 */
	public abstract String updateColumn(String tableName, String oldColumn, DefineColumn o);

	/**
	 * 删除字段
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public abstract String dropColumn(String tableName, String columnName);

}