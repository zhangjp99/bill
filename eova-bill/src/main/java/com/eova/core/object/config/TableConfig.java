/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.object.config;

import com.alibaba.fastjson.annotation.JSONField;

//@JSONType(orders={"whereField","paramField"})
public class TableConfig {

	// where 条件字段
	@JSONField(ordinal = 1)
	private String whereField;
	// where 条件字段的值(必须是视图显示列)
	@JSONField(ordinal = 2)
	private String paramField;

	public String getWhereField() {
		return whereField;
	}

	public void setWhereField(String whereField) {
		this.whereField = whereField;
	}

	public String getParamField() {
		return paramField;
	}

	public void setParamField(String paramField) {
		this.paramField = paramField;
	}

}