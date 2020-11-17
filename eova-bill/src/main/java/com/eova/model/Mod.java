/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.model;

import java.util.List;

import com.eova.common.base.BaseModel;

/**
 * EOVA模块
 * 
 * @author Jieven
 * @date 2014-9-10
 */
public class Mod extends BaseModel<Mod> {

	private static final long serialVersionUID = 4254060861819273244L;

	public static final Mod dao = new Mod().dao();

	/**
	 * 组织编码
	 * @return
	 */
	public String getGroup() {
		return this.getStr("group_code");
	}

	/**
	 * 模块编码
	 * @return
	 */
	public String getCode() {
		return this.getStr("code");
	}

	public String getVersion() {
		return this.getStr("version");
	}

	public String toString() {
		return String.format("%s-%s", getGroup(), getCode(), getVersion());
	}

	public List<Mod> findByEnabled() {
		return this.find("select * from eova_mod where status = 1");
	}
}