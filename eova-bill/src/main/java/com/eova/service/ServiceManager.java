/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.service;

/**
 * 服务管理中心
 * 
 * @author Jieven
 *
 */
public class ServiceManager {

	/** 登录服务 **/
	public static LoginService login;
	/** 权限服务 **/
	public static AuthService auth;
	/** 元服务 **/
	public static MetaService meta;
	/** 动态表单服务 **/
	public static FormService form;
	/** 文件服务 **/
	// public static FileService file;

	public static void init() {
		login = new LoginService();
		auth = new AuthService();
		meta = new MetaService();
		form = new FormService();
		// file = new FileService();
	}
}