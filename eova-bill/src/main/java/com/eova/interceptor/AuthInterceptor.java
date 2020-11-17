/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.interceptor;

import java.util.Set;

import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.common.utils.util.AntPathMatcher;
import com.eova.config.EovaConfig;
import com.eova.model.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * URI权限验证
 * 
 * @author Jieven
 * @date 2014-9-18
 */
public class AuthInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		String uri = inv.getController().getRequest().getRequestURI();

		AntPathMatcher pm = new AntPathMatcher();
		
		// 根目录功能+登录页免鉴权
		if (pm.match("/*", uri) || uri.endsWith("/toLogin")) {
			inv.invoke();
			return;
		}
		
		// 非登录免鉴权
		for (String pattern : LoginInterceptor.excludes) {
			if (pm.match(pattern, uri)) {
				inv.invoke();
				return;
			}
		}

		BaseController ctrl = (BaseController) inv.getController();
		User user = ctrl.getUser();
		// 上帝无所不能
		if (user.isAdmin()) {
			inv.invoke();
			return;
		}
		
		String msg = "对不起，您没有权限进行该操作：" + uri;
		
		// 当前角色分配授权
		Set<String> authUriPattern = user.get("auths");
		if (xx.isEmpty(authUriPattern)) {
			inv.getController().renderText(msg);
			return;
		}
		// 当前角色自定义授权
		Set<String> temp = EovaConfig.getAuthUris().get(user.getRid());
		if (!xx.isEmpty(temp)) {
			authUriPattern.addAll(temp);
		}
		// 所有角色公共授权
		temp = EovaConfig.getAuthUris().get(0);
		if(!xx.isEmpty(temp)){
			authUriPattern.addAll(temp);
		}
		// 检查授权
		for (String pattern : authUriPattern) {
			if (pm.match(pattern, uri)) {
				inv.invoke();
				return;
			}
		}
		if (uri.startsWith("/grid/query")) {
			// QueryAjax的友好提示
			inv.getController().renderJson(String.format("{\"code\": 1, \"msg\": \"%s\", \"count\":\"0\",\"data\": []}", "权限异常, 请检查当前菜单查询按钮的URI白名单是否存在:" + uri));
			return;
		}
		inv.getController().renderText(msg);
	}

}