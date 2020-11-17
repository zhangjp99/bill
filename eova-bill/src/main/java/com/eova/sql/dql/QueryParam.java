/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.sql.dql;

/**
 * 查询参数
 * @author Jieven
 *
 */
public class QueryParam {

	/**逻辑运算法**/
	public String cond;
	/**单个值**/
	public Object value;
	/**范围开始值**/
	public String start;
	/**范围结束值**/
	public String end;

	public QueryParam(String cond, Object value, String start, String end) {
		super();
		this.cond = cond;
		this.value = value;
		this.start = start;
		this.end = end;
	}

}