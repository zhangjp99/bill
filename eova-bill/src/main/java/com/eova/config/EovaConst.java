/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.config;

import java.io.File;
import java.util.HashMap;

import com.eova.common.utils.xx;
import com.jfinal.kit.PathKit;

/**
 * 系统配置
 *
 * @author Jieven
 * @date 2014-5-15
 */
public class EovaConst {

	/** 当前版本号 **/
	public static final String VERSION = "3.6.0";

	/** 默认 数据源名称 **/
	public static final String DS_MAIN = "main";
	/** EOVA 数据源名称 **/
	public static final String DS_EOVA = "eova";
	/** Oracle 默认Sequence前缀 **/
	public static final String SEQ_ = "seq_";

	/** 登录密码错误次数标识 **/
	public static final String LOGIN_NUM = "login_num";
	/** 登录用户标识 **/
	public static final String USER = "user";
	// TODO 3.x改为 $.login
	// 1.避免和前台业务SessionKey混淆
	// 2.login != user

	/** EovaDB缓存表 **/
	public static final String EOVA_CACHE = "eova_cache";

	/** 本地语言标识 **/
	public static final String LOCAL = "eova_local";

	/** Cache Key 所有菜单信息 **/
	public static final String ALL_MENU = "all_menu";

	/** 默认超级管理员角色(创建菜单自动给角色授权) **/
	public static final int ADMIN_RID = xx.getConfigInt("admin_rid", 1);

	/** 插件目录 **/
	public static final String DIR_PLUGINS = PathKit.getWebRootPath() + File.separator + "plugins" + File.separator;

	/** Eova插件包URL **/
	public static final String PLUGINS_URL = "http://7xign9.com1.z0.glb.clouddn.com/eova_plugins.zip";

	/** 虚拟字段标识 **/
	public static final String VIRTUAL = "virtual";

	/**
	 * 全局页面常量配置
	 * @return
	 */
	public static HashMap<String, String> getPageConf() {
		HashMap<String, String> map = new HashMap<>();
		map.put("STATIC", "domain_static");
		map.put("CDN", "domain_cdn");
		map.put("IMG", "domain_img");
		map.put("FILE", "domain_file");
		map.put("VER", "ver");
		map.put("ENV", "env");
		map.put("ZOOM", "ui.zoom");
		map.put("SKIN", "ui.skin");
		map.put("INCLUDE", "ui.include");
		return map;
	}

	/**
	 * 获取Eova版本号, 直接读取常量会被直接编译写死
	 * @return
	 */
	public static String getEovaVer() {
		return VERSION;
	}
}