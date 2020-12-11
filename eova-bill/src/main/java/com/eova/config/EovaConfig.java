/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.config;

import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.beetl.core.GroupTemplate;

import com.alibaba.fastjson.parser.ParserConfig;
import com.eova.aop.MetaObjectIntercept;
import com.eova.aop.UploadIntercept;
import com.eova.aop.UserSessionIntercept;
import com.eova.aop.eova.EovaIntercept;
import com.eova.common.utils.xx;
import com.eova.common.utils.web.RequestUtil;
import com.eova.copyright.CopyrightController;
import com.eova.copyright.EovaAuth;
import com.eova.core.IndexController;
import com.eova.core.admin.AdminController;
import com.eova.core.auth.AuthController;
import com.eova.core.button.ButtonController;
import com.eova.core.dict.DictController;
import com.eova.core.menu.MenuController;
import com.eova.core.meta.MetaController;
import com.eova.core.task.TaskController;
import com.eova.core.type.Convertor;
import com.eova.core.type.MysqlConvertor;
import com.eova.ext.beetl.BeetlEovaRenderFactory;
import com.eova.ext.jfinal.DbCaptchaCache;
import com.eova.handler.UrlBanHandler;
import com.eova.interceptor.AuthInterceptor;
import com.eova.interceptor.LoginInterceptor;
import com.eova.mod.EovaModConfig;
import com.eova.mod.EovaModPlugin;
import com.eova.mod.EovaModUtil;
import com.eova.model.Button;
import com.eova.model.Menu;
import com.eova.model.MetaField;
import com.eova.model.MetaObject;
import com.eova.model.Mod;
import com.eova.model.Role;
import com.eova.model.RoleBtn;
import com.eova.model.Session;
import com.eova.model.Task;
import com.eova.model.User;
import com.eova.model.Widget;
import com.eova.plugin.config.EovaConfigPlugin;
import com.eova.plugin.quartz.QuartzPlugin;
import com.eova.service.LoginService;
import com.eova.service.ServiceManager;
import com.eova.service.sm;
import com.eova.sql.dql.dialect.QueryDialect;
import com.eova.template.common.config.TemplateConfig;
import com.eova.template.masterslave.MasterSlaveController;
import com.eova.template.office.OfficeController;
import com.eova.template.single.SingleController;
import com.eova.template.singlechart.SingleChartController;
import com.eova.template.singletree.SingleTreeController;
import com.eova.template.treetogrid.TreeToGridController;
import com.eova.user.UserController;
import com.eova.widget.WidgetController;
import com.eova.widget.form.FormController;
import com.eova.widget.grid.GridController;
import com.eova.widget.tree.TreeController;
import com.eova.widget.treegrid.TreeGridController;
import com.eova.widget.upload.UploadController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.config.Routes.Route;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;
import com.oss.Interceptor.KuayuInterceptor;

public class EovaConfig extends JFinalConfig {

	public static boolean isDevMode = true;

	// 应用信息
	public static String APP_ID = "";
	public static String APP_SECRET = "";
	public static String EOVA_INDEX = "/";
	public static String EOVA_INDEX_H5 = "/h5";

	/**Eova Mod加载器**/
	public static URLClassLoader modLoader = null;

	/** EOVA所在数据库的类型 **/
	public static String EOVA_DBTYPE = "mysql";
	/** 数据库命名规则-是否自动小写 **/
	public static boolean isLowerCase = true;
	/** 数据类型转换器 **/
	public static Convertor convertor = new MysqlConvertor();
	/** DQL方言 **/
	static HashMap<String, QueryDialect> queryDialectMap = new HashMap<>();

	/** Eova配置属性 **/
	protected static Map<String, String> props = new HashMap<String, String>();
	/** Eova表达式集合 **/
	protected static Map<String, String> exps = new HashMap<String, String>();
	/** URI授权集合<角色ID,URI> **/
	protected static Map<Integer, Set<String>> authUris = new HashMap<Integer, Set<String>>();
	/** 字段角色授权<字段,角色集合> **/
	protected static Map<String, Set<String>> authFields = new HashMap<String, Set<String>>();
	/** ActiveRecord Map **/
	static HashMap<String, ActiveRecordPlugin> arps = new HashMap<>();

	/**全局查询拦截器**/
	private static EovaIntercept eovaIntercept = null;
	/**默认元对象业务拦截器**/
	private static MetaObjectIntercept defaultMetaObjectIntercept = null;
	/**用户会话处理拦截器**/
	private static UserSessionIntercept userSessionIntercept = null;
	/**上传拦截器**/
	private static UploadIntercept uploadIntercept = null;

	private GroupTemplate gt = null;

