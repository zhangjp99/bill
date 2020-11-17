/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.interceptor;

import java.util.ArrayList;

import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.common.utils.util.AntPathMatcher;
import com.eova.common.utils.web.WebUtil;
import com.eova.config.EovaConfig;
import com.eova.config.EovaConst;
import com.eova.i18n.I18NBuilder;
import com.eova.model.User;
import com.eova.service.LoginService;
import com.eova.service.sm;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 登录拦截器
 * @author Jieven
 * 
 */
public class LoginInterceptor implements Interceptor {

	/**
	 * 登录拦截排除URI<br>
	 * ?  匹配任何单字符<br> 
	 * *  匹配0或者任意数量的字符<br> 
	 * ** 匹配0或者更多的目录 <br>
	 */
	public static ArrayList<String> excludes = new ArrayList<String>();
	public static void initNoLogin(String index) {
		excludes.add(index + "captcha");
		excludes.add(index + "toLogin");
		excludes.add(index + "doExit");
		excludes.add(index + "doLogin");
	}

	@Override
	public void intercept(Invocation inv) {
		
		String uri = inv.getActionKey();

		AntPathMatcher pm = new AntPathMatcher();
		for (String pattern : excludes) {
			if (pm.match(pattern, uri)) {
				inv.invoke();
				return;
			}
		}

		BaseController ctrl = (BaseController) inv.getController();
		User user = ctrl.getUser();
		if (user == null) {
			String ip = WebUtil.getRealIp(ctrl.getRequest());
			user = sm.login.loginBySid(ctrl.SID(), ip);
			if (user == null) {
				// SID登录失败，销毁Cookie
				ctrl.removeCookie(LoginService.CKSID);
				ctrl.redirect(EovaConfig.EOVA_INDEX + "toLogin");
				return;
			}
			// 登录初始化
			if (EovaConfig.getUserSessionIntercept() != null) {
				EovaConfig.getUserSessionIntercept().login(user);
				// 更新用户缓存
				ctrl.updateUser(user);
			}
		}

		// 续传 登录用户 用于其他场景 比如模版的Web域
		ctrl.setAttr(LoginService.USER, user);

		syncThreadLocal(inv);

		inv.invoke();
	}

	/**
	 * 线程数据同步
	 * PS:服务器网络模型不同导致新建线程策略不同, 可能每次请求都会被一个新线程处理, 需要再此续传.
	 * @param inv
	 */
	public void syncThreadLocal(Invocation inv) {
		if (xx.getConfigBool("isI18N", false)) {
			String local = (String) inv.getController().getCookie(EovaConst.LOCAL);
			if (!xx.isEmpty(local)) {
				I18NBuilder.setLocal(local);
				inv.getController().set("LOCAL", local);
			}
		}

	}
}