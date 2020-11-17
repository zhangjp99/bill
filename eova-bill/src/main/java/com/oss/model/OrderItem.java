/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.oss.model;

import java.util.List;

import com.eova.common.base.BaseModel;

/**
 * 订单项
 * @author Jieven
 *
 */
public class OrderItem extends BaseModel<OrderItem> {

	private static final long serialVersionUID = 1064291771401662738L;

	public static final OrderItem dao = new OrderItem();
	
	public List<OrderItem> findOrderItemByOrderId(int orderId){
		return this.find("select * from order_item where order_id = ?", orderId);
	}
}