/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.template;

import java.util.List;
import java.util.Map;

import com.eova.model.Button;

/**
 * Eova 业务模版接口
 * 
 * @author Jieven
 * 
 */
public interface Template {
	/**
	 * 模版名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * 模版编码
	 * 
	 * @return
	 */
	String code();

	/**
	 * 模版按钮组
	 * @return
	 */
	Map<Integer, List<Button>> getBtnMap();

}