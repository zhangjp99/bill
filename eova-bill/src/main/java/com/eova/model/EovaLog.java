/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.model;

import com.eova.common.base.BaseController;
import com.eova.common.base.BaseModel;
import com.eova.common.utils.web.RequestUtil;
import com.jfinal.core.Controller;

/**
 * 系统操作日志
 *
 * @author Jieven
 * @date 2014-9-10
 */
public class EovaLog extends BaseModel<EovaLog> {

	private static final long serialVersionUID = -1592533967096109392L;

	public static final EovaLog dao = new EovaLog().dao();

	/** 新增 **/
	public static final int ADD = 1;
	/** 修改 **/
	public static final int UPDATE = 2;
	/** 删除 **/
	public static final int DELETE = 3;
	/** 导入 **/
	public static final int IMPORT = 4;
	/** 隐藏 **/
	public static final int HIDE = 5;
	
	/**
	 * 操作日志
	 * @param con
	 * @param info 日志详情
	 */
	public void info(Controller ctrl, int type, String info) {
		EovaLog el = new EovaLog();
		// TYPE
		el.set("type", type);
		// UID
		User user = ((BaseController) ctrl).getUser();
		el.set("user_id", user.get("id"));
		// IP
		String ip = RequestUtil.getIp(ctrl.getRequest());
		el.set("ip", ip);
		el.set("info", info);
		el.save();
	}
}