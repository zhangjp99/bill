/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.sql.dql.dialect;

import java.util.List;

import com.eova.common.utils.xx;
import com.eova.common.vo.KeyVal;
import com.eova.model.MetaField;
import com.eova.sql.dql.QueryParam;

/**
 * DQL(data query language)数据查询方言
 * 关键字:select, where等
 * 
 * @author Jieven
 *
 */
public abstract class QueryDialect {

	/**
	 * 多值条件
	 * @param field
	 * @return
	 */
	protected abstract String multipleCondition(String field, String value);

	/**
	 * 时间条件
	 * @param field
	 * @return
	 */
	protected abstract String timeCondition(String field);

	/**
	 * 日期条件
	 * @param field
	 * @return
	 */
	protected abstract String dateCondition(String field);

	/**
	 * 多选值查询条件规则
	 * @param type
	 * @param dataType
	 * @param field
	 * @param pm
	 * @return
	 */
	public String multiple(String field, String value, List<Object> params) {
		StringBuilder sb = new StringBuilder();
		sb.append(" and (");
		for (String val : value.split(",")) {
			if (!sb.toString().endsWith(" (")) {
				sb.append(" or ");
			}
			sb.append(multipleCondition(field, val));
			if (xx.isMysql()) {
				// mysql FIND_IN_SET 给参
				params.add(val);
			}
		}
		sb.append(")");

		return sb.toString();
	}

	/**
	 * 单选值查询条件规则
	 * @param type 控件类型
	 * @param dataType 数据类型
	 * @param dataSize TODO
	 * @param field 当前字段
	 * @param pm 查询参数
	 * @param isMultiple 是否多值
	 * @return
	 */
	public KeyVal single(String type, String dataType, int dataSize, String field, QueryParam pm) {
		Object value = pm.value;
		Object start = pm.start;
		Object end = pm.end;
		String cond = pm.cond;

		// 数字框
		if (type.equals(MetaField.TYPE_NUM)) {
			// 数字框与文本框取值兼容
			if (!xx.isEmpty(value))
				start = value;
			return numType(field, start, end, cond);
		}

		// 时间框
		if (type.equals(MetaField.TYPE_TIME)) {
			return new KeyVal(timeCondition(field), start + "," + end);
		}

		// 日期框
		if (type.equals(MetaField.TYPE_DATE)) {
			return new KeyVal(dateCondition(field), start + "," + end);
		}

		// 数字类型都是精确查询: mysql *int || order number||long 
		if (dataType.contains("int") || dataType.equals("number") || dataType.equals("long")) {
			return new KeyVal(" and " + field + " = ?", value);
		}

		// 文本框
		if (type.equals(MetaField.TYPE_TEXT) || type.equals(MetaField.TYPE_TEXTS) || type.equals(MetaField.TYPE_EDIT)) {
			return new KeyVal(" and " + field + " like ?", "%" + value + "%");
		}

		return new KeyVal(" and " + field + " = ?", value);
	}

	public KeyVal numType(String field, Object start, Object end, String cond) {
		if (cond.equals("") || cond.equals("=")) {
			return new KeyVal(String.format(" and %s = ? ", field), start);
		} else if (cond.equals("<")) {
			return new KeyVal(String.format(" and %s < ? ", field), start);
		} else if (cond.equals(">")) {
			return new KeyVal(String.format(" and %s > ? ", field), start);
		} else {
			// 只选一个值无效
			if (xx.isEmptyOne(start, end)) {
				return null;
			}
			if (cond.equals("∩")) {
				return new KeyVal(" and ? < " + field + " and " + field + " < ?", start + "," + end);
			} else if (cond.equals("U")) {
				return new KeyVal(" and ? < " + field + " or " + field + " < ?", start + "," + end);
			}
		}
		return null;
	}
}