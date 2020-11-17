/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.utils.excel;

import java.util.List;

import com.eova.model.MetaField;
import com.jfinal.plugin.activerecord.Record;

/**
 * 大数据导出, 推荐!
 *
 * @author Jieven
 *
 */
public class CsvUtil {

	public static String createCsv(List<Record> list, List<MetaField> items) {
		StringBuilder sb = new StringBuilder();

		// 标题
		for (MetaField x : items) {
			sb.append(x.getCn()).append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("\n");

		// 数据
		for (Record e : list) {
			String[] values = ExcelUtil.getValues(items, e);
			for (String s : values) {
				sb.append(s).append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("\n");
		}

		return sb.toString();
	}

}