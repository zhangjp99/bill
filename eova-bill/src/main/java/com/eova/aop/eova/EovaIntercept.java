/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.aop.eova;

import java.util.List;

import com.eova.model.MetaField;
import com.eova.model.MetaObject;

/**
 * <pre>
 * 全局Eova业务拦截器
 * </pre>
 * @author Jieven
 *
 */
public class EovaIntercept {

	/**
	 * <pre>
	 * 过滤查询数据
	 * 
	 * ac.ctrl 当前控制器
	 * ac.user 当前用户
	 * ac.menu 当前菜单
	 * ac.object 当前元对象
	 * ec.object.getFields() 当前元字段
	 * </pre>
	 * @param ac
	 */
	public String filterQuery(EovaContext ec) throws Exception {
		return "";
	}

	/**
	 * <pre>
	 * 过滤表达式数据
	 * 
	 * ac.ctrl 当前控制器
	 * ac.user 当前用户
	 * ac.exp 当前表达式
	 * </pre>
	 * @param ac
	 */
	public String filterExp(EovaContext ec) throws Exception {
		return "";
	}

	/**
	 * 是否存在此字段
	 *
	 * @param object
	 * @param fieldName
	 * @return
	 */
	protected boolean findField(MetaObject object, String fieldName) {
		List<MetaField> fields = object.getFields();
		for (MetaField f : fields) {
			if (f.getEn().equals(fieldName)) {
				return true;
			}
		}
		return false;
	}
}