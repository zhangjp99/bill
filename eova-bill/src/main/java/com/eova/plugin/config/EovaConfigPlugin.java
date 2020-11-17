/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.plugin.config;

import java.util.List;

import com.eova.common.utils.xx;
import com.eova.config.EovaConfig;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 加载EOVA数据库配置
 * @author Jieven
 *
 */
public class EovaConfigPlugin implements IPlugin {

	@Override
	public boolean start() {
		try {
			List<Record> list = Db.use(xx.DS_EOVA).find("select code, value, test from eova_config where status = 1");
			LogKit.info("load enable eova config:" + list.size());
			// 优先读取测试值, 否则取默认值
			list.forEach(x -> EovaConfig.addConfig(x.getStr("code"), x.get("test", x.getStr("value"))));
		} catch (Exception e) {
			LogKit.warn("读取EovaConfig异常:" + e.getMessage());
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}