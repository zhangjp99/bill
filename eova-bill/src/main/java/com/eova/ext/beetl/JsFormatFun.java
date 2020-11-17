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

import com.eova.common.utils.xx;

/**
 * JS 参数自动获取并处理默认值
 * 
 * @author Jieven
 * @date 2014-5-23
 */
public class JsFormatFun implements Function {
	@Override
	public Object call(Object[] paras, Context ctx) {
		if (paras.length != 1) {
			throw new RuntimeException("参数错误，期望Object");
		}
		Object obj = paras[0];
		if (xx.isEmpty(obj)) {
			return "undefined";
		}
		if (xx.isNum(obj)) {
			return obj.toString();
		}
		return xx.format(obj);
	}
}