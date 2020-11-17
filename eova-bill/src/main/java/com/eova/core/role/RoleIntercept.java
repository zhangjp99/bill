/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.role;

import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;

public class RoleIntercept extends MetaObjectIntercept {

	@Override
	public String addBefore(AopContext ac) throws Exception {
		Integer lv = ac.record.getInt("lv");
		Integer roleLv = ac.user.getRole().getInt("lv");
		if (lv <= roleLv) {
			return error("权限级别必须大于：" + roleLv);
		}
		return null;
	}

	@Override
	public String updateBefore(AopContext ac) throws Exception {
		return addBefore(ac);
	}

	

}