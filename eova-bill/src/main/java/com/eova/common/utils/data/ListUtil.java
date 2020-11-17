/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.utils.data;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

	/**
	 * List&lt;Object> 转常用数值类型
	 * @param list
	 * @param cs
	 */
	public static <T> List<T> toNumber(List<Object> list, Class<? extends Number> cs) {
		if (list.isEmpty()) {
			return null;
		}
		List<Object> t = new ArrayList<>();
		for (Object o : list) {
			if (o == null)
				continue;
			String s = o.toString();
			if (cs == Integer.class) {
				t.add(Integer.valueOf(s));
			} else if (cs == Long.class) {
				t.add(Long.valueOf(s));
			} else if (cs == Float.class) {
				t.add(Float.valueOf(s));
			} else if (cs == Double.class) {
				t.add(Double.valueOf(s));
			}
		}
		return (List<T>) t;
	}

}