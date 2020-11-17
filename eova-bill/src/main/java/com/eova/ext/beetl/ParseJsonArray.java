/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.ext.beetl;

import org.beetl.core.Context;
import org.beetl.core.Function;

import com.alibaba.fastjson.JSONArray;

/**
 * 字符串转JSONArray
 * 
 * @author Jieven
 * @date 2017-07-16
 */
public class ParseJsonArray implements Function {
	@Override
	public Object call(Object[] paras, Context ctx) {
		if (paras.length != 1) {
			throw new RuntimeException("参数错误，请传入一个JSON字符串");
		}
		Object para = paras[0];
		if (para == null) {
			return null;
		}
		return JSONArray.parse(para.toString());
	}
}