	/**
	 * 系统启动之后
	 */
	@Override
	public void onStart() {
		System.out.println(String.format("Starting Eova %s -> The most easy development platform", EovaConst.VERSION));

		// 初始化配置Eova业务
		configEova();

		// 初始化ServiceManager
		ServiceManager.init();

		// 回调Eova Mod Start
		try {
			for (EovaModConfig mc : EovaModPlugin.getModules()) {
				if (mc != null) {
					mc.afterEovaStart();
				}
			}
		} catch (Exception e) {
			LogKit.error(String.format("eova mod start error:%s", e.getMessage()));
		}

	}

	/**
	 * 系统停止之前
	 */
	@Override
	public void onStop() {
		try {
			for (EovaModConfig mc : EovaModPlugin.getModules()) {
				if (mc != null) {
					mc.beforeEovaStop();
				}
			}
		} catch (Exception e) {
			LogKit.error(String.format("eova mod stop error:%s", e.getMessage()));
		}
	}

	/**
	 * 	配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		System.err.println("Config Constants Starting...");
		me.setEncoding("UTF-8");

		// 加载本地配置文件
		EovaInit.loadLocalConfig(props);

		// 初始化Mod
		modLoader = EovaModUtil.initLoader();

		// 开发模式
		isDevMode = xx.getConfigBool("devMode", true);
		me.setDevMode(isDevMode);
		if (isDevMode && "PRD".equals(xx.getConfig("env"))) {
			LogKit.warn("当前环境为生产环境, 并且开启了开发者模式, 如无必要请立即关闭, 避免对线上造成不可逆的后果!");
			LogKit.info("当前环境为生产环境, 并且开启了开发者模式, 如无必要请立即关闭, 避免对线上造成不可逆的后果!");
			LogKit.info("当前环境为生产环境, 并且开启了开发者模式, 如无必要请立即关闭, 避免对线上造成不可逆的后果!");
		}

		// POST内容最大500M(安装包上传)
		me.setMaxPostSize(1024 * 1024 * 500);
		me.setError500View("/eova/500.html");
		me.setError404View("/eova/404.html");
		me.setBaseUploadPath(xx.getConfig("static_root"));
		me.setJsonFactory(MixedJsonFactory.me());
		me.setJsonDatePattern("yyyy-MM-dd");

		// 关闭autoType
		ParserConfig.getGlobalInstance().setSafeMode(true);
		// 注册分布式验证码
		me.setCaptchaCache(new DbCaptchaCache());
		// 插件顺序调整到configConstant()之后
		me.setConfigPluginOrder(1);

		// 设置主视图为Beetl
		boolean webappMode = xx.getConfigBool("webappMode", false);
		BeetlEovaRenderFactory rf = new BeetlEovaRenderFactory();
		rf.config(webappMode);
		me.setRenderFactory(rf);
		gt = rf.groupTemplate;

		// 初始化配置
		exp();// Eova表达式
		authUri();// URI授权
		authField();// 字段角色授权
		license();// 加载授权信息

	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		System.err.println("Config Routes Starting...");
		// 父类方法自动注册为路由
		me.setMappingSuperClass(true);

		// Eova业务
		me.add("/eova/admin", AdminController.class);
		me.add("/copyright", CopyrightController.class);

		// 业务模版
		me.add("/" + TemplateConfig.SINGLE_GRID, SingleController.class);
		me.add("/" + TemplateConfig.SINGLE_TREE, SingleTreeController.class);
		me.add("/" + TemplateConfig.SINGLE_CHART, SingleChartController.class);
		me.add("/" + TemplateConfig.MASTER_SLAVE_GRID, MasterSlaveController.class);
		me.add("/" + TemplateConfig.TREE_GRID, TreeToGridController.class);
		me.add("/" + TemplateConfig.OFFICE, OfficeController.class);
		// 组件
		me.add("/widget", WidgetController.class);
		me.add("/upload", UploadController.class);
		me.add("/form", FormController.class);
		me.add("/grid", GridController.class);
		me.add("/tree", TreeController.class);
		me.add("/treegrid", TreeGridController.class);

		me.add("/meta", MetaController.class);
		me.add("/menu", MenuController.class);
		me.add("/button", ButtonController.class);
		me.add("/auth", AuthController.class);
		me.add("/task", TaskController.class);
		me.add("/dict", DictController.class);
		// 用户
		me.add("/user", UserController.class);

		// LoginInterceptor.excludes.add("/cloud");

		// 如果有自定义，将不再注册系统默认实现
		EOVA_INDEX = xx.getConfig("eova.index", "/");
		EOVA_INDEX_H5 = xx.getConfig("eova.index.h5", "/h5");

		// 自定义路由
		route(me);

		boolean flag = false;
		for (Route x : me.getRouteItemList()) {
			if (x.getControllerKey().equals(EOVA_INDEX)) {
				flag = true;
			}
		}
		if (!flag) {
			me.add(EOVA_INDEX, IndexController.class);
		}

		// load eova module route
		try {
			for (EovaModConfig mc : EovaModPlugin.getModules()) {
				if (mc != null) {
					me.add(EovaModPlugin.moduleRoutes(mc));
				}
			}
		} catch (Exception e) {
			LogKit.error(String.format("load eova module routes error:%s", e.getMessage()));
		}
	}

	@Override
	public void configEngine(Engine me) {
		// 暂时不需要Enjoy class:webapp
		// me.setBaseTemplatePath("webapp");
		// me.setToClassPathSourceFactory();
		// TODO Enjoy 不支持混合, 需要单独开发

		// me.addSharedFunction("/WEB-INF/_layout/pager.html");

		// 设置全局变量
		Map<String, Object> sharedVars = new HashMap<String, Object>();
		EovaConst.getPageConf().forEach((k, v) -> {
			sharedVars.put(k, props.get(v));
		});
		sharedVars.put("APP", EovaAuth.getEovaApp());

		// 格式化URI => / or /admin/
		EOVA_INDEX = EOVA_INDEX.endsWith("/") ? EOVA_INDEX : EOVA_INDEX + '/';
		// 排除首页免登录URI
		LoginInterceptor.initNoLogin(EOVA_INDEX);
		sharedVars.put("INDEX", EOVA_INDEX);

		// sharedVars.put("I18N", I18NBuilder.I18N);

		gt.setSharedVars(sharedVars);

		// Load Template Const
		PageConst.init(sharedVars);
	}

	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins plugins) {
		System.err.println("Config Plugins Starting...");

		// 初始化数据源
		EovaDataSource.create(plugins);

		// 初始化EOVA DB配置
		plugins.add(new EovaConfigPlugin());

		/*
		 * 特别说明, configPlugin{} 暂时无法使用DB和参数
		 * 如果需要使用, 必须封装为Plugin, 并且排在下面
		 */

