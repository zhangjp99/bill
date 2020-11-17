/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.render;

import java.io.InputStream;
import java.util.Map;

import org.beetl.core.Template;

import com.eova.common.utils.io.StreamUtil;
import com.eova.common.utils.io.TxtUtil;
import com.eova.engine.DynamicParse;

public class RenderUtil {


	/**
	 * 渲染指定文件
	 * @param path 文件路径
	 * @param attr 动态参数
	 * @return
	 */
	public static String renderFile(String path, Map<String, Object> attr) {
		return parseTemplate(TxtUtil.getTxt(path), attr);
	}

	/**
	 * 渲染指定资源文件(比如Jar内资源文件)
	 * @param resource 资源文件路径
	 * @param attr 动态参数
	 * @return
	 */
	public static String renderResource(String resource, Map<String, Object> attr) {
		String text = "";
		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if (in == null) {
				in = RenderUtil.class.getResourceAsStream(resource);
			}
			if (in == null) {
				return null;
			}
			text = TxtUtil.read(in);
		} finally {
			StreamUtil.close(in);
		}
		return parseTemplate(text, attr);
	}

	private static String parseTemplate(String temp, Map<String, Object> attr) {
		Template t = DynamicParse.gt.getTemplate(temp);
		for (String key : attr.keySet()) {
			Object o = attr.get(key);
			t.binding(key, o);
		}
		return t.render();
	}
}