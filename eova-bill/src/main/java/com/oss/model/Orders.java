/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.oss.model;

import com.eova.common.base.BaseModel;
import com.jfinal.plugin.activerecord.Db;

/**
 * 订单
 * @author Jieven
 *
 */
public class Orders extends BaseModel<Orders> {

	private static final long serialVersionUID = 1064291771401662738L;

	public static final Orders dao = new Orders();
	
	public int updateState(int id, int state) {
		return Db.update("update orders set state = ? where id = ?", state, id);
	}
}