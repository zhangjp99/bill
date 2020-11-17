/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.utils.util;

public class SysUtil {
	/**
	 * 是否为Windows系统
	 * 
	 * @return
	 */
	public static boolean isWindows() {
		String osName = System.getProperty("os.name");
		return osName.contains("Windows");
	}
}