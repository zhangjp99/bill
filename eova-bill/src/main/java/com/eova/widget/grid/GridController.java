/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.widget.grid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;
import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.render.CsvRender;
import com.eova.common.render.XlsRender;
import com.eova.common.utils.xx;
import com.eova.config.EovaConfig;
import com.eova.config.PageConst;
import com.eova.model.Menu;
import com.eova.model.MetaField;
import com.eova.model.MetaObject;
import com.eova.service.sm;
import com.eova.template.common.util.TemplateUtil;
import com.eova.widget.WidgetManager;
import com.eova.widget.WidgetUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * Grid组件
 * 
 * @author Jieven
 * 
 */
public class GridController extends BaseController {

	final Controller ctrl = this;

	/** 元对象业务拦截器 **/
	protected MetaObjectIntercept intercept = null;

	/** 异常信息 **/
	private String errorInfo = "";

	/**
	 * 导出查询
	 * 
	 * @throws Exception
	 */
	public void export() throws Exception {

		String objectCode = getPara(0);
		String menuCode = getPara("menu_code");
		String type = getPara("type", "csv");// xls csv

		MetaObject object = sm.meta.getMeta(objectCode);
		Menu menu = Menu.dao.findByCode(menuCode);

		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());

		// 元数据拦截器
		if (intercept != null) {
			AopContext ac = new AopContext(this);
			ac.object = object;// 只读
			ac.fields = object.getFields();// 读写
			intercept.metadata(ac);
		}

		// 构建查询
		List<Object> parmList = new ArrayList<Object>();
		String sql = WidgetManager.buildQuerySQL(ctrl, menu, object, intercept, parmList, true, "");

		// 转换SQL参数
		Object[] paras = new Object[parmList.size()];
		parmList.toArray(paras);

		Long count = Db.use(object.getDs()).queryLong("select count(*) " + sql, paras);
		int maxXls = xx.getConfigInt("export.max.xls", 1 * 10000);
		int maxCsv = xx.getConfigInt("export.max.csv", 10 * 10000);
		if (count != null && count > maxXls) {
			if (type.equals("xls")) {
				// 如需导出大数据, 请采用其它方案自定义实现. 比如POI, 分布式导出等.
				renderText(String.format("[导出XLS]一次最多只能导出%s条, 超过%s条请使用[导出CSV]", maxXls, maxXls));
				return;
			} else if (type.equals("csv") && count > maxCsv) {
				// 大数据导出非常占用内存, 所以也建议单独一个项目实现大数据导致, 避免宕机影响其它业务.
				renderText(String.format("[导出CSV]一次最多只能导出%s条, 超过%s条请联系管理员DB导出", maxCsv, maxCsv));
				return;
			}
		}

		List<Record> data = Db.use(object.getDs()).find("select * " + sql, paras);

		// 查询后置任务
		if (intercept != null) {
			AopContext ac = new AopContext(ctrl, data);
			ac.object = object;
			intercept.queryAfter(ac);
		}

		List<MetaField> fields = object.getFields();

		// 根据表达式将数据中的值翻译成汉字
		WidgetManager.convertValueByExp(this, fields, data);

		Iterator<MetaField> it = fields.iterator();
		while (it.hasNext()) {
			MetaField f = it.next();
			if (!f.getBoolean("is_show")) {
				it.remove();
			}
		}

