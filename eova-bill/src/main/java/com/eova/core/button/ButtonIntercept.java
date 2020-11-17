/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.button;

import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;
import com.eova.model.RoleBtn;

public class ButtonIntercept extends MetaObjectIntercept {

	@Override
	public String deleteBefore(AopContext ac) throws Exception {
		int id = ac.record.getInt("id");

		// 删除菜单按钮关联权限
		RoleBtn.dao.deleteByBid(id);

		return null;
	}

}