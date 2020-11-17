/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.mod;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.eova.config.EovaConfig;
import com.eova.model.Mod;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableBuilder;

/**
 * Eova Mod 插件
 * 1.获取本地Mod列表
 * 2.加载Mod Jar
 * 3.加载Mod Model(必须需等待ActiveRecordPlugin start之后)
 * 
 * @author Jieven
 *
 */
public class EovaModPlugin implements IPlugin {

	private static List<EovaModConfig> modules = new ArrayList<>();
	private static List<String> routes = new ArrayList<>();


	@Override
	public boolean start() {
		List<Mod> mods = Mod.dao.findByEnabled();
		for (Mod mod : mods) {
			try {
				String groupCode = mod.getStr("group_code");
				String code = mod.getStr("code");
				String cs = String.format("com.eova.mod.%s.%s.ModConfig", groupCode, code);
				Class clazz = EovaConfig.modLoader.loadClass(cs);
				EovaModConfig mc = (EovaModConfig) clazz.newInstance();
				if (mc != null) {
					// 初始化插件
					modules.add(mc);
					LogKit.info(String.format("Start eova mod [%s] succeed", mod));

					// 注册Model
					HashMap<String, List<Table>> mapping = new HashMap<>();
					mc.configModel(mapping);
					// Add JFinal 4.9
					mapping.forEach((k, v) -> {
						new TableBuilder().build(v, DbKit.getConfig(k));
					});
					// TableMapping.me().putTable(new Table("eova_flow", modelClass));
					// CPI.addModelToConfigMapping(modelClass, config);

					// EovaDataSource.mod(); 需要JFinal 4.9 支持
				}
			} catch (Exception e) {
				LogKit.error(String.format("Start eova mod model error:%s", e.getMessage()));
			}
			/*
			 * 必须关闭，在Windows下卸载插件的时候删除jar包
			 * 否则一旦被ClassLoader load之后，无法被删除
			 * 提前关了, 后续还有路由, 拦截器等需要用到, NoClassDefFoundError
			 * 由卸载的地方进行关闭 xx.close(loader);
			 */
		}

		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

	public static List<EovaModConfig> getModules() {
		return modules;
	}

	public static List<String> getRoutes() {
		return routes;
	}

	public static void addRoute(String route) {
		EovaModPlugin.routes.add(route);
	}

	/**
	 * 注册路由规则:/组织名/路由名
	 * @param module
	 * @return
	 */
	public static EovaModRoutes moduleRoutes(EovaModConfig module) {
		// 子应用路由
		EovaModRoutes routes = new EovaModRoutes();
		// 个人或组织名
		String groupKey = module.GROUP();
		// mod在安装时会解压到/_mod 方便读取静态资源文件(开发时和运行时保持一致)
		routes.setBaseViewPath("/_mod/" + groupKey);
	
		EovaModRoute appRoute = new EovaModRoute();
	
		module.configRoute(appRoute);
		// 加载路由(路由Key强制加上用户名)
		for (String key : appRoute.getRouteMap().keySet()) {
			Class<? extends Controller> cs = appRoute.getRouteMap().get(key);
			String ctrlKey = String.format("/%s%s", groupKey, key);
			routes.add(ctrlKey, cs, key);
			// 登记Mod Ctrl key
			addRoute(ctrlKey);
		}
		// 加载路由拦截器
		for (Interceptor i : appRoute.getInterceptors()) {
			routes.addInterceptor(i);
		}
		return routes;
	}

}