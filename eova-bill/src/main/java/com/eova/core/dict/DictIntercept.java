/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.dict;

import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;
import com.eova.common.utils.xx;
import com.eova.model.MetaObject;

public class DictIntercept extends MetaObjectIntercept {

	@Override
	public void queryBefore(AopContext ac) throws Exception {
		String objectCode = ac.ctrl.getPara("query_v_object_code");
		if (!xx.isEmpty(objectCode)) {
			MetaObject o = MetaObject.dao.getByCode(objectCode);
			ac.condition = " and object = ?";
			ac.params.add(o.getTable());
		}

		super.queryBefore(ac);
	}

	
}