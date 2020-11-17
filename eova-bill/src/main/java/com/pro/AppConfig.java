/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.pro;

import java.util.HashMap;

import com.eova.common.utils.xx;
import com.eova.config.EovaConfig;
import com.eova.config.EovaSystem;
import com.eova.interceptor.LoginInterceptor;
import com.eova.model.EovaLog;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.oss.bill.BillController;
import com.oss.hotel.HotelController;
import com.oss.model.Address;
import com.oss.model.Orders;
import com.oss.model.UserInfo;
import com.oss.model.Users;
import com.oss.product.ProductController;
import com.oss.test.TestController;
import com.pro.global.MyUserSessionIntercept;

public class AppConfig extends EovaConfig {
	
	public static void main(String[] args) {
		EovaSystem.server = UndertowServer.create(AppConfig.class).addSystemClassPrefix("com.eova.config.EovaSystem");
		EovaSystem.server.addHotSwapClassPrefix("org.beetl.").addHotSwapClassPrefix("com.eova.").start();
	}

	/**
	 * 自定义路由
	 *
	 * @param me
	 */
	@Override
	protected void route(Routes me) {
		// 自定义的路由配置往这里加。。。
		me.add(EOVA_INDEX, AppController.class);
		me.add(EOVA_INDEX_H5, H5Controller.class);
		me.add("/test", TestController.class);
		me.add("/product", ProductController.class);
		me.add("/hotel", HotelController.class);
		me.add("/bill", BillController.class);

		// 排除不需要登录拦截的URI 语法同SpringMVC拦截器配置 @see com.eova.common.utils.util.AntPathMatcher
		LoginInterceptor.excludes.add("/test/**");
		LoginInterceptor.excludes.add("/bill/**");
		// LoginInterceptor.excludes.add("/xxxx/**");

	}

	/**
	 * 自定义Main数据源Model映射
	 *
	 * @param arp
	 */
	@Override
	protected void mapping(HashMap<String, ActiveRecordPlugin> arps) {
		// 获取主数据源的ARP
		ActiveRecordPlugin main = arps.get(xx.DS_MAIN);

		// 自定义业务Model映射往这里加
//		main.addMapping("user_info", UserInfo.class);
//		main.addMapping("users", Users.class);
//		main.addMapping("address", Address.class);
//		main.addMapping("orders", Orders.class);

		// main.addSqlTemplate("xxx.sql");

		// 获取其它数据源的ARP
		ActiveRecordPlugin eova = arps.get(xx.DS_EOVA);
		eova.addMapping("eova_log", EovaLog.class);//  默认日志
	}

	/**
	 * 自定义插件
	 */
	@Override
	protected void plugin(Plugins plugins) {
	}

	/**
	 * 自定义表达式(主要用于级联)
	 */
	@Override
	protected void exp() {
		super.exp();
		// 区域级联查询
		exps.put("selectAreaByLv2AndPid", "select id ID,name CN from area where lv = 2 and pid = ?");
		exps.put("selectAreaByLv3AndPid", "select id ID,name CN from area where lv = 3 and pid = ?");
		exps.put("selectEovaMenu", "select id,parent_id pid, name, iconskip from eova_menu;ds=eova");
		// 用法，级联动态在页面改变SQL和参数
		// $xxx.eovacombo({exp : 'selectAreaByLv2AndPid,aaa,10'}).reload();
		// $xxx.eovafind({exp : 'selectAreaByLv2AndPid,aaa,10'});
		// $xxx.eovatree({exp : 'selectAreaByLv2AndPid,10'}).reload();
	}

	@Override
	protected void authUri() {
		super.authUri();

		// 全角色 所有URI 白名单
		// addAuthUri("/**/**->0");

		// 管理员(ID=1),测试主管(ID=2) 订单列表  白名单
		// addAuthUri("/order/list/*->1,2");

		// URI配置语法规则咋写的?
		// @see AntPathMatcher
	}

	@Override
	public void configEova() {
		/*
		 * 自定义Eova全局拦截器
		 * 全局的查询拦截,可快速集中解决系统的查询数据权限,严谨,高效!
		 */
		// setEovaIntercept(new GlobalEovaIntercept());
		/*
		 * 默认元对象业务拦截器:未配置元对象业务拦截器会默认命中此拦截器
		 * 自定义元对象拦截器时自行考虑是否需要继承默认拦截器
		 */
		// setDefaultMetaObjectIntercept(new BaseMetaObjectIntercept());
		/*
		 * 用户会话拦截器
		 */
		setUserSessionIntercept(new MyUserSessionIntercept());
		// setUploadIntercept(new MyUploadIntercept());
	}

}