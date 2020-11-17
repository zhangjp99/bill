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
 * 用户角色
 *
 * @author Jieven
 * @date 2014-9-10
 */
public class Role extends BaseModel<Role> {

	private static final long serialVersionUID = -1794335434198017392L;

	public static final Role dao = new Role();
	
	/**
	 * 获取下级角色
	 * @param lv
	 * @return
	 */
	public List<Role> findSubRole(int lv){
		return this.find("select * from eova_role where lv > ?", lv);
	}

	@Override
	public List<Role> findAll() {
		return this.find("select * from eova_role order by lv");
	}

}