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

public class MysqlDefineDialect extends DefineDialect {

	@Override
	public String toEovaType(String type) {
		if (type.contains("INT")) {
			return "NUMBER";
		} else if (type.equals("FLOAT")) {
			return "NUMBER";
		} else if (type.equals("DOUBLE")) {
			return "NUMBER";
		} else if (type.equals("DECIMAL")) {
			return "NUMBER";
		} else if (type.equals("BIT")) {
			return "BOOL";
		} else if (type.equals("CHAR")) {
			return "VARCHAR";
		}
		return type.toUpperCase();
	}

	@Override
	public String toDbType(String eovaType, int size, int decimal) {
		switch (eovaType) {
		case "BOOL":
			return "TINYINT(1)";
		case "NUMBER":
			// 整数
			if (decimal == 0) {
				if (size == 1)
					return "TINYINT(1)";
				else if (size <= 4)
					return String.format("TINYINT(%s)", size);
				else if (size <= 11)// 此处按照使用习惯优先，大多数人的认知是int <= 10位
					return String.format("INT(%s)", size);
				else
					return String.format("BIGINT(%s)", size);
			}
			// 小数
			else {
				if (size + decimal <= 8)
					return String.format("FLOAT(%s,%s)", size, decimal);
				if (size + decimal <= 16)
					return String.format("DOUBLE(%s,%s)", size, decimal);
				return String.format("DECIMAL(%s,%s)", size, decimal);
			}
		case "VARCHAR":
			if (size <= 20000) {
				return String.format("VARCHAR(%s)", size);
			}
			return "TEXT";
		default:
			break;
		}
		// date dateime timestamp
		return eovaType.toUpperCase();
	}

	@Override
	public String createTable(DefineTable tables) {
		StringBuilder sb = new StringBuilder();

		// + System.currentTimeMillis()
		sb.append(String.format("CREATE TABLE `%s` (\n", tables.getEn()));
		for (DefineColumn col : tables.getFields()) {
			sb.append(buildColumn(col)).append(",\n");
		}
		sb.append(String.format(" PRIMARY KEY (`%s`)\n", tables.getPk()));
		sb.append(String.format(") CHARSET=utf8 COMMENT='%s';", tables.getCn()));
		sb.append("\n");

		return sb.toString();
	}

	@Override
	public String dropTable(String tableName) {
		return "DROP TABLE IF EXISTS `" + tableName + "`;";
	}

	@Override
	public String renameTable(String oldName, String newName) {
		return String.format("ALTER TABLE `%s` RENAME TO `%s`;", oldName, newName);
	}

	@Override
	public String buildColumn(DefineColumn o) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(" `%s`", o.getEn()));
		sb.append(" ").append(toDbType(o.getType(), o.getSize(), o.getDecimal()));
		sb.append(o.isNull() ? "" : " NOT NULL");
		sb.append(buildDefault(o));
		sb.append(o.isAuto() ? " AUTO_INCREMENT" : "");
		sb.append(String.format(" COMMENT '%s'", o.getCn()));
		return sb.toString();
	}

	private String buildDefault(DefineColumn o) {
		String def = o.getDefault();
		if (def == null) {
			return "";
		}
		if ("VARCHAR".equals(o.getType())) {
			return String.format(" DEFAULT '%s'", def);
		}
		return String.format(" DEFAULT %s", def);
	}

	@Override
	public String addPrimary(String tableName, String pk) {
		return String.format("ALTER TABLE `%s` ADD PRIMARY KEY (`%s`);", tableName, pk);
	}

	@Override
	public String addColumn(String tableName, DefineColumn o) {
		return String.format("ALTER TABLE `%s` ADD COLUMN %s;", tableName, buildColumn(o));
	}

	@Override
	public String updateColumn(String tableName, String oldColumn, DefineColumn o) {
		return String.format("ALTER TABLE `%s` CHANGE COLUMN `%s` %s;", tableName, oldColumn, buildColumn(o));
	}

	@Override
	public String dropColumn(String tableName, String columnName) {
		return String.format("ALTER TABLE `%s` DROP COLUMN `%s`;", tableName, columnName);
	}

}