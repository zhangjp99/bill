/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.admin;

import com.alibaba.fastjson.JSONObject;
import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.config.EovaConfig;
import com.eova.model.User;
import com.jfinal.aop.Before;

/**
 * EOVA超级管理功能
 * @author Jieven
 *
 */
@Before(AdminInterceptor.class)
public class AdminController extends BaseController {

	/**
	 * 升级控制台
	 */
	public void upgrade() {
		String isUpgrade = EovaConfig.getProps().get("isUpgrade");
		if (xx.isEmpty(isUpgrade) || !isUpgrade.equals("true")) {
			renderText("未开启升级模式，请启动配置 isUpgrade = true, 用完之后立马关掉,后果自负!");
			return;
		}

		render("/eova/admin/upgrade.html");
	}

	// 超级切换 
	public void su() {

		setAttr("ID", xx.getConfig("login.user.id", "id"));
		setAttr("RID", xx.getConfig("login.user.rid", "rid"));
		setAttr("NAME", xx.getConfig("login.user.name", "name"));
		setAttr("INFO", xx.getConfig("su.user.info", "${user.rid}:${user.name}[${user.id}]"));

		setAttr("objectCode", xx.getConfig("su.object.code", "eova_user_code"));

		setAttr("USERINFO", String.format("%s", getUser().getStr("company_id")));

		render("/eova/admin/su.html");
	}

	public void doSu() {

		String ID = xx.getConfig("login.user.id", "id");
		String RID = xx.getConfig("login.user.rid", "rid");
		String NAME = xx.getConfig("login.user.name", "name");

		JSONObject o = getSelectRow();
		if (o == null) {
			renderJson(Easy.fail("请选择一行"));
			return;
		}

		User user = getUser();
		user.set("id", o.get(ID));
		user.put("su_rid", o.get(RID + "_val"));
		user.set("name", o.get(NAME));
		// 默认切部门, 其它的数据和参数通过拦截器干预
		Integer orgId = o.getInteger("org_id_val");
		if (orgId != null) {
			user.put("org_id", orgId);
		}

		// 用户切换AOP
		if (EovaConfig.getUserSessionIntercept() != null) {
			EovaConfig.getUserSessionIntercept().su(user, o);
		}

		updateUser(user);

		renderJson(Easy.sucess());
	}

}