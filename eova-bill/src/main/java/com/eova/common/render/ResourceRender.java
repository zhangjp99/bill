/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.render;

import java.util.HashMap;

import com.jfinal.kit.PathKit;
import com.jfinal.render.HtmlRender;

public class ResourceRender extends HtmlRender {

	public ResourceRender(Object object, String view, HashMap<String, Object> attr) {
		super(RenderUtil.renderResource(buildResource(object, view), attr));
	}

	private static String buildResource(Object object, String filePath) {
		// 获取当前方法的上上级 也就是 调用
		// StackTraceElement[] ss = Thread.currentThread().getStackTrace();
		// StackTraceElement a = (StackTraceElement)ss[4];
		// String txt = Utils.readFromResource(filePath);
		String pack = PathKit.getPackagePath(object);
		return String.format("%s/resources/%s", pack, filePath);
	}

}