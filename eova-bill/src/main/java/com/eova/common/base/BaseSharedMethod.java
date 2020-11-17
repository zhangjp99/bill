/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.base;

import java.util.Map;

import com.eova.common.utils.web.HtmlUtil;

public class BaseSharedMethod {

	private Map<String, String> conf;

	public BaseSharedMethod(Map<String, String> conf) {
		this.conf = conf;
	}

	public String ws(String domain) {
		return "ws://" + conf.get(domain);
	}

	public String wss(String domain) {
		return "wss://" + conf.get(domain);
	}

	public String htt(String domain) {
		return "//" + conf.get(domain);
	}

	public String http(String domain) {
		return "http://" + conf.get(domain);
	}

	public String https(String domain) {
		return "https://" + conf.get(domain);
	}

	public String dir(String dirName) {
		return conf.get("dir.static") + conf.get("dir." + dirName);
	}

	//	 非法内容过滤,适用于纯文本输出
	public String xss(String s) {
		return HtmlUtil.XSSEncode(s);
	}

	// html内容转码
	public String html(String s) {
		return HtmlUtil.HTMLEncode(s);
	}
}