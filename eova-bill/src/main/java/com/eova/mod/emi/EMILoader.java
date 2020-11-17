/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.mod.emi;

import com.eova.common.utils.io.ClassUtil;

/**
 * Eova Mod Invoke Loader
 * @author Jieven
 */
public class EMILoader {

	public static EMI load(String group, String code, String className) {
		try {
			return (EMI) ClassUtil.newClass(String.format("com.eova.mod.%s.%s.%s", group, code, className));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T instance(String group, String code, String className) {
		try {
			return (T) ClassUtil.newClass(String.format("com.eova.mod.%s.%s.%s", group, code, className));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}