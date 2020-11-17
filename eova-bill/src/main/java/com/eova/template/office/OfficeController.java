/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.template.office;

import java.util.HashMap;
import java.util.List;

import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.render.OfficeRender;
import com.eova.common.utils.xx;
import com.eova.common.utils.io.ClassUtil;
import com.eova.model.Button;
import com.eova.model.Menu;
import com.eova.model.User;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;

/**
 * 业务模版：Office
 * 
 * @author Jieven
 * 
 */
public class OfficeController extends BaseController {

	final Controller ctrl = this;

	/** 自定义拦截器 **/
	protected OfficeIntercept intercept = null;

	public void list() {
		String menuCode = this.getPara(0);
		
		// 获取元数据
		Menu menu = Menu.dao.findByCode(menuCode);

		// 根据权限获取功能按钮(非必须)
		User user = this.getUser();
		if (user != null) {
			List<Button> btnList = Button.dao.queryByMenuCode(menuCode, user.getRid());
			setAttr("btnList", btnList);
		}

		setAttr("menu", menu);

		// 参数传递
		String query = this.getRequest().getQueryString();
		setAttr("para", this.getPara() + (xx.isEmpty(query) ? "" : '?' + query));

		render("/eova/template/office/list.html");
	}

	// 显示文档
	public void show() throws Exception {
		String menuCode = this.getPara(0);

		Menu menu = Menu.dao.findByCode(menuCode);
		String template = menu.getStr("url");

		intercept = ClassUtil.newClass(menu.getBizIntercept());

		HashMap<String, Object> data = new HashMap<>();

		if (intercept != null) {
			try {
				template = intercept.init(ctrl, data, template);
			} catch (Exception e) {
				renderText(e.getMessage());
				return;
			}
		}

		for (String key : data.keySet()) {
			Object o = data.get(key);
			setAttr(key, o);
		}

		render(template);
	}

	// 下载文档
	public void file() throws Exception {

		String menuCode = this.getPara(0);

		Menu menu = Menu.dao.findByCode(menuCode);
		String template = menu.getStr("url");

		intercept = ClassUtil.newClass(menu.getBizIntercept());

		HashMap<String, Object> data = new HashMap<>();

		String fileType = menu.getConfig().getParams().getString("office_type");
		String fileName = menu.getStr("name") + '.' + fileType;

		if (intercept != null) {
			try {
				template = intercept.init(ctrl, data, template);
			} catch (Exception e) {
				renderJson(new Easy(e.getMessage()));
				return;
			}
		}

		String path = PathKit.getWebRootPath() + template;

		render(new OfficeRender(fileType, fileName, path, data));
	}

}