/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.base;

import java.util.HashSet;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eova.common.utils.xx;
import com.eova.model.User;
import com.eova.service.LoginService;
import com.eova.service.sm;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.json.Json;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.render.TemplateRender;

public class BaseController extends Controller {

	protected String http(String domain) {
		return "http://" + xx.getConfig(domain);
	}

	@NotAction
	public int UID() {
		// 字符串ID自行获取
		return (Integer) getUser().getId();
	}

	@NotAction
	public int RID() {
		return getUser().getRid();
	}

	@NotAction
	public String SID() {
		// Request域优先于 Cookie域
		String sid = getAttr(LoginService.CKSID);
		if (sid == null) {
		    sid = getCookie(LoginService.CKSID);
		}
		return sid;
	}

	@NotAction
	public User getUser() {
		return sm.login.getLoginUser(SID());
	}

	@NotAction
	public void updateUser(User user) {
		sm.login.update(user);
	}

	/**	
	 * <pre>
	 * 获取选中行某列的值
	 * 多选且为整型:1,2,3
	 * 多选且为字符:'a','b','c' 
	 * PS:多选的处理方便取值后直接in
	 * </pre>
	 * @param field 字段名
	 */
	protected String getSelectValue(String field) {
		// 单选
		List<JSONObject> rows = getSelectRows();
		if (rows.isEmpty()) {
			return null;
		}
		if (rows.size() == 1) {
			return rows.get(0).getString(field);
		}

		boolean isNum = true;
		// 多选
		HashSet<String> set = new HashSet<>();
		for (int i = 0; i < rows.size(); i++) {
			Object val = rows.get(i).get(field);
			set.add(val.toString());
			if (!xx.isNum(val))
				isNum = false;
		}
		if (isNum) {
			return xx.join(set);
		} else {
			return xx.join(set, "'", ",");
		}
	}

	protected String getSelectValue(String field, String def) {
		String val = getSelectValue(field);
		return xx.isEmpty(val) ? def : val;
	}

	protected Integer getSelectValueToInt(String field) {
		return xx.toInt(getSelectValue(field));
	}

	/**
	 * 获取选中行
	 * 
	 * @return
	 */
	protected JSONObject getSelectRow() {
		String json = getPara("row");
		if (json == null) {
			return getSelectRows().get(0);
		}
		return JSONObject.parseObject(json);
	}

	/**
	 * 获取自定义按钮提交的所有选中行
	 * 
	 * @return
	 */
	//	protected JSONArray getSelectRows() {
	//		String json = getPara("rows");
	//		JSONArray o = JSONObject.parseArray(json);
	//		return o;
	//	}

	protected List<JSONObject> getSelectRows() {
		String json = getPara("rows");
		return JSONObject.parseArray(json, JSONObject.class);
	}

	/**
	 * 获取提交表单时的自定义数据
	 * 
	 * @return
	 */
	protected String getEovaData() {
		return getPara("eova_data");
	}

	protected JSONObject getEovaDataToJSONObject() {
		return JSON.parseObject(getEovaData());
	}

	protected JSONArray getEovaDataToJSONArray() {
		return JSON.parseArray(getEovaData());
	}


	/**
	 * 获取按钮输入框的值
	 * <pre>
	 * Integer id = getSelectValue("id");
	 * 
	 * if (xx.isEmpty(val)) {
	 *     renderJson(new Easy("参数不能为空！"));
	 *     return;
	 * }
	 * 
	 * Db.update("update xxx set xx = ? where id = ?", val, id);
	 * renderJson(Easy.sucess());
	 *</pre>
	 */
	protected String getInputValue() {
		return getPara("input");
	}

	/**
	 * 获取JSONObject
	 * 
	 */
	protected Ret getJsonToRet() {
		String s = HttpKit.readData(getRequest());
		if (xx.isEmpty(s)) {
			return null;
		}
		return Json.getJson().parse(s, Ret.class);
		// return JSONObject.parseObject(HttpKit.readData(getRequest()));
	}

	/**
	 * 快速GET Request请求参数
	 * @param modelClass
	 * @param fields
	 * @return
	 */
	protected <T extends Model> T getModel(Class<? extends Model> modelClass, String... fields) {
		Model<?> m = null;
		try {
			m = modelClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (String field : fields) {
			if (!field.contains(" ")) {
				m.set(field, getPara(field));
				continue;
			}
			String[] ss = field.split("\\s+");
			String type = ss[0];
			String col = ss[1];
			Object val = "";
			if (type.equals("int")) {
				val = getParaToInt(col);
			} else if (type.equals("long")) {
				val = getParaToLong(col);
			} else if (type.equals("double")) {
				val = xx.toDouble(getPara());
			} else if (type.equals("boolean")) {
				val = getParaToBoolean(col);
			} else if (type.equals("date")) {
				val = getParaToDate(col);
			} else {
				val = getPara(col);
			}
			m.set(col, val);
		}

		return (T) m;
	}

	@NotAction
	public void renderMsg(String msg) {
		String style = "<link rel=\"stylesheet\" href=\"/eova/plugins/eova/css/eova.render.css\">";
		renderHtml(style + "<div class='eova-msg-error'>" + msg + "</div>");
	}

	@NotAction
	public void OK() {
		renderJson(Ret.ok());
	}

	@NotAction
	public void NO(String msg) {
		renderJson(Ret.fail("msg", msg));
	}

	@NotAction
	public void renderEnjoy(String view) {
		render(new TemplateRender(view));
	}

}