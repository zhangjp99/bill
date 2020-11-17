/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.user;

import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.utils.EncryptUtil;
import com.eova.common.utils.xx;
import com.eova.model.User;

/**
 * 自定义用户管理
 * 
 * @author Jieven
 * @date 2014-9-11
 */
public class UserController extends BaseController {

	// 修改密码
	public void pwd() {
		Integer id = xx.toInt(getSelectValue("id"));
		String val = getInputValue();

		if (xx.isEmpty(val)) {
			renderJson(new Easy("密码不能为空！"));
			return;
		}
		if (val.length() < 6) {
			renderJson(new Easy("密码不能少于6位！"));
			return;
		}

		User user = new User();
		user.set("id", id);
		user.set("login_pwd", EncryptUtil.getSM32(val));
		user.update();

		renderJson(new Easy());
	}

}