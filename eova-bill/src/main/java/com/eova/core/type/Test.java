/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.type;

import java.math.BigDecimal;

public class Test {

	public static void main(String[] args) {
		String num = "9.8888888888888888888";
		System.out.println(Float.valueOf(num)); // 输出:99.888885 				8,8-2
		System.out.println(Double.valueOf(num)); // 输出:99.88888888888889		16,16-2
		System.out.println(new BigDecimal(num)); // 输出:99.8888888888888888888	支持最大
	}

}