		// 延迟添加Model映射, 可能会导致初始化数据源卡住
		// eova model mapping
		mappingEova(arps.get(xx.DS_EOVA));

		// diy model mapping
		mapping(arps);

		// 构建类型转换方言
		EovaDataSource.buildConvertor();

		// 自定义插件
		plugin(plugins);

		// 配置EhCachePlugin插件
		plugins.add(new EhCachePlugin());

		// 配置定时调度  默认不启动
		if (xx.getConfigBool("isQuartz", false)) {
			plugins.add(new QuartzPlugin());
		}

		// Eova Mod 初始化并注册Model
		plugins.add(new EovaModPlugin());

	}

	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		System.err.println("Config Interceptors Starting...");
		// JFinal.me().getServletContext().setAttribute("EOVA", "简单才是高科技");
		// 登录验证
		me.addGlobalActionInterceptor(new LoginInterceptor());
		// 权限验证拦截
		me.addGlobalActionInterceptor(new AuthInterceptor());
		//跨域拦截器
		me.addGlobalActionInterceptor(new KuayuInterceptor());
	}

	/**配置Eova业务**/
	public void configEova() {
	}

	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me) {
		System.err.println("Config Handlers Starting...");
		// 添加DruidHandler
		DruidStatViewHandler dvh = new DruidStatViewHandler("/druid", new IDruidStatViewAuth() {
			@Override
			public boolean isPermitted(HttpServletRequest request) {
				String sid = RequestUtil.getCookieStr(request, LoginService.CKSID, null);
				if (sid == null) {
					return false;
				}
				User user = sm.login.getLoginUser(sid);
				if (user == null) {
					return false;
				}
				return user.isAdmin();
			}
		});
		me.add(dvh);
		// 过滤禁止访问资源
		me.add(new UrlBanHandler(".*\\.(html|tag|sql)", false));
	}

	/**
	 * Eova Data Source Model Mapping
	 *
	 * @param arp
	 */
	private void mappingEova(ActiveRecordPlugin arp) {
		arp.addMapping("eova_session", Session.class);
		arp.addMapping("eova_object", MetaObject.class);
		arp.addMapping("eova_field", MetaField.class);
		arp.addMapping("eova_button", Button.class);
		arp.addMapping("eova_menu", Menu.class);
		arp.addMapping("eova_user", User.class);
		arp.addMapping("eova_role", Role.class);
		arp.addMapping("eova_role_btn", RoleBtn.class);
		arp.addMapping("eova_task", Task.class);
		arp.addMapping("eova_widget", Widget.class);
		arp.addMapping("eova_mod", Mod.class);
	}

	/**
	 * Diy Data Source Model Mapping
	 * @param arps 数据源key->ActiveRecordPlugin
	 */
	protected void mapping(HashMap<String, ActiveRecordPlugin> arps) {
	}

	/**
	 * Custom Route
	 *
	 * @param me
	 */
	protected void route(Routes me) {
	}

	/**
	 * Custom Plugin
	 *
	 * @param plugins
	 * @return
	 */
	protected void plugin(Plugins plugins) {
	}

	/**
	 * Eova Expression Mapping
	 */
	protected void exp() {
		// Eova 系统功能需要的Exp
		exps.put("selectEovaFieldByObjectCode", "select en Field,cn Name from eova_field where object_code = ?;ds=eova");
		exps.put("selectEovaUser", "select id ID,name 姓名, login_id 帐号 from eova_user;ds=eova");
		exps.put("selectEovaRole", "select id id,name cn from eova_role;ds=eova");
		// 隐藏玩法の软硬结合
		// exps.put("selectEovaUser", "select id ID,name 姓名, login_id 帐号 from eova_user where id in %s and id < ?;ds=eova");
		// exp=selectEovaUser;@(1,2,3);10000
	}

	/**
	 * URI授权配置
	 */
	protected void authUri() {

		// 公有授权白名单
		HashSet<String> uris = new HashSet<String>();
		uris.add("/meta/object/**");
		uris.add("/meta/fields/**");
		uris.add("/meta/dict/**");
		uris.add("/widget/**");
		uris.add("/upload/**");
		uris.add("/grid/updateCell");
		authUris.put(0, uris);

	}

	/**
	 * Field授权配置
	 */
	protected void authField() {
		// 系统角色字段授权
		addAuthField("eova_role_code.lv->1,2");// 解释:eova_role_code对象的lv字段 只有角色1和角色2 可见
	}

	/**
	 * 添加字段授权规则<br>
	 * 语法:元对象编码.元字段英文名->角色1ID,角色2ID
	 * 
	 * @param auth
	 */
	protected static void addAuthField(String rule) {
		String[] ss = rule.split("->");
		String key = ss[0];
		String b = ss[1];

		Set<String> set = authFields.get(key);
		if (set == null) {
			set = new HashSet<>();
		}
		set.addAll(Arrays.asList(b.split(",")));
		authFields.put(key, set);
	}

	/**
	 * 添加URI授权规则<br>
	 * 语法:URI->角色1ID,角色2ID
	 * 
	 * @param auth
	 */
	protected static void addAuthUri(String rule) {
		String[] ss = rule.split("->");
		String uri = ss[0];
		String s1 = ss[1];

		String[] rids = s1.split(",");
		for (String x : rids) {
			Integer rid = xx.toInt(x.trim());
			Set<String> set = authUris.get(rid);
			if (set == null) {
				set = new HashSet<>();
			}
			set.addAll(Arrays.asList(uri.split(",")));
			authUris.put(rid, set);
		}
	}

	// 默认从配置中读取授权密钥
	protected void license() {
		APP_ID = xx.getConfig("app_id").trim();
		APP_SECRET = xx.getConfig("app_secret").trim();
		// 默认从配置中读取license,为了私密也可以写在代码中,就不用向需求方解释这玩意了.三方无感!
		// 同理可以藏到任意别人找不到的地方.
	}

	public static EovaIntercept getEovaIntercept() {
		return eovaIntercept;
	}

	public static void setEovaIntercept(EovaIntercept eovaIntercept) {
		EovaConfig.eovaIntercept = eovaIntercept;
	}

	public static MetaObjectIntercept getDefaultMetaObjectIntercept() {
		return defaultMetaObjectIntercept;
	}

	public static void setDefaultMetaObjectIntercept(MetaObjectIntercept defaultMetaObjectIntercept) {
		EovaConfig.defaultMetaObjectIntercept = defaultMetaObjectIntercept;
	}

	public static UserSessionIntercept getUserSessionIntercept() {
		return userSessionIntercept;
	}

	public static void setUserSessionIntercept(UserSessionIntercept userSessionIntercept) {
		EovaConfig.userSessionIntercept = userSessionIntercept;
	}

	public static UploadIntercept getUploadIntercept() {
		return uploadIntercept;
	}

	public static void setUploadIntercept(UploadIntercept uploadIntercept) {
		EovaConfig.uploadIntercept = uploadIntercept;
	}

	public static Map<String, String> getProps() {
		return props;
	}

	public static void addConfig(String key, String value) {
		EovaConfig.props.put(key, value);
	}

	public static HashMap<String, ActiveRecordPlugin> getArps() {
		return arps;
	}

	public static void setArps(HashMap<String, ActiveRecordPlugin> arps) {
		EovaConfig.arps = arps;
	}

	public static Map<String, String> getExps() {
		return exps;
	}

	public static Map<Integer, Set<String>> getAuthUris() {
		return authUris;
	}

	public static Map<String, Set<String>> getAuthFields() {
		return authFields;
	}

	public static QueryDialect getQueryDialect(String ds) {
		return queryDialectMap.get(ds);
	}

}