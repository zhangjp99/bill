/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.model;

import com.eova.common.base.BaseModel;

/**
 * session 存放在数据库中，并引入 cache 中间层，优点如下：
 * 1：简单且高性能
 * 2：支持分布式与集群
 * 3：支持服务器断电和重启
 * 4：支持 tomcat、jetty 等运行容器重启
 */
public class Session extends BaseModel<Session> {

	private static final long serialVersionUID = 1L;
	public static final Session dao = new Session().dao();

	public Session() {
	}

	public Session(String sid, Object uid, long expire) {
		this.set("id", sid);
		this.set("user_id", uid);
		this.set("expire", expire);
	}

	/**
	 * 会话是否已过期
	 */
	public boolean isExpired() {
		return getLong("expire") < System.currentTimeMillis();
	}

	public Object getUid() {
		return get("user_id");
	}

	public String getSid() {
		return getStr("sid");
	}

}