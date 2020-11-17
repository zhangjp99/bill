/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.aop.impl;

import java.util.Map;

import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;
import com.eova.engine.DynamicParse;
import com.jfinal.kit.Kv;

/**
 * 通用SQL查询解析
 * <pre>
 * eova_object.view_sql 中配置SQL
 * </pre>
 * @author Jieven
 *
 */
public class SqlQueryIntercept extends MetaObjectIntercept {

	@Override
	public void queryBefore(AopContext ac) throws Exception {
		
		String sql = ac.object.getStr("view_sql");

		// 语法
		// FROM xxx where vsi.sid = ${sid} and '${start_v_day} 00:00:00' <= update_time and update_time < '${end_v_day} 23:59:59'");

		// 循环撸参
		Map<String, String[]> paraMap = ac.ctrl.getParaMap();
		Kv kv = new Kv();
		paraMap.forEach((key, value) -> {
			kv.set(key, value[0]);
		});

		// 解析SQL
		ac.sql = DynamicParse.buildSql(sql, kv);
	}


}