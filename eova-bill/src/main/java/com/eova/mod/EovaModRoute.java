/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.Controller;

public class EovaModRoute {

	private HashMap<String, Class<? extends Controller>> routeMap = new HashMap<>();
	private List<Interceptor> interceptors = new ArrayList<>();

	/**
	 * 添加路由
	 * @param controllerKey
	 * @param controllerClass
	 */
	public void add(String controllerKey, Class<? extends Controller> controllerClass) {
		routeMap.put(controllerKey, controllerClass);
	}

	/**
	 * 添加拦截器
	 * @param interceptor
	 */
	public void addInterceptor(Interceptor interceptor) {
		interceptors.add(interceptor);
	}

	public HashMap<String, Class<? extends Controller>> getRouteMap() {
		return routeMap;
	}

	public List<Interceptor> getInterceptors() {
		return interceptors;
	}



}