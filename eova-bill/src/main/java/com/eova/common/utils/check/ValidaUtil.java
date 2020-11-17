/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.utils.check;

import com.eova.common.utils.util.RegexUtil;

public class ValidaUtil {
	private static String regex = "[$^&<>'/]";

	/**
	 * 是否安全字符串(不包含$^&<>'/)
	 * @param str
	 * @return
	 */
	public static boolean isSecStr(String str){
		return !RegexUtil.isExist(regex, str);
	}
}