/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.service;

import java.util.ArrayList;
import java.util.List;

import com.eova.common.base.BaseService;
import com.eova.common.utils.xx;
import com.eova.model.MetaField;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 动态表单服务
 * 
 * @author Jieven
 * 
 */
public class FormService extends BaseService {

	/**
	 * 获取表单字段
	 * @param formCode 表单编码
	 * @return
	 */
	public List<Record> getFormField(String formCode) {
		List<Record> list = Db.use(xx.DS_EOVA).find(
				"select fw.id widget_id,fw.type,fw.exp widget_exp,fw.validator,fw.config,ff.* from eova_form_field ff left join eova_flow_widget fw on ff.widget_type = fw.name where form_code = ? order by ff.order_num",
				formCode);
		list.forEach(e -> {

			// 表达式的特别处理(如果存在表达式,自动从原表查找!)
			String exp = e.getStr("exp");
			String widgetExp = e.getStr("widget_exp");
			// 字段表达式优先, 没有就用控件预设的
			if (xx.isEmpty(exp)) {
				exp = widgetExp;
			}
			if (!xx.isEmpty(exp)) {
				exp = exp.trim();
				if (exp.toLowerCase().startsWith("select")) {
					e.set("exp", "EOVA_FLOW_WIDGET," + e.getInt("widget_id"));
				} else {
					if (!xx.isEmpty(exp)) {
						// 按空格分割
						String[] ss = xx.splitBlank(exp);
						List<Record> arrs = new ArrayList<>();
						for (int j = 0; j < ss.length; j++) {
							// TODO 有值和传统玩法的兼容!!
							arrs.add(new Record().set("id", ss[j]).set("cn", ss[j]));
						}
						e.set("items", arrs);// 构建字段
						e.set("items_json", JsonKit.toJson(arrs));// 构建条件
					}
				}
			}

			// 配置预处理
			String config = e.getStr("config");
			if (!xx.isEmpty(config)) {
				e.set("config", MetaField.parseConfig(config));
			}
		});
		return list;
	}

	/**
	 * 更改排序
	 * 
	 * @param s 原字段
	 * @param t 目标字段
	 */
	public void updateOrderNum(String code, String sid, String tid) {
		Record e = Db.use(xx.DS_EOVA).findFirst("select * from eova_form_field where form_code = ? and id = ?", code, tid);
		Db.use(xx.DS_EOVA).update("update eova_form_field set order_num = ? where form_code = ? and id = ?", e.getInt("order_num") + 1, code, sid);
	}

	/**
	 * 获取表单数据并格式化
	 * @param c
	 * @param fields
	 * @return
	 */
	public static Record buildData(Controller c, List<Record> fields) {
		Record data = new Record();
	
		for (Record e : fields) {
			// 字段名
			String en = e.getStr("en");
			//			String cn = e.getStr("cn");
			//			String type = e.getStr("type");
			//			String wid = e.getStr("widget_id");// 控件业务ID
	
			String value = c.get(en);
	
			// 值格式化
			//			if (!xx.isEmpty(value)) {
			//				String IMG = xx.getConfig("domain_img");// 图片域名配置
			//				MetaFieldConfig config = e.get("config");// 多图框业务配置
			//				if (type.equals("多图框")) {
			//					String[] vs = value.split(",");
			//					StringBuilder sb = new StringBuilder("<ul class='eova-img-list'>");
			//					for (String v : vs) {
			//						sb.append(String.format("<li><img src='%s/%s/%s'></li>", IMG, config.getFiledir(), v));
			//					}
			//					value = sb.append("</ul>").toString();
			//				}
			//				else if (type.equals("附件框")) {
			//					String[] vs = value.split(",");
			//					StringBuilder sb = new StringBuilder("<ul class='eova-file-list'>");
			//					for (String v : vs) {
			//						sb.append(String.format("<li><a target='_blank' href='%s/%s/%s'>%s</a></li>", IMG, config.getFiledir(), v, v));
			//					}
			//					value = sb.append("</ul>").toString();
			//				}
			//			}
	
			//			Record data = new Record();
			//			e.set("type", type);
			//			e.set("widget_id", wid);
			//			e.set("en", en);
			//			e.set("cn", cn);
			//			e.set("value", value);
			data.set(en, value);
	
			//			list.add(data);
		}
	
		return data;
	}
}