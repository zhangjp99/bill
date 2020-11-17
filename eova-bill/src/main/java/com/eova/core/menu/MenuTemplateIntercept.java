/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.menu;

import com.eova.common.utils.xx;
import com.eova.model.Menu;
import com.eova.template.singletree.SingleTreeController;
import com.eova.template.singletree.SingleTreeIntercept;
import com.jfinal.kit.Kv;

/**
 * 菜单的模版业务拦截器
 * @author Jieven
 *
 */
public class MenuTemplateIntercept extends SingleTreeIntercept {

	@Override
	public void drop(Kv kv) throws Exception {
		String type = kv.getStr("type");// 拖拽类型
		// String sid = kv.getStr("sid");// 被拖拽原始节点ID
		Integer tid = xx.toInt(kv.getStr("tid"));// 拖拽到目标节点ID
		Menu menu = Menu.dao.findById(tid);
		// 拖拽成为子节点
		if (type.equals(SingleTreeController.DRAG_INNER)) {
			// 目标节点非目录
			if (!menu.getStr("type").equals(Menu.TYPE_DIR)) {
				throw new Exception("目标节点不是目录,移动失败,请刷新页面重新操作");
			}
		}
	}

}