/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.ext.beetl.format;

import org.beetl.core.Format;

import com.eova.common.utils.web.HtmlUtil;

public class XSSFormat implements Format {

	@Override
	public Object format(Object data, String pattern) {
		if (null == data) {
			return null;
		} else {
			String content = (String) data;
			// XSS简单过滤
			content = HtmlUtil.XSSEncode(content);
			return content;
		}
	}
}