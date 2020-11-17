/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.button;

import java.util.List;

import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.common.utils.db.DbUtil;
import com.eova.config.EovaConst;
import com.eova.model.Button;
import com.eova.model.RoleBtn;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.NestedTransactionHelpException;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;

/**
 * 按钮管理
 * 
 * @author Jieven
 * @date 2014-9-11
 */
public class ButtonController extends BaseController {

	// 菜单基本功能管理
	public void quick() {
		setAttr("menuCode", getPara(0));
		render("/eova/button/quick.html");
	}

	// 菜单基本功能管理
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void doQuick() {
		try {
			Button btn = new Button();
			String menuCode = getPara("menu_code");
			btn.set("menu_code", menuCode);
			Integer groupNum = getParaToInt("group_num", 0);
			btn.set("group_num", groupNum);
			btn.set("icon", getPara("icon"));
			btn.set("name", getPara("name"));
			btn.set("ui", getPara("ui"));
			// 选了模版按钮才必填(非模版按钮在UI中执行指定)
			String uri = getPara("uri");
			if (!xx.isEmpty(uri)) {
				btn.set("uri", uri);
			}
			btn.set("bs", xx.replaceBlank(getPara("bs")));
			// 计算最大排序值
			btn.set("order_num", Button.dao.getMaxOrderNum(menuCode, groupNum) + 1);
			btn.save();

			// 分配权限
			String roles = getPara("role", EovaConst.ADMIN_RID + "");
			for (String role : roles.split(",")) {
				RoleBtn rb = new RoleBtn();
				rb.set("rid", role);
				rb.set("bid", btn.get("id"));
				rb.save();
			}
			
			renderJson(new Easy());
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(new Easy("新增按钮失败,请看控制台日志寻找原因！"));
			throw new NestedTransactionHelpException("新增按钮失败");
		}
	}

	// 导出选中按钮数据
	public void doExport() {
		String ids = getPara(0);

		StringBuilder sb = new StringBuilder();

		String sql = "select * from eova_button where id in (" + ids + ")";
		List<Record> objects = Db.use(xx.DS_EOVA).find(sql);
		DbUtil.generateSql(xx.DS_EOVA, "eova_button", objects, "id", sb);

		renderText(sb.toString());
	}

}