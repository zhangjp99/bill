/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.template.singletree;

import java.util.List;

import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.common.utils.io.ClassUtil;
import com.eova.core.menu.config.MenuConfig;
import com.eova.core.menu.config.TreeConfig;
import com.eova.model.Button;
import com.eova.model.Menu;
import com.eova.model.MetaObject;
import com.eova.model.User;
import com.eova.service.sm;
import com.eova.template.common.util.TemplateUtil;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 业务模版:单表树(TreeGrid)
 * 
 * @author Jieven
 * 
 */
public class SingleTreeController extends BaseController {

	/** 业务拦截器 **/
	protected SingleTreeIntercept intercept = null;

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

		render("/eova/template/singletree/list.html");
	}

	public static final String DRAG_INNER = "inner";// 成为子节点
	public static final String DRAG_PREV = "prev";// 成为同级前一个节点
	public static final String DRAG_NEXT = "next";// 成为同级后一个节点

	// 拖拽处理
	public void drag() throws Exception {
		String menuCode = this.getPara(0);

		// 获取元数据
		Menu menu = Menu.dao.findByCode(menuCode);
		MenuConfig config = menu.getConfig();
		String objectCode = config.getObjectCode();
		MetaObject object = sm.meta.getMeta(objectCode);
		if (object == null) {
			throw new RuntimeException("元对象不存在,请检查:" + objectCode);
		}

		// 获取配置
		TreeConfig tree = config.getTree();

		// 初始化拦截器
		intercept = ClassUtil.newClass(menu.getBizIntercept());

		String type = getPara(1);
		Object sid = getPara(2);
		Object tid = getPara(3);

		try {
			// 拖拽完成时拦截
			if (intercept != null) {
				intercept.drop(Kv.by("type", type).set("sid", sid).set("tid", tid));
			}
		} catch (Exception e) {
			String errorInfo = TemplateUtil.buildException(e);
			renderJson(Easy.fail(errorInfo));
			return;
		}
		
		// Tree Config
		String orderField = tree.getOrderField();
		String idField = tree.getIdField();
		String parentField = tree.getParentField();

		int num = 9;// 默认序,可进可退
		Object pid = tree.getRootPid();// 默认拖拽到根下
		
		// 目标非根的处理
		if (!tid.equals("root")) {
			// PGSQL 强类型兼容
			Record target = Db.use(object.getDs()).findById(object.getView(), tid);
			num = target.getInt(orderField);
			String id = target.getStr(idField);
			pid = target.get(parentField);

			if (type.equals(DRAG_INNER)) {
				// 认干爹:移动到某父下, 认爹做父
				pid = id;
			} else {
				// 走亲戚:移动到某点前后, 入乡随俗
				if (type.equals(DRAG_PREV)) {
					num--;
				} else if (type.equals(DRAG_NEXT)) {
					num++;
				}
			}
		}

		// 精准类型处理
		if (xx.isPgsql()) {
			pid = object.buildFieldValue(tree.getParentField(), pid);
			sid = object.buildFieldValue("id", sid);
			tid = object.buildFieldValue("id", tid);
		}

		Db.use(object.getDs()).update(String.format("update %s set %s = ?, %s = ? where %s = ?", object.getTable(), orderField, parentField, idField), num, pid, sid);

		// 排序优化算法:自动优化本身的乱序, 目标节点序号之后的节点+1
		String sql = String.format("update %s set %s = %s + 1 where %s = ? and %s > ?", object.getTable(), orderField, orderField, parentField, orderField);
		Db.use(object.getDs()).update(sql, pid, num);

		renderJson(Easy.sucess());
	}

}