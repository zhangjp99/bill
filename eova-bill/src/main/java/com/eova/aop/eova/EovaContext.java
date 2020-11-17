/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.aop.eova;

import com.eova.common.base.BaseController;
import com.eova.engine.EovaExp;
import com.eova.model.Menu;
import com.eova.model.MetaObject;
import com.eova.model.User;
import com.jfinal.core.Controller;

/**
 * Eova全局业务拦截器上下文
 *
 * @author Jieven
 * @date 2014-8-29
 */
public class EovaContext {

	/**
	 * 当前控制器
	 */
	public Controller ctrl;

	/**
	 * 当前用户对象
	 */
	public User user;

	/**
	 * 当前菜单
	 */
	public Menu menu;

	/**
	 * 当前元对象
	 * 元字段=object.fields
	 * 
	 */
	public MetaObject object;

	/**
	 * 当前操作表达式
	 */
	public EovaExp exp;

	public EovaContext(Controller ctrl) {
		this.ctrl = ctrl;
		this.user = ((BaseController) ctrl).getUser();
	}

	public int UID() {
		return this.user.get("id");
	}
}