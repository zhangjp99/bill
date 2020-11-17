/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.oss.test;


import com.eova.common.base.BaseController;

/**
 * 测试Ctrl
 * @author Jieven
 *
 */
public class TestController extends BaseController {

	public void remote() {
		String name = getPara("name");
		if (name.indexOf("SB") != -1) {
			renderText("请不要说脏话" + name);
			return;
		} 
		renderText("");
	}
}