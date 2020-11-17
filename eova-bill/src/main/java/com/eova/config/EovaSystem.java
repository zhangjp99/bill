/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.config;

import com.jfinal.server.undertow.UndertowServer;

/**
 * 全局系统配置
 * 启动时由主ClassLoader负责加载
 * @author Jieven
 *
 */
public class EovaSystem {

	/**
	 * 当前启动服务全局共享
	 */
	public static UndertowServer server = null;

}