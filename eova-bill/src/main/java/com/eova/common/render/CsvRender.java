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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.eova.common.utils.excel.CsvUtil;
import com.eova.model.MetaField;
import com.eova.model.MetaObject;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

public class CsvRender extends Render {

	private final static String CONTENT_TYPE = "text/csv;charset=" + getEncoding();

	private final List<MetaField> items;
	private final List<Record> data;

	private final String fileName;

	public CsvRender(List<Record> data, List<MetaField> items, MetaObject object) {
		this.data = data;
		this.items = items;

		fileName = object.getStr("name") + ".csv";
	}

	@Override
	public void render() {
		response.reset();
		try {
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, getEncoding()));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setContentType(CONTENT_TYPE);
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			String s = CsvUtil.createCsv(data, items);
			os.write(s.getBytes());
		} catch (Exception e) {
			throw new RenderException(e);
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				LogKit.error(e.getMessage(), e);
			}

		}
	}

}