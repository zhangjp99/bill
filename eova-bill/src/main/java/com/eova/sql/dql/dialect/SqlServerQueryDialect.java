package com.eova.sql.dql.dialect;

import com.eova.sql.dql.dialect.QueryDialect;

public class SqlServerQueryDialect extends QueryDialect {

	@Override
	public String timeCondition(String field) {
		return String.format(" and ? <= %s and %s <= ?", field, field);
	}

	@Override
	public String dateCondition(String field) {
		return String.format(" and ? <= %s and %s <= ?", field, field);
	}

	@Override
	public String multipleCondition(String field, String value) {
		// 最终SQL:where (FIND_IN_SET(?, tag) or FIND_IN_SET(?, tag)) 
		// 原因:in只能进行关系查询, 不能进行字符串查找, 所以用 FIND_IN_SET
		return String.format("FIND_IN_SET(?, %s)", field);
	}

	public static void main(String[] args) {
		System.out.println(String.format(" and ? <= date(%s) and date(%s) <= ?", "11", "11"));
	}

}
