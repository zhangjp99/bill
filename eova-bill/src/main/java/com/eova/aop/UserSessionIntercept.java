/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.aop;

import com.alibaba.fastjson.JSONObject;
import com.eova.model.User;

/**
 * 用户会话拦截器
 * @author Jieven
 *
 */
public interface UserSessionIntercept {

	public String loginBefore(User user);

	public void login(User user);

	public void logout(User user);

	public void su(User user, JSONObject switchUser);
}