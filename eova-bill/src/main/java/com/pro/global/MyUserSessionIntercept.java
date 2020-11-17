/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.pro.global;

import com.alibaba.fastjson.JSONObject;
import com.eova.aop.UserSessionIntercept;
import com.eova.model.User;
import com.jfinal.plugin.activerecord.Record;

/**
 * 用户登录拦截器
 * @author Jieven
 *
 */
public class MyUserSessionIntercept implements UserSessionIntercept {

	@Override
	public String loginBefore(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void login(User user) {
		// user数据结构: id, rid, 帐号, 登录源Record 如果登录源存在会自动续传的字段org_id, name

		// 从登录源表获取信息()
		Record data = user.getData();
		data.getStr("phone");
		// user.put("phone", data.getStr("phone")); // 例如:手机号
		// 使用: ${user.phone}

		// 从其它表获取信息
		// UserInfo info = UserInfo.dao.findById(user.get("id"));
		// if (info != null) {
		// user.put("info", info);
		// 使用: ${user.info.nickname}
		// }
	}

	@Override
	public void logout(User user) {
		// TODO Auto-generated method stub
	}

	@Override
	public void su(User user, JSONObject switchUser) {
		// TODO Auto-generated method stub

	}

}