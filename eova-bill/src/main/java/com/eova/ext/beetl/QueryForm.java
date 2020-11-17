/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.ext.beetl;

import java.util.ArrayList;
import java.util.List;

import org.beetl.core.Context;
import org.beetl.core.Function;

import com.eova.common.utils.xx;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 模版数据查询函数
 * 
 * @author Jieven
 * @date 2014-5-23
 */
public class QueryForm implements Function {
	@Override
	public Object call(Object[] paras, Context ctx) {
		// 查询自定义表单字段
		Object code = paras[0];
		if (xx.isEmpty(code)) {
			throw new RuntimeException("参数异常，请输入表单编码");
		}
		List<Record> list = Db.use(xx.DS_EOVA).find("select * from eova_form_field where form_code = ?", code);
		list.forEach(e -> {
			String items = e.getStr("items");
			if (!xx.isEmpty(items)) {
				// 按空格 tab 回车 换行进行分割
				String[] ss = items.split("\\s|\t|\r|\n");
				List<Record> arrs = new ArrayList<>();
				for (int j = 0; j < ss.length; j++) {
					// TODO 有值和传统玩法的兼容!!
					arrs.add(new Record().set("id", ss[j]).set("cn", ss[j]));
				}
				e.set("items", arrs);
				e.set("items_json", JsonKit.toJson(arrs));
			}
		});
		return list;
	}
}