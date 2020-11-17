/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.sql.dql.dialect;

public class MysqlQueryDialect extends QueryDialect {

	@Override
	public String timeCondition(String field) {
		return String.format(" and ? <= %s and %s <= ?", field, field);
	}

	@Override
	public String dateCondition(String field) {
		return String.format(" and ? <= date(%s) and date(%s) <= ?", field, field);
	}

	@Override
	public String multipleCondition(String field, String value) {
		// 最终SQL:where (FIND_IN_SET(?, tag) or FIND_IN_SET(?, tag)) 
		// 原因:in只能进行关系查询, 不能进行字符串查找, 所以用 FIND_IN_SET
		return String.format("FIND_IN_SET(?, %s)", field);
	}

}