		if (type.equals("xls")) {
			render(new XlsRender(data, fields, object));
		} else if (type.equals("csv")) {
			render(new CsvRender(data, fields, object));
		} else {
			renderText("仅支持导出xls和csv");
		}
	}

	/**
	 * 数据查询
	 * 
	 * @throws Exception
	 */
	public void query() throws Exception {

		String objectCode = getPara(0);
		String menuCode = getPara(1);
		int pageNumber = getParaToInt(PageConst.PAGENUM, 1);
		int pageSize = getParaToInt(PageConst.PAGESIZE, 100000);

		MetaObject object = sm.meta.getMeta(objectCode);
		Menu menu = Menu.dao.findByCode(menuCode);

		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());

		// 构建查询
		List<Object> paras = new ArrayList<Object>();

		String select = "select " + WidgetManager.buildSelect(object, RID());
		String sql = WidgetManager.buildQuerySQL(ctrl, menu, object, intercept, paras, true, select);
		// 自定义全SQL, 去除包装Form并查全部字段, 敏感字段自行控制.
		if (sql.contains(") EVIEW")) {
			select = "select *";
		}

		if (sql.equals("from virtual")) {
			renderJson("{\"code\": 0, \"msg\": \"这是一个虚拟元对象, 请在MetaObjectIntercept.queryBefore()中编写SQL查询逻辑\", \"count\":\"0\",\"data\": []}");
			return;
		}

		Page<Record> page = Db.use(object.getDs()).paginate(pageNumber, pageSize, select, sql, xx.toArray(paras));

		// 查询后置任务
		if (intercept != null) {
			AopContext ac = new AopContext(ctrl, page.getList());
			ac.object = object;
			intercept.queryAfter(ac);
		}

		// 备份Value列，然后将值列转换成Key列
		WidgetUtil.copyValueColumn(page.getList(), object.getPk(), object.getFields());
		// 根据表达式将数据中的值翻译成汉字
		WidgetManager.convertValueByExp(this, object.getFields(), page.getList());

		// 构建JSON数据
		String ui = xx.getConfig("ui", "layui");
		if (ui.equals("easyui")) {
			ui = "{\"total\":%s,\"rows\": %s}";
		} else if (ui.equals("layui")) {
			ui = "{\"code\": 0, \"msg\": \"\", \"count\":\"%s\",\"data\": %s}";
		}
		StringBuilder sb = new StringBuilder(String.format(ui, page.getTotalRow(), JsonKit.toJson(page.getList())));

		// Footer
		if (intercept != null) {
			AopContext ac = new AopContext(ctrl, page.getList());
			ac.object = object;
			Kv footer = intercept.queryFooter(ac);
			if (footer != null) {
				sb.insert(sb.length() - 1, String.format(",\"footer\":[%s]", footer.toJson()));
			}
		}

		renderJson(sb.toString());
	}

	/**
	 * 新增
	 * @throws Exception 
	 */
	public void add() throws Exception {
		String objectCode = getPara(0);
		final MetaObject object = sm.meta.getMeta(objectCode);

		String json = getPara("rows");
		final List<Record> records = getRecordsByJson(json, object.getFields(), object.getPk());

		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());
		// 事务
		Db.use(object.getDs()).tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					for (Record record : records) {

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
						if (xx.isEmpty(object.getTable())) {
							throw new Exception("视图暂时不支持Grid 单元格编辑，请使用Form模式！");
						}
						Db.use(object.getDs()).save(object.getTable(), object.getPk(), record);
						// 新增后置任务
						if (intercept != null) {
							String msg = intercept.addAfter(ac);
							if (!xx.isEmpty(msg)) {
								errorInfo = msg;
								return false;
							}
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
		if (intercept != null) {
			try {
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

	/**
	 * 删除
	 * @throws Exception 
	 */
	public void delete() throws Exception {
		deleteOrHide(true);
	}

	/**
	 * 隐藏
	 * @throws Exception 
	 */
	public void hide() throws Exception {
		deleteOrHide(false);
	}

	/**
	 * 删除或者隐藏
	 * @throws Exception
	 */
	private void deleteOrHide(final boolean isDel) throws Exception {
		String objectCode = getPara(0);
		final MetaObject object = sm.meta.getMeta(objectCode);

		String json = getPara("rows");

		final List<Record> records = getRecordsByJson(json, object.getFields(), object.getPk());

		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());
		// 事务
		Db.use(object.getDs()).tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					for (Record record : records) {

						AopContext ac = new AopContext(ctrl, record);
						ac.object = object;

						// 删除前置任务
						if (intercept != null) {
							String msg = null;
							if (isDel) {
								msg = intercept.deleteBefore(ac);
							} else {
								msg = intercept.hideBefore(ac);
							}
							if (!xx.isEmpty(msg)) {
								errorInfo = msg;
								return false;
							}
						}
						if (!xx.isEmpty(object.getTable())) {
							String pk = object.getPk();
							String pkValue = record.get(pk).toString();

							if (isDel) {
								// 删除数据
								Db.use(object.getDs()).deleteById(object.getTable(), pk, object.buildPkValue(pkValue));
							} else {
								// 隐藏数据
								String hideFieldName = xx.getConfig("hide_field_name", "is_hide");
								String sql = String.format("update %s set %s = 1 where %s = ?", object.getTable(), hideFieldName, pk);
								Db.use(object.getDs()).update(sql, pkValue);
							}
						} else {
							// 视图无法自动删除，请自定义元对象业务拦截完成删除逻辑！
							// MetaObjectIntercept.deleteBefore();
						}
						// 删除后置任务
						if (intercept != null) {
							String msg = null;
							if (isDel) {
								msg = intercept.deleteAfter(ac);
							} else {
								msg = intercept.hideAfter(ac);
							}
							if (!xx.isEmpty(msg)) {
								errorInfo = msg;
								return false;
							}
						}
					}
				} catch (Exception e) {
					errorInfo = TemplateUtil.buildException(e);
					return false;
				}
				return true;
			}
		});

		// 删除成功之后
		if (intercept != null) {
			try {
				AopContext ac = new AopContext(this, records);
				ac.object = object;

				String msg = null;
				if (isDel) {
					msg = intercept.deleteSucceed(ac);
				} else {
					msg = intercept.hideSucceed(ac);
				}
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

	/**
	 * 更新
	 * @throws Exception 
	 */
	public void update() throws Exception {

		String objectCode = getPara(0);
		final MetaObject object = sm.meta.getMeta(objectCode);

		String json = getPara("rows");

		final List<Record> records = getRecordsByJson(json, object.getFields(), object.getPk());

		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());
		// 事务
		Db.use(object.getDs()).tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					for (Record record : records) {

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
						if (xx.isEmpty(object.getTable())) {
							throw new Exception("视图暂时不支持Grid单元格编辑，请使用Form模式！");
						}
						Db.use(object.getDs()).update(object.getTable(), object.getPk(), record);
						// 修改后置任务
						if (intercept != null) {
							String msg = intercept.updateAfter(ac);
							if (!xx.isEmpty(msg)) {
								errorInfo = msg;
								return false;
							}
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
		if (intercept != null) {
			try {
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

		if (!xx.isEmpty(errorInfo)) {
			renderJson(Easy.fail(errorInfo));
			return;
		}

		renderJson(new Easy());
	}

	/**
	 * 更新Grid列宽度
	 * 
	 * @throws Exception
	 */
	public void updateWidths() throws Exception {
		String objectCode = getPara(0);

		if (objectCode == null) {
			renderJson(Easy.fail("元对象缺失,当前组件无法调整列宽!"));
			return;
		}

		String widths = getPara(1);

		String[] ss = widths.split(",");

		int i = 0;
		List<MetaField> fields = MetaField.dao.queryShowFieldByObjectCode(objectCode);
		for (MetaField x : fields) {
			x.set("width", xx.toInt(ss[i]));
			x.update();
			i++;
		}

		renderJson(Easy.sucess());
	}

	/**
	 * json转List
	 * 
	 * @param json
	 * @param pkName
	 * @return
	 */
	private static List<Record> getRecordsByJson(String json, List<MetaField> items, String pkName) {
		List<Record> records = new ArrayList<Record>();

		List<JSONObject> list = JSON.parseArray(json, JSONObject.class);
		for (JSONObject o : list) {
			Map<String, Object> map = JSON.parseObject(o + "", new TypeReference<Map<String, Object>>() {
			});
			Record re = new Record();
			re.setColumns(map);
			// 将Text翻译成Value,然后删除val字段
			for (MetaField x : items) {
				String en = x.getEn();// 字段名
				String exp = x.getStr("exp");// 表达式
				Object value = re.get(en);// 值

				if (!xx.isEmpty(exp)) {
					String valField = en + "_val";
					// 获取值列中的值
					value = re.get(valField);
					// 获得值之后删除值列防止持久化报错
					re.remove(valField);
				}

				re.set(en, EovaConfig.convertor.convert(x, value));
			}
			// 删除主键备份值列
			re.remove("pk_val");
			// 删除Orcle分页产生的rownum_
			if (xx.isOracle()) {
				re.remove("rownum_");
			}
			records.add(re);
		}

		return records;
	}

	/**
	 * 单元格编辑
	 * @throws Exception 
	 */
	
	public void updateCell() throws Exception {
		String code = getPara("code");
		String pk = getPara("pk");
		String field = getPara("field");
		String val = getPara("val");

		final MetaObject object = sm.meta.getMeta(code);

		Record cell = new Record();
		cell.set("pk", pk);
		cell.set("field", field);
		cell.set("value", val);

		// 默认校验:非字符串不能为空值
		errorInfo = strNotEmpty(field, val, object);

		intercept = TemplateUtil.initMetaObjectIntercept(object.getBizIntercept());
		try {
			AopContext ac = new AopContext(this, cell);
			ac.object = object;

			// 前置拦截
			if (intercept != null) {
				errorInfo = intercept.updateCellBefore(ac);
				// 重新获取值(允许拦截器内改变值)
				val = cell.getStr("value");
			}

			// 视图拦截
			if (object.isView() && intercept != null) {
				// 视图必须手工实现持久化
				errorInfo = intercept.updateCell(ac);
			} else {
				// 表直接更新
				Object pkValue = object.buildPkValue(pk);
				Object fieldValue = object.buildFieldValue(field, val);
				Db.use(object.getDs()).update(String.format("update %s set %s = ? where %s = ?", object.getTable(), field, object.getPk()), fieldValue, pkValue);
			}

			// 后置拦截
			if (intercept != null) {
				errorInfo = intercept.updateCellAfter(ac);
			}

		} catch (Exception e) {
			errorInfo = TemplateUtil.buildException(e);
		}

		if (!xx.isEmpty(errorInfo)) {
			renderJson(Easy.fail(errorInfo));
			return;
		}

		renderJson(Easy.sucess());
	}

	private String strNotEmpty(String field, String val, final MetaObject object) {
		if (xx.isEmpty(val)) {
			List<MetaField> fields = object.getFields();
			for (MetaField x : fields) {
				if (x.getEn().equals(field)) {
					Class type = EovaConfig.convertor.getJavaType(x);
					if (type != String.class) {
						return "非字符类型, 值不能为空";
					}
				}
			}
		}
		return null;
	}

}