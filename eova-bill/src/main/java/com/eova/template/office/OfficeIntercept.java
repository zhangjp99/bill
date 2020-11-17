/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.template.office;

import java.util.HashMap;

import com.jfinal.core.Controller;

public class OfficeIntercept {

	protected final static char Y = '☑';
	protected final static char N = '□';

	/**
	 * 初始化获取模版数据
	 * @param ctrl
	 * @param data 模版渲染数据
	 * @param template 模版路径
	 * @return 最终渲染模版(可以实现自定义控制多模版)
	 * @throws Exception
	 */
	public String init(Controller ctrl, HashMap<String, Object> data, String template) throws Exception {
		return template;
	}

}