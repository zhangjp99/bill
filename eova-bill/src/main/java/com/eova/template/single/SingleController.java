/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.template.single;

import java.util.List;

import com.eova.aop.AopContext;
import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.common.utils.io.ClassUtil;
import com.eova.common.utils.io.FileUtil;
import com.eova.core.menu.config.MenuConfig;
import com.eova.i18n.I18NBuilder;
import com.eova.model.Button;
import com.eova.model.Menu;
import com.eova.model.MetaObject;
import com.eova.model.User;
import com.eova.service.sm;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;

/**
 * 业务模版：单表(DataGrid)
 * 
 * @author Jieven
 * 
 */
public class SingleController extends BaseController {

	final Controller ctrl = this;

	/** 自定义拦截器 **/
	protected SingleIntercept intercept = null;

	public void list() {

		String menuCode = this.getPara(0);

		// 获取元数据
		Menu menu = Menu.dao.findByCode(menuCode);
		MenuConfig config = menu.getConfig();
		String objectCode = config.getObjectCode();
		MetaObject object = MetaObject.dao.getByCode(objectCode);
		if (object == null) {
			throw new RuntimeException("元对象不存在,请检查是否存在?元对象编码=" + objectCode);
		}

		// 根据权限获取功能按钮
		User user = this.getUser();
		List<Button> btnList = Button.dao.queryByMenuCode(menuCode, user.getRid());

		// 是否需要显示快速查询
		setAttr("isQuery", MetaObject.dao.isExistQuery(objectCode));

		setAttr("menu", menu);
		setAttr("btnList", btnList);
		setAttr("object", object);

		// TODO  test
		//		List<Record> list = Db.find("select * from users limit 0,30");
		//		setAttr("list", JsonKit.toJson(list));

		render(xx.getConfig("ui.template.single", "/eova/template/single/list.html"));
	}

	public void importXls() {
		String menuCode = this.getPara(0);

		Menu menu = Menu.dao.findByCode(menuCode);
		MetaObject object = sm.meta.getMeta(menu.getConfig().getObjectCode());

		boolean info = true;

		// 默认构造导出当前空数据[主键=0]模版
		String url = String.format("/grid/export/%s?type=xls&query_%s=0", object.getCode(), object.getPk());
		try {
			String importXls = menu.getConfig().getParams().getString("import_xls");
			if (!xx.isEmpty(importXls)) {
				url = importXls;
				info = false;
			}
		} catch (Exception e) {
		}

		// 显示导入提示(增量和覆盖)
		setAttr("info", info);
		// 导入处理URI
		setAttr("action", "/single_grid/doImportXls/" + menuCode);
		// 下载模版URL
		setAttr("template", url);

		render("/eova/template/common/import.html");
	}
	
	public void doImportXls() throws Exception {
		
		String menuCode = this.getPara(0);
		
		// 获取元数据
		Menu menu = Menu.dao.findByCode(menuCode);
		MenuConfig config = menu.getConfig();
		String objectCode = config.getObjectCode();
		final MetaObject object = sm.meta.getMeta(objectCode);
		
		intercept = ClassUtil.newClass(menu.getBizIntercept());

		// 默认上传到/temp 临时目录
		final UploadFile file = getFile("upfile", "/temp");
		if (file == null) {
			uploadCallback(false, I18NBuilder.get("上传失败，文件不存在"));
			return;
		}
		
		// 获取文件后缀
		String suffix = FileUtil.getFileType(file.getFileName());
		if (!suffix.equals(".xls")) {
			uploadCallback(false, I18NBuilder.get("请导入.xls格式的Excel文件"));
			return;
		}

		// 事务(默认为TRANSACTION_READ_COMMITTED)
		SingleAtom atom = new SingleAtom(file.getFile(), object, intercept, ctrl);
		boolean flag = Db.use(object.getDs()).tx(atom);

		if (!flag) {
			atom.getRunExp().printStackTrace();
			uploadCallback(false, atom.getRunExp().getMessage());
			return;
		}

		// 导入成功之后
		if (intercept != null) {
			try {
				AopContext ac = new AopContext(ctrl, atom.getRecords());
				intercept.importSucceed(ac);
			} catch (Exception e) {
				e.printStackTrace();
				uploadCallback(false, e.getMessage());
				return;
			}
		}

		uploadCallback(true, I18NBuilder.get("导入成功"));
	}

	// ajax 上传回调
	@NotAction
	public void uploadCallback(boolean succeed, String msg) {
		renderHtml("<script>parent.callback(\"" + msg + "\", " + succeed + ");</script>");
	}

}