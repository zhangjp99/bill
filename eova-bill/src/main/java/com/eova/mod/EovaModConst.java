/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.mod;

import java.io.File;
import java.util.ArrayList;

import com.jfinal.kit.PathKit;

public class EovaModConst {
	/** MOD Jar 目录 **/
	public static final String DIR_MOD = dirMod();
	/** MOD View 目录 **/
	public static final String DIR_MOD_VIEW = dirModView();

	/**
	 * 获取资源目录
	 * @return
	 */
	public static String getResourcesPath() {
		return new File(PathKit.getWebRootPath()).getParent() + File.separator + "resources";
	}

	public static String dirMod() {
		StringBuilder sb = new StringBuilder(PathKit.getWebRootPath());
		sb.append(File.separator);
		sb.append("WEB-INF");
		sb.append(File.separator);
		sb.append("mod");
		sb.append(File.separator);
		return sb.toString();
	}

	public static String dirModView() {
		StringBuilder sb = new StringBuilder(PathKit.getWebRootPath());
		sb.append(File.separator);
		sb.append("_mod");
		sb.append(File.separator);
		return sb.toString();
	}

	/**
	 * 合法资源文件白名单
	 * @return
	 */
	public static String[] getResourceSuffix() {
		ArrayList<String> config = new ArrayList<>();
		config.add(".html");
		config.add(".htm");
		config.add(".css");
		config.add(".js");
		config.add(".jpg");
		config.add(".jpeg");
		config.add(".png");
		config.add(".bmp");
		config.add(".gif");
		config.add(".webp");
		config.add(".svg");
		config.add(".ttf");
		config.add(".woff");
		config.add(".woff2");
		config.add(".webp");
		config.add(".sql");

		return config.toArray(new String[config.size()]);
	}

}