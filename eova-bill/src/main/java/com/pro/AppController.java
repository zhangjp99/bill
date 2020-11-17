/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.pro;

import com.eova.common.utils.xx;
import com.eova.core.IndexController;

/**
 * 自定义 新增或重写 登录 注册 等各种默认系统业务！！！
 *
 * @author Jieven
 * @date 2016-05-11
 */
public class AppController extends IndexController {

	@Override
	public void toIndex() {
		super.toIndex();
		System.out.println("简单才是高科技，因为简单所以更快，降低70%开发成本");
		render("/eova/index.html");
	}

	@Override
	public void toLogin() {

		// 初始化帐号密码和提示
		if (xx.isEmpty(getAttr("msg"))) {
			setAttr("loginId", "eova");
			setAttr("loginPwd", "000000");
			// 方便第一次使用的新手和开发快速测试,不需要注释即可!
		}

		super.toLogin();
	}

}