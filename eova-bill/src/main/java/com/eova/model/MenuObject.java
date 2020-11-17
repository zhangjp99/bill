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

import com.alibaba.fastjson.JSON;
import com.eova.common.base.BaseModel;
import com.eova.common.utils.xx;
import com.jfinal.plugin.activerecord.Db;

/**
 * 菜单关联对象
 * 
 * @author Jieven
 * @date 2014-9-18
 */
public class MenuObject extends BaseModel<MenuObject> {

	private static final long serialVersionUID = 9176734392973431592L;

	public static final MenuObject dao = new MenuObject();

	public MetaFieldConfig getConfig() {
		String json = this.getStr("config");
		if (xx.isEmpty(json)) {
			return null;
		}
		return JSON.parseObject(json, MetaFieldConfig.class);
	}

	public void setConfig(MetaFieldConfig config) {
		this.set("config", JSON.toJSONString(config));
	}

	/**
	 * 获取菜单关联对象
	 * @param menuCode
	 * @return
	 */
	public List<MenuObject> queryByMenuCode(String menuCode) {
		return MenuObject.dao.queryByCache("select object_code from eova_menu_object where menu_code = ?", menuCode);
	}
	
	/**
	 * 删除菜单关联数据对象
	 * @param menuCode
	 */
	public void deleteByMenuCode(String menuCode) {
		Db.use(xx.DS_EOVA).update("delete from eova_menu_object where menu_code = ?", menuCode);
	}
	

}