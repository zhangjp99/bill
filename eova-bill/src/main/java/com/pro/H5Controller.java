/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.pro;


import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.eova.common.utils.xx;
import com.eova.core.IndexController;
import com.eova.core.object.config.MetaObjectConfig;
import com.eova.model.Menu;
import com.eova.model.MetaObject;
import com.eova.service.sm;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 移动端 H5 DEMO
 * @author Jieven
 *
 */
public class H5Controller extends IndexController {

	// 首页
	public void index() {

		// 获取演示功能
		List<Menu> list = Menu.dao.find("select * from eova_menu where code in ('biz_demo_users', 'biz_product', 'biz_demo_links', 'biz_demo_hotel', 'biz_product')");
		setAttr("list", list);

		renderEnjoy("/mobile/index.html");
	}
	
	// 列表
	public void list() {
		
		String code = get(0);
		MetaObject o = sm.meta.getMeta(code);
		setAttr("o", o);

		// 默认显示主键值 不报错
		String[] left = {"pk_val"};
		String[] right = {"pk_val"};

		MetaObjectConfig config = o.getConfig();
		if (config != null) {
			// setAttr("config", JsonKit.toJson(o));
			JSONObject h5 = config.getJson().getJSONObject("h5");
			if (!xx.isEmpty(h5)) {
				String s1 = h5.getString("list_left");
				if (!xx.isEmpty(s1)) {
					left = s1.split(" ");
				}
				String s2 = h5.getString("list_right");
				if (!xx.isEmpty(s2)) {
					right = s2.split(" ");
				}
			}
		}

		set("list_left", left);
		set("list_right", right);

		// 同步渲染
		// List<Record> list = Db.use(o.getDs()).findAll(o.getTable());
		// setAttr("list", JsonKit.toJson(list.subList(0,15)));

		render("/mobile/list.html");
		// renderEnjoy("/mobile/list.html");// 也可以Enjony模版
	}

	// 新增
	public void add() {
		String code = get(0);
		set("objectCode", code);
		MetaObject o = sm.meta.getMeta(code);
		setAttr("o", o);
		render("/mobile/add.html");
	}

	// 修改
	public void update() {
		String code = get(0);
		set("objectCode", code);
		MetaObject o = sm.meta.getMeta(code);
		setAttr("o", o);
		setAttr("id", get(1));
		render("/mobile/update.html");
	}

	// 详情
	public void detail() {
		String code = get(0);
		set("objectCode", code);
		MetaObject o = sm.meta.getMeta(code);
		setAttr("o", o);
		setAttr("id", get(1));
		render("/mobile/detail.html");
	}

	public void msg() {
		render("/mobile/msg.html");
	}

}