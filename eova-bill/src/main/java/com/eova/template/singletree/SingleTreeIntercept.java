/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.template.singletree;

import com.jfinal.kit.Kv;

/**
 * 单表树模版业务拦截器<br>
 * 
 * @author Jieven
 * @date 2014-8-29
 */
public class SingleTreeIntercept {

	/**
	 * 拖拽完成时
	 * <pre>
	 * 参数:
	 * kv.getStr("type");// 拖拽类型:inner=成为子节点,prev=成为同级前一个节点,next=成为同级后一个节点
	 * kv.getStr("sid");// 被拖拽原始节点ID
	 * kv.getStr("tid");// 拖拽到目标节点ID
	 * </pre>
	 * @throws Exception
	 */
	public void drop(Kv kv) throws Exception {
	}

}