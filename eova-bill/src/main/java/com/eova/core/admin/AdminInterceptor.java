/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.admin;

import com.eova.common.base.BaseController;
import com.eova.model.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 超级管理员拦截器
 * @author Jieven
 * 
 */
public class AdminInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {

		BaseController ctrl = (BaseController) inv.getController();
		User user = ctrl.getUser();
		if (!user.isAdmin()) {
			inv.getController().renderText("非法操作, 已报警, 请速速离去!");
			return;
		}

		inv.invoke();
	}
}