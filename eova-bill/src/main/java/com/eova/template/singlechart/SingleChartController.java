/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.template.singlechart;

import java.util.List;

import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.core.menu.config.MenuConfig;
import com.eova.model.Button;
import com.eova.model.Menu;
import com.eova.model.MetaObject;
import com.eova.model.User;

/**
 * 业务模版：单表图(Grid ref Chart)
 * 
 * @author Jieven
 * 
 */
public class SingleChartController extends BaseController {

	public void list() {

		String menuCode = this.getPara(0);
		
		// 获取元数据
		Menu menu = Menu.dao.findByCode(menuCode);
		MenuConfig config = menu.getConfig();
		String objectCode = config.getObjectCode();
		MetaObject object = MetaObject.dao.getByCode(objectCode);
		if (object == null) {
			throw new RuntimeException("元对象不存在,请检查:" + objectCode);
		}

		// 根据权限获取功能按钮
		User user = this.getUser();
		List<Button> btnList = Button.dao.queryByMenuCode(menuCode, user.getRid());

		// 是否需要显示快速查询
		setAttr("isQuery", MetaObject.dao.isExistQuery(objectCode));

		setAttr("menu", menu);
		setAttr("btnList", btnList);
		setAttr("object", object);

		render(xx.getConfig("ui.template.singlechart", "/eova/template/singlechart/list.html"));
	}

}