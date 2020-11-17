/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.oss.player;

import com.eova.aop.AopContext;
import com.eova.template.single.SingleIntercept;
import com.jfinal.plugin.activerecord.Record;

public class PlayerIntercept extends SingleIntercept {

	@Override
	public void importBefore(AopContext ac) throws Exception {
		for (Record record : ac.records) {
			if (record.getStr("status").equals("3")) {
				throw new Exception("数据状态异常");
			}
		}
	}

}