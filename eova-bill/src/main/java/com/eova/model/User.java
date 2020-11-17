/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.model;

import com.eova.common.base.BaseModel;
import com.eova.config.EovaConst;
import com.jfinal.plugin.activerecord.Record;

public class User extends BaseModel<User> {

	private static final long serialVersionUID = 1064291771401662738L;

	public static final User dao = new User().dao();

	public Role role;
	public Record data;// 登录源用户数据

	public Object getId() {
		return this.get("id");
	}

	public int getRid(){
		// 优先获取临时切换角色
		Integer suRid = this.getInt("su_rid");
		if (suRid != null) {
			return suRid;
		}

		return this.getInt("rid");
	}

	/**
	 * 是否超级管理员
	 * @return
	 */
	public boolean isAdmin(){
		return getIsAdmin();
	}
	
	// 为兼容模版取值
	public boolean getIsAdmin(){
		// 按真实角色判断是否为管理员
		if (this.getInt("rid") == EovaConst.ADMIN_RID) {
			return true;
		}
		return false;
	}

	public void initRole() {
		this.role = Role.dao.findById(this.getInt("rid"));
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getName() {
		return this.getStr("name");
	}

	public int getOrgId() {
		return this.getInt("org_id");
	}

	/**
	 * 获取登录源用户数据
	 * @return
	 */
	public Record getData() {
		return data;
	}

	public void setData(Record data) {
		this.data = data;
	}

}