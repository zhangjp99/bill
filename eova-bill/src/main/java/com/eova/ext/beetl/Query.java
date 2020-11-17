/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.ext.beetl;

import java.util.ArrayList;
import java.util.List;

import org.beetl.core.Context;
import org.beetl.core.Function;

import com.eova.common.utils.xx;
import com.eova.model.MetaField;
import com.eova.model.User;
import com.eova.model.Widget;
import com.eova.service.LoginService;
import com.eova.service.sm;

/**
 * 模版数据查询函数
 * 
 * @author Jieven
 * @date 2014-5-23
 */
public class Query implements Function {
	@Override
	public Object call(Object[] paras, Context ctx) {
		String type = paras[0].toString();
		if (type.equals("fields")) {
			// 元字段查询
			Object objectCode = paras[1];
			if (xx.isEmpty(objectCode)) {
				throw new RuntimeException("参数异常，请输入合法参数");
			}

			// 获取当前用户所有可用字段
			User user = (User) ctx.getGlobal(LoginService.USER);
			List<MetaField> fields = MetaField.dao.queryFieldsGroup(objectCode.toString(), user);

			// 按白名单顺序选取字段
			Object showFields = paras[2];
			if (!xx.isEmpty(showFields)) {
				List<MetaField> temp = new ArrayList<>();
				String[] ss = showFields.toString().split(",");
				for (String s : ss) {
					for (MetaField f : fields) {
						if (s.equals(f.getEn())) {
							temp.add(f);
							break;
						}
					}
				}
				return temp;
			}

			return fields;
		}
		// 查询自定义表单字段
		else if (type.equals("form_field")) {
			Object code = paras[1];
			if (xx.isEmpty(code)) {
				throw new RuntimeException("参数异常，请输入表单编码");
			}
			return sm.form.getFormField(code.toString());
		}
		// 自定义插件查询
		else if (type.equals("widget")) {
			return Widget.dao.findByType(Widget.TYPE_DIY);
		}
		return null;
	}
}