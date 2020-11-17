/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.widget.form;

import java.sql.SQLException;
import java.util.ArrayList;

import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;
import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.model.MetaField;
import com.eova.model.MetaObject;
import com.eova.service.sm;
import com.eova.template.common.util.TemplateUtil;
import com.eova.widget.WidgetManager;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

/**
 * Form组件
 * 
 * @author Jieven
 * 
 */
public class FormController extends BaseController {

	final Controller ctrl = this;

	/** 自定义拦截器 **/
	protected MetaObjectIntercept intercept = null;

	/** 异常信息 **/
	private String errorInfo = "";

	/** 当前操作的主对象 **/
	private final Record record = new Record();

	public void add() throws Exception {
		String objectCode = this.getPara(0);
		MetaObject object = sm.meta.getMeta(objectCode);

		// 字段禁用默认对新增无效
		for (MetaField mf : object.getFields()) {
			mf.put("is_disable", false);
		}

		// 构建关联参数值
		Record fixed = WidgetManager.getRef(this);
		
		// 业务拦截
		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());
		if (intercept != null) {
			AopContext ac = new AopContext(ctrl);
			ac.object = object;
			ac.fixed = fixed;
			intercept.addInit(ac);
		}
		
		setAttr("fixed", fixed);
		setAttr("object", object);
		render("/eova/widget/form/add.html");
	}

	public void doAdd() throws Exception {

		String objectCode = this.getPara(0);

		final MetaObject object = sm.meta.getMeta(objectCode);

		// 获取当前操作数据
		WidgetManager.buildData(this, object, record, object.getPk(), true);

		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());
		// 事务(默认为TRANSACTION_READ_COMMITTED)
		boolean isSucceed = Db.use(object.getDs()).tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					AopContext ac = new AopContext(ctrl, record);
					ac.object = object;

					// 新增前置任务
					if (intercept != null) {
						String msg = intercept.addBefore(ac);
						if (!xx.isEmpty(msg)) {
							errorInfo = msg;
							return false;
						}
					}

					if (!xx.isEmpty(object.getTable())) {
						// 剥离虚拟字段
						Record t = WidgetManager.peelVirtual(record);
						Db.use(object.getDs()).save(object.getTable(), object.getPk(), record);
						// 还原虚拟字段
						record.setColumns(t);
					} else {
						// update view
						// WidgetManager.operateView(TemplateConfig.ADD, object, record);
						// 视图无法自动操作，请自定义元对象业务拦截完成持久化逻辑！
					}

					// 新增后置任务
					if (intercept != null) {
						String msg = intercept.addAfter(ac);
						if (!xx.isEmpty(msg)) {
							errorInfo = msg;
							return false;
						}
					}
				} catch (Exception e) {
					errorInfo = TemplateUtil.buildException(e);
					return false;
				}
				return true;
			}
		});

		// 新增成功之后
		if (isSucceed && intercept != null) {
			try {
				ArrayList<Record> records = new ArrayList<Record>();
				records.add(record);

				AopContext ac = new AopContext(this, records);
				ac.object = object;
				String msg = intercept.addSucceed(ac);
				if (!xx.isEmpty(msg)) {
					errorInfo = msg;
				}
			} catch (Exception e) {
				errorInfo = TemplateUtil.buildException(e);
			}
		}

		if (!xx.isEmpty(errorInfo)) {
			renderJson(Easy.fail(errorInfo));
			return;
		}

		renderJson(new Easy());
	}

	public void update() throws Exception {

		// 获取关联参数
		Record fixed = WidgetManager.getRef(this);

		AopContext ac = new AopContext(ctrl);
		ac.fixed = fixed;
		
		// 初始化数据
		MetaObject object = buildFormData(true, ac);
		// 业务拦截
		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());
		if (intercept != null) {
			intercept.updateInit(ac);
		}

		setAttr("fixed", fixed);
		
		render("/eova/widget/form/update.html");
	}

	public void doUpdate() throws Exception {

		String objectCode = this.getPara(0);

		final MetaObject object = sm.meta.getMeta(objectCode);

		// 获取当前操作数据
		WidgetManager.buildData(this, object, record, object.getPk(), false);
		//final Object pkValue = record.get(object.getPk());

		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());
		// 事务(默认为TRANSACTION_READ_COMMITTED)
		boolean isSucceed = Db.use(object.getDs()).tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					AopContext ac = new AopContext(ctrl, record);
					ac.object = object;

					// 修改前置任务
					if (intercept != null) {
						String msg = intercept.updateBefore(ac);
						if (!xx.isEmpty(msg)) {
							errorInfo = msg;
							return false;
						}
					}

					if (!xx.isEmpty(object.getTable())) {
						// 剥离虚拟字段
						Record t = WidgetManager.peelVirtual(record);
						Db.use(object.getDs()).update(object.getTable(), object.getPk(), record);
						// 还原虚拟字段
						record.setColumns(t);
					} else {
						// update view
						// WidgetManager.operateView(TemplateConfig.UPDATE, object, record);
						// 视图无法自动操作，请自定义元对象业务拦截完成持久化逻辑！
					}

					// 修改后置任务
					if (intercept != null) {
						String msg = intercept.updateAfter(ac);
						if (!xx.isEmpty(msg)) {
							errorInfo = msg;
							return false;
						}
					}
				} catch (Exception e) {
					errorInfo = TemplateUtil.buildException(e);
					return false;
				}
				return true;
			}

		});

		// 修改成功之后
		if (isSucceed && intercept != null) {
			try {
				ArrayList<Record> records = new ArrayList<Record>();
				records.add(record);

				AopContext ac = new AopContext(this, records);
				ac.object = object;
				String msg = intercept.updateSucceed(ac);
				if (!xx.isEmpty(msg)) {
					errorInfo = msg;
				}
			} catch (Exception e) {
				errorInfo = TemplateUtil.buildException(e);
			}
		}

		//		if (1 == 1) {
		//			renderJson(Easy.fail("服务端业务异常"));
		//			return;
		//		}

		if (!xx.isEmpty(errorInfo)) {
			renderJson(Easy.fail(errorInfo));
			return;
		}

		renderJson(new Easy());
	}

	public void detail() throws Exception {
		AopContext ac = new AopContext(ctrl);
		MetaObject object = buildFormData(false, ac);
		// 业务拦截
		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());
		if (intercept != null) {
			intercept.detailInit(ac);
		}
		render("/eova/widget/form/detail.html");
	}

	public void diy() throws Exception {
		String code = this.getPara(0);

		// 获取自定义表单信息
		Record form = Db.use(xx.DS_EOVA).findById("eova_form", "code", code);
		String object_code = form.getStr("object_code");
		String fields = form.getStr("fields");
		String action = form.getStr("action");

		setAttr("code", code);
		setAttr("action", action);
		setAttr("fields", fields);

		MetaObject object = MetaObject.dao.getByCode(object_code);

		// 构建关联参数值
		Record fixed = WidgetManager.getRef(this);

		setAttr("fixed", fixed);
		setAttr("object", object);
		render("/eova/widget/form/diy.html");
	}

	/**
	 * 构建对象数据
	 */
	private MetaObject buildFormData(boolean isEdit, AopContext ac) {
		String objectCode = this.getPara(0);
		// 获取主键的值
		Object pkValue = getPara(1);

		MetaObject object = sm.meta.getMeta(objectCode);
		ac.object = object;

		// 根据主键获取对象
		Record record = null;
		// EOVA系统内部特殊处理 如果是元对象,自动识别根据编码查询
		if (objectCode.equals("eova_object_code") && !xx.isNum(pkValue)) {
			MetaObject mo = MetaObject.dao.getByCode(pkValue.toString());
			record = new Record().setColumns(mo);
		} else {
			record = Db.use(object.getDs()).findById(object.getView(), object.getPk(), object.buildPkValue(pkValue));
		}

		setAttr("record", record);
		setAttr("object", object);
		ac.record = record;

		return object;
	}


}