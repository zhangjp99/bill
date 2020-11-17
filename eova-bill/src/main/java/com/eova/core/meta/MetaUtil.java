/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.meta;

import com.eova.model.MetaField;
import com.eova.model.MetaObject;

public class MetaUtil {

	/**
	 * 转换数据类型
	 * 
	 * @param typeName DB数据类型
	 * @return
	 */
	public static String getDataType(String typeName) {
		typeName = typeName.toLowerCase();
		if (typeName.contains("int") || typeName.contains("bit") || typeName.equals("number") || typeName.equals("double") || typeName.equals("float")) {
			return MetaField.DATATYPE_NUMBER;
		} else if (typeName.contains("time") || typeName.contains("date")) {
			return MetaField.DATATYPE_TIME;
		} else {
			return MetaField.DATATYPE_STRING;
		}
	}

	/**
	 * 获取表单类型
	 * 
	 * @param isAuto 是否自增
	 * @param typeName 类型
	 * @param size 长度
	 * @return
	 */
	public static String getFormType(boolean isAuto, String typeName, int size) {
		typeName = typeName.toLowerCase();
		if (typeName.contains("time") || typeName.contains("date")) {
			return MetaField.TYPE_TIME;
		} else if (isAuto) {
			return MetaField.TYPE_AUTO;
		} else if (size > 255) {
			return MetaField.TYPE_TEXTS;
		} else if (size > 500) {
			return MetaField.TYPE_EDIT;
		} else if (size == 1 && (typeName.equals("tinyint") || typeName.equals("char"))) {
			return MetaField.TYPE_BOOL;
		} else {
			// 默认都是文本框
			return MetaField.TYPE_TEXT;
		}
	}

	/**
	 * 添加虚拟元对象
	 * @param sql 根据 SQL select 自动构建虚拟元对象
	 * @param code 菜单编码
	 * @param name 菜单名称
	 * @param ds SQL执行数据源
	 */
	public static void addVirtualObject(String sql, String code, String name, String ds) {
		int i1 = sql.indexOf("select") + 6;
		int i2 = sql.indexOf("from");

		code = "v_" + code;

		// 根据表达式手工构建Eova_Object
		MetaObject eo = MetaObject.dao.getTemplate();
		eo.remove("id");
		eo.set("code", code);
		eo.set("name", name);
		eo.set("data_source", ds);
		eo.set("table_name", "virtual");
		eo.set("is_first_load", 0);
		eo.set("view_sql", sql);
		eo.save();

		// 根据表达式手工构建Eova_Item
		String select = sql.trim().toLowerCase().substring(i1, i2);
		String[] ss = select.split(",");
		int i = 10;
		for (String s : ss) {
			// num1, num2
			String item = s.trim();
			//  (sum1-sum2) total
			if (item.indexOf(" ") != -1) {
				// 取最后一项
				String[] items = item.split(" ");
				item = items[items.length - 1];
			}

			MetaField ei = MetaField.dao.getTemplate();
			ei.remove("id");
			ei.put("order_num", i);
			ei.put("en", item);
			ei.put("cn", item);
			ei.put("type", "文本框");
			ei.put("is_show", 1);
			ei.put("width", 100);
			ei.set("object_code", code);
			ei.save();

			i++;
		}
	}

}