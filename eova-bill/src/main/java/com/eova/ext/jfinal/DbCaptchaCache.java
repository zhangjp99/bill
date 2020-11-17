/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.ext.jfinal;

import com.eova.common.utils.xx;
import com.eova.config.EovaConst;
import com.jfinal.captcha.Captcha;
import com.jfinal.captcha.ICaptchaCache;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * <pre>
 * 分布式验证码(eova.eova_cache)
 * 支持集群部署多实例
 * 性能优越:基于DB内存表
 * 无第三方依赖
 * 自动回收,无内存泄漏
 * </pre>
 */
public class DbCaptchaCache implements ICaptchaCache {

	private static int BIZ = 1;

	@Override
	public void put(Captcha captcha) {
		Record e = new Record();
		e.set("biz", BIZ);
		e.set("id", captcha.getKey());
		e.set("val", captcha.getValue());
		e.set("expire", captcha.getExpireAt());
		Db.use(xx.DS_EOVA).save(EovaConst.EOVA_CACHE, e);
	}

	@Override
	public Captcha get(String key) {
		// 回收过期验证码
		Db.use(xx.DS_EOVA).delete(String.format("delete from %s where biz = ? and expire < ?", EovaConst.EOVA_CACHE), BIZ, System.currentTimeMillis());
		// 取最新的有效验证码
		Record e = Db.use(xx.DS_EOVA).findFirst(String.format("select * from %s where biz = ? and id = ? order by expire desc", EovaConst.EOVA_CACHE), BIZ, key);
		if (e == null) {
			return null;
		}
		return new Captcha(e.getStr("id"), e.getStr("val"));
	}

	@Override
	public void remove(String key) {
		Db.use(xx.DS_EOVA).delete(String.format("delete from %s where id = ?", EovaConst.EOVA_CACHE), key);
	}

	@Override
	public void removeAll() {
		Db.use(xx.DS_EOVA).delete(String.format("delete from %s where biz = ?", EovaConst.EOVA_CACHE), BIZ);
	}

}