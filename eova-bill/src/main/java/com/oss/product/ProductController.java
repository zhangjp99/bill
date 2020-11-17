/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.oss.product;

import java.util.List;

import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 产品管理
 * 
 * @author Jieven
 * 
 */
public class ProductController extends BaseController {

	// 清空积分 -> 二次确认按钮
	public void clean() throws Exception {
		Integer id = getSelectValueToInt("id");

		Db.update("update product set score = 0 where id = ?", id);

		renderJson(Easy.sucess());
	}

	// 添加积分 -> 前端输入按钮
	public void score() throws Exception {
		Integer id = getSelectValueToInt("id");
		String val = getInputValue();

		if (val.equals("666")) {
			renderJson(Easy.fail("SB666"));
			return;
		}

		Db.update("update product set score = score + ? where id = ?", xx.toInt(val), id);

		renderJson(Easy.sucess());
	}

	// 更新产品 -> 后台执行按钮
	public void refresh() throws Exception {

		List<Record> list = Db.find("select * from product");
		for (Record r : list) {
			r.set("name", r.getStr("name") + 1);
			Db.update("product", r);
		}

		renderJson(Easy.sucess());
	}

}