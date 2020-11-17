/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.render;

import java.io.IOException;
import java.io.PrintWriter;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

public class XmlRender extends Render {

	private static final String contentType = "text/xml;charset=" + getEncoding();

	private String xml;

	public XmlRender(String xml) {
		if (xml == null)
			throw new IllegalArgumentException("The parameter xml can not be null.");
		this.xml = xml;
	}

	public void render() {
		PrintWriter writer = null;
		try {
			response.setHeader("Pragma", "no-cache"); // HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType(contentType);
			response.setCharacterEncoding(getEncoding());

			writer = response.getWriter();
			writer.write(xml);
			writer.flush();
		} catch (IOException e) {
			throw new RenderException(e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}
}