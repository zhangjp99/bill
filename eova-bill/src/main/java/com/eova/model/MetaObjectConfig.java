/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.model;

/**
 * 元字段配置
 * 
 * @author Jieven
 *
 */
public class MetaObjectConfig {

	// 是否进行合计
	private boolean totalRow = false;

	public boolean isTotalRow() {
		return totalRow;
	}

	public void setTotalRow(boolean totalRow) {
		this.totalRow = totalRow;
	}

}