/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.i18n;

import java.util.HashMap;

import com.eova.common.utils.xx;

public class I18N extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;

	public String get(String key) {
		String s = super.get(key);
		if (xx.isEmpty(s)) {
			return key;
		}
		return s;
	}

}