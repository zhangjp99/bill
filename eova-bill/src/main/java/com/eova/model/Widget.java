/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.model;

import java.util.List;

import com.eova.common.base.BaseModel;

/**
 * 控件
 * 
 * @author Jieven
 * @date 2014-9-10
 */
public class Widget extends BaseModel<Widget> {

	private static final long serialVersionUID = 4254060861819273244L;

	public static final Widget dao = new Widget();

	/** EOVA控件 **/
	public static final int TYPE_EOVA = 1;
	/** DIY控件 **/
	public static final int TYPE_DIY = 2;

	public List<Widget> findByType(int type) {
		return this.queryByCache("select * from eova_widget where type = ?", type);
	}
}