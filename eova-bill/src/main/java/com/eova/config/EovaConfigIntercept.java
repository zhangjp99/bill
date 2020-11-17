/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.config;

import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;
import com.eova.common.utils.xx;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class EovaConfigIntercept extends MetaObjectIntercept {

	@Override
	public String addSucceed(AopContext ac) throws Exception {
		reload(ac.record);
		return super.addSucceed(ac);
	}

	@Override
	public String updateSucceed(AopContext ac) throws Exception {
		reload(ac.record);
		return super.addSucceed(ac);
	}

	// 修改后更新配置缓存
	@Override
	public String updateCellAfter(AopContext ac) throws Exception {
		String pk = ac.record.getStr("pk");
		Record e = Db.use(xx.DS_EOVA).findById("eova_config", pk);
		reload(e);
		return super.updateCellAfter(ac);
	}

	public void reload(Record e) {
		String key = e.getStr("code");
		// 测试值 || 默认值
		String val = e.get("test", e.get("value"));

		LogKit.info("更新配置文件[%s=%s]", key, val);
		EovaConfig.addConfig(key, val);
		// PS:EovaConst.getPageConf()的全局常量不会更新
	}

}