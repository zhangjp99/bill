/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.oss.order;

import com.jfinal.core.Controller;

public class OrderController extends Controller {

	public void test() throws Exception {

		renderText("test");
	}

	public void order() throws Exception {
		renderText("order");
	}
}