/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.eova.common.Easy;
import com.eova.common.base.BaseCache;
import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.common.utils.db.DbUtil;
import com.eova.config.EovaConfig;
import com.eova.config.EovaConst;
import com.eova.config.EovaDataSource;
import com.eova.core.button.ButtonFactory;
import com.eova.core.menu.config.ChartConfig;
import com.eova.core.menu.config.MenuConfig;
import com.eova.core.menu.config.TreeConfig;
import com.eova.core.meta.MetaUtil;
import com.eova.model.Button;
import com.eova.model.Menu;
import com.eova.model.MetaField;
import com.eova.model.MetaObject;
import com.eova.model.Role;
import com.eova.model.RoleBtn;
import com.eova.template.common.config.TemplateConfig;
import com.jfinal.aop.Before;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.NestedTransactionHelpException;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;

/**
 * 菜单管理
 *
 * @author Jieven
 * @date 2014-9-11
 */
public class MenuController extends BaseController {

	public void icon() {
		render("/eova/icon.html");
	}

	public void toAdd() {
		Integer pid = getParaToInt("parent_id");
		// pid == 0 是根节点
		if (pid != 0) {
			// 判断父节点是否为目录
			Menu menu = Menu.dao.findById(pid);
			if (!menu.getStr("type").equals(Menu.TYPE_DIR)) {
				renderHtml("您选择的节点类型不是目录,无法添加子菜单,请选择其他目录节点进行添加");
				return;
			}
		}
		keepPara("parent_id");

		setAttr("dataSources", EovaDataSource.map());

		render("/eova/menu/add.html");
	}

	public void toUpdate() {
		int pkValue = getParaToInt(1);
		Menu menu = Menu.dao.findById(pkValue);

		setAttr("menu", menu);

		render("/eova/menu/add.html");
	}

	/**
	 * 菜单基本功能管理
	 */
	public void toMenuFun() {
		int id = getParaToInt(0);
		Menu menu = Menu.dao.findById(id);

		setAttr("menu", menu);

		HashMap<Integer, List<Button>> btnMap = new HashMap<Integer, List<Button>>();

		List<Button> btns = Button.dao.findNoQueryByMenuCode(menu.getStr("code"));
		for (Button b : btns) {
			int group = b.getInt("group_num");
			List<Button> list = btnMap.get(group);
			if (list == null) {
				list = new ArrayList<Button>();
				btnMap.put(group, list);
			}
			list.add(b);
		}

		setAttr("btnMap", btnMap);

		render("/eova/menu/menuFun.html");
	}

	// 一键导入
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void addAll() {
		if (!getPara(0, "").equals("eova")) {
			renderJson(new Easy("请输入校验码，防止误操作！！！！！"));
			return;
		}

		List<MetaObject> objects = MetaObject.dao.find("select * from eova_object where id >= 1100");
		for (MetaObject o : objects) {

			String menuCode = o.getStr("code");
			System.out.println("create " + menuCode);
			Menu menu = new Menu();
			menu.set("parent_id", 3);
			menu.set("name", o.getStr("name"));
			menu.set("code", menuCode);
			menu.set("type", TemplateConfig.SINGLE_GRID);

			// 菜单配置
			MenuConfig config = new MenuConfig();
			config.setObjectCode(o.getStr("code"));
			menu.setConfig(config);
			menu.save();

//			createMenuButton(menuCode, TemplateConfig.SINGLE_GRID, config);
			// 创建菜单按钮
			new ButtonFactory(TemplateConfig.SINGLE_GRID).build(menuCode, config);

			// 还原成默认状态
			o.set("diy_card", null);
			o.update();
		}
		// 新增菜单使缓存失效
		BaseCache.delSer(EovaConst.ALL_MENU);
		
		renderJson(new Easy("Auto Create Menu:" + objects.size(), true));
	}

	// 导出选中菜单数据
	public void doExport() {
		String ids = getPara(0);

		StringBuilder sb = new StringBuilder();

		String sql = "select * from eova_menu where id in (" + ids + ")";
		List<Record> list = Db.use(xx.DS_EOVA).find(sql);
		// 删除语句
		list.forEach(x -> {
			String code = x.getStr("code");
			sb.append(String.format("delete from eova_menu where code = '%s';\n", code));
			sb.append(String.format("delete from eova_button where menu_code = '%s';\n", code));
			sb.append("\n");
		});
		sb.append("\n");
		DbUtil.generateSql(xx.DS_EOVA, "eova_menu", list, "id", sb);
		sb.append("\n");
		for (Record r : list) {
			List<Record> btns = Db.use(xx.DS_EOVA).find("select * from eova_button where menu_code = ?", r.getStr("code"));
			DbUtil.generateSql(xx.DS_EOVA, "eova_button", btns, "id", sb);
			sb.append("\n");
		}

		renderText(sb.toString());
	}

	/**
	 * 新增菜单
	 */
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void add() {

		String menuCode = getPara("code");
		String type = getPara("type");

		Menu temp = Menu.dao.findFirst("select * from eova_menu where code = ?", menuCode);
		if (temp != null) {
			renderJson(new Easy("菜单编码不能重复"));
			return;
		}
		String icon = getPara("iconField");
		if (!xx.isEmpty(icon) && icon.equalsIgnoreCase("icon")) {
			renderJson(new Easy("Tree图标字段:字段名不能为icon(系统关键字，你可以改为：iconskip)"));
			return;
		}
		icon = getPara("treeGridIconField");
		if (!xx.isEmpty(icon) && icon.equalsIgnoreCase("icon")) {
			renderJson(new Easy("Tree图标字段:字段名不能为icon(系统关键字，你可以改为：iconskip)"));
			return;
		}
		
		try {
			
			String name = get("name");
			Menu menu = new Menu();
			menu.set("parent_id", getParaToInt("parent_id"));
			menu.set("iconskip", getPara("icon", ""));
			menu.set("name", name);
			menu.set("code", menuCode);
			menu.set("order_num", getParaToInt("indexNum"));
			if (type.equals(TemplateConfig.QUERY)) {
				// 自定义查询复用单表模版(除了自定义SQL, 其它都一样)
				menu.set("type", TemplateConfig.SINGLE_GRID);
				// 默认指定通用SQL查询
				menu.set("biz_intercept", "com.eova.aop.impl.SqlQueryIntercept");
				String sql = get("sql");
				String ds = get("ds");
				// 通过SQL生成虚拟元对象和元字段 入库
				MetaUtil.addVirtualObject(sql, menuCode, name, ds);
			} else {
				menu.set("type", type);
			}
			// menu.set("biz_intercept", getPara("bizIntercept", ""));

			String url = getPara("url", "");// 自定义业务URL

			// 自定义office模版
			String path = getPara("path", "");
			menu.set("url", type.equals(TemplateConfig.DIY) ? url : path);

			// 菜单配置
			MenuConfig config = new MenuConfig();
			// 构建菜单配置项
			buildConfig(type, config);
			menu.setConfig(config);
			menu.save();

			// 目录没有默认按钮
			if (type.equals(Menu.TYPE_DIR)) {
				renderJson(new Easy());
				return;
			}

			// 创建菜单按钮
			new ButtonFactory(type).build(menuCode, config);
			
			// 新增菜单使缓存失效
			BaseCache.delSer(EovaConst.ALL_MENU);
			
			// EovaCloud.app();
			
			renderJson(new Easy());
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(new Easy("新增菜单失败,请仔细查看控制台日志！"));
			throw new NestedTransactionHelpException("新增菜单异常");
		}
		
	}

	/**
	 * 配置菜单
	 *
	 * @param type 模版类型
	 * @param config
	 */
	private void buildConfig(String type, MenuConfig config) {
		if (type.equals(TemplateConfig.SINGLE_GRID)) {
			// 单表
			config.setObjectCode(getPara("objectCode"));
		} else if (type.equals(TemplateConfig.QUERY)) {
			// 自定义查询 虚拟元对象编码
			config.setObjectCode("v_" + get("code"));
		} else if (type.equals(TemplateConfig.SINGLE_TREE)) {
			// 单表树
			config.setObjectCode(getPara("singleTreeObjectCode"));// TODO 此参数后续应去掉,tree配置应在一起.

			TreeConfig tc = new TreeConfig();
			tc.setObjectCode(config.getObjectCode());
			tc.setRootPid(getPara("rootPid"));
			tc.setIdField(getPara("idField", "id"));
			tc.setParentField(getPara("parentField"));
			tc.setTreeField(getPara("treeField"));
			tc.setIconField(getPara("iconField"));
			tc.setOrderField(getPara("orderField"));
			config.setTree(tc);
		} else if (type.equals(TemplateConfig.SINGLE_CHART)) {
			// 单表图
			config.setObjectCode(getPara("singleChartObjectCode"));

			String ys = getPara("singleChartY");
			List<String> ens = Arrays.asList(ys.split(","));

			// 根据字段英文名，获取字段中文名
			List<String> ycn = new ArrayList<>();
			List<MetaField> fields = MetaField.dao.queryFields(config.getObjectCode());
			for (String en : ens) {
				for (MetaField f : fields) {
					if (f.getEn().equals(en)) {
						ycn.add(f.getCn());
						break;
					}
				}
			}

			ChartConfig cc = new ChartConfig();
			cc.setType(getParaToInt("singleChartType"));
			cc.setX(getPara("singleChartX"));
			cc.setYunit(getPara("singleChartYunit"));
			cc.setY(ens);
			cc.setYcn(ycn);
			config.setChart(cc);

		} else if (type.equals(TemplateConfig.MASTER_SLAVE_GRID)) {
			// 主
			String masterObjectCode = getPara("masterObjectCode");
			String masterFieldCode = getPara("masterFieldCode");
			config.setObjectCode(masterObjectCode);
			config.setObjectField(masterFieldCode);

			// 子
			ArrayList<String> objects = new ArrayList<String>();
			ArrayList<String> fields = new ArrayList<String>();
			for (int i = 1; i <= 5; i++) {
				String slaveObjectCode = getPara("slaveObjectCode" + i);
				String slaveFieldCode = getPara("slaveFieldCode" + i);
				if (xx.isEmptyOne(slaveObjectCode, slaveFieldCode)) {
					break;
				}
				objects.add(slaveObjectCode);
				fields.add(slaveFieldCode);
			}
			config.setObjects(objects);
			config.setFields(fields);
		} else if (type.equals(TemplateConfig.TREE_GRID)) {
			// 树&表
			TreeConfig tc = new TreeConfig();
			tc.setObjectCode(getPara("treeGridTreeObjectCode"));
			tc.setObjectField(getPara("treeGridTreeFieldCode"));

			tc.setIconField(getPara("treeGridIconField"));
			tc.setTreeField(getPara("treeGridTreeField"));
			tc.setParentField(getPara("treeGridParentField"));
			tc.setIdField(getPara("treeGridIdField", "id"));
			tc.setRootPid(getPara("treeGridRootPid"));
			config.setTree(tc);

			config.setObjectCode(getPara("treeGridObjectCode"));
			config.setObjectField(getPara("treeGridFieldCode"));
		} else if (type.equals(TemplateConfig.OFFICE)) {
			config.getParams().put("office_type", getPara("office_type"));
		}
		
	}

	/**
	 * 菜单功能管理
	 */
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void menuFun() {
		String menuCode = getPara(0);

		List<Button> btns = Button.dao.findNoQueryByMenuCode(menuCode);
		// 动态获取按钮是否禁用
		for (Button btn : btns) {
			String ck = getPara("btn" + btn.getInt("id"));
			// 选中表示可被分配
			if (!xx.isEmpty(ck) && ck.equals("on")) {
				btn.set("is_hide", false);
			} else {
				// 未选中表示不可分配
				btn.set("is_hide", true);
			}
			btn.update();
		}

		renderJson(new Easy());
	}
	
	/**
	 * 是否能配置基础功能
	 */
	public void isFun() {
		int id = getParaToInt(0);
		Menu m = Menu.dao.findById(id);
		String type = m.getStr("type");
		// 目录和自定义功能,不能配置基本功能
		if (type.equals(Menu.TYPE_DIR) || type.equals(Menu.TYPE_DIY)) {
			renderJson(Ret.fail());
			return;
		}
		renderJson(Ret.ok());
	}
	
	// 菜单功能授权给角色
	public void auth() {
		int id = getParaToInt(0);

		HashMap<Integer, List<Button>> btnMap = new HashMap<Integer, List<Button>>();

		List<Button> btns = Button.dao.findByMenuId(id);
		for (Button b : btns) {
			int group = b.getInt("group_num");
			List<Button> list = btnMap.get(group);
			if (list == null) {
				list = new ArrayList<Button>();
				btnMap.put(group, list);
			}
			list.add(b);
		}

		List<Role> roles = Role.dao.findAll();

		List<Record> auths = RoleBtn.dao.findByMenuId(id);
		Menu menu = Menu.dao.findById(id);

		setAttr("menu", menu);
		setAttr("btnMap", btnMap);
		setAttr("roles", roles);
		setAttr("auths", auths);

		render("/eova/menu/auth.html");
	}

	// eova_button 升级 V1.4/1.5 -> V1.6
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void v16ButtonUpdate() {
		
		boolean isUpgrade = xx.getConfigBool("isUpgrade", false);
		if (!isUpgrade) {
			renderText("未开启升级模式，请启动配置 isUpgrade = true");
			return;
		}
		
		String sql = "select distinct(menu_code) from eova_button";
		List<String> codes = Db.use(xx.DS_EOVA).query(sql);
		
		// 修正异常的 查询按钮
		int num = Db.use(xx.DS_EOVA).update("update eova_button set is_base = 1 where ui = 'query'");
		System.err.println("修复异常的按钮数=" + num);
		// 删除所有查询按钮和基础功能
		num = Db.use(xx.DS_EOVA).update("delete from eova_button where is_base = 1 or ui = 'query'");
		System.err.println("删除按钮数=" + num);
		
		for (String code : codes) {
			Menu menu = Menu.dao.findByCode(code);
			if (menu == null || menu.getStr("type").equals(Menu.TYPE_DIR)) {
				continue;
			}
			new ButtonFactory(menu.getStr("type")).build(code, menu.getConfig());
		}
		
		// 删除无效权限数据
		num = Db.use(xx.DS_EOVA).update("delete from eova_role_btn");
		System.err.println("权限数据被清空");
		
		// 初始化超管权限
		sql = "select id from eova_button where menu_code = ?";
		List<Integer> ids = Db.use(xx.DS_EOVA).query(sql, "sys_auth_role");
		for (Integer id : ids) {
			RoleBtn rb = new RoleBtn();
			rb.set("bid", id);
			rb.set("rid", EovaConst.ADMIN_RID);
			rb.save();
		}
		
		initNewMenu();
		
		// 初始化EOVA按钮
		initEovaButton();
		
		
		renderText(" V1.4/1.5 -> V1.6 升级成功,请重新对所有角色重新进行权限分配！");
	}

	/**
	 * 初始化新菜单(没有生成按钮的菜单)
	 */
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void initNewMenu() {

		String isUpgrade = EovaConfig.getProps().get("isUpgrade");
		if (xx.isEmpty(isUpgrade) || !isUpgrade.equals("true")) {
			renderText("未开启升级模式，请启动配置 eova.config isUpgrade = true");
			return;
		}

		String sql = "select code from eova_menu where type not in ('dir') and code not in (select DISTINCT(menu_code) from eova_button);";
		List<String> codes = Db.use(xx.DS_EOVA).query(sql);

		for (String code : codes) {
			System.out.println(code);
			Menu menu = Menu.dao.findByCode(code);
			new ButtonFactory(menu.getStr("type")).build(code, menu.getConfig());
		}
		
		System.err.println("自动修复未生成按钮的菜单成功");
		
		renderText("自动为没有按钮的菜单初始化成功！");
	}
	
	/**
	 * 初始化指定菜单的基础按钮
	 */
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void initMenuButton() {
		List<JSONObject> selectRows = getSelectRows();
		for (int i = 0; i < selectRows.size(); i++) {
			JSONObject o = selectRows.get(i);
			String menuCode = o.getString("code");
			// 删除所有基础按钮
			Db.use(xx.DS_EOVA).update("delete from eova_button where is_base = 1 and menu_code = ?", menuCode);
			Menu menu = Menu.dao.findByCode(menuCode);
			new ButtonFactory(menu.getStr("type")).build(menuCode, menu.getConfig());

			System.err.println("初始化菜单按钮成功：" + menuCode);
		}
		
		renderJson(new Easy());
	}
	
	/**
	 * 初始化EOVA按钮信息
	 */
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void initEovaButton() {

		String isUpgrade = EovaConfig.getProps().get("isUpgrade");
		if (xx.isEmpty(isUpgrade) || !isUpgrade.equals("true")) {
			renderText("未开启升级模式，请启动配置 eova.config isUpgrade = true");
			return;
		}

		{
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "eova_menu", "新增");
		}
		{
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "eova_menu", "查看");
		}
		{
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "eova_task", "导入");
		}
		{
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "eova_object", "新增");
		}
		{
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "eova_object", "查看");
		}
		{
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "eova_object", "新增");
		}
		{
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "sys_auth_users", "查看");
		}
		{
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "sys_auth_users", "用户详细信息新增");
		}
		{
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "sys_auth_users", "用户详细信息删除");
		}
		{	
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "sys_auth_role", "查看");
		}
		{	
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and name = ?";
			Db.use(xx.DS_EOVA).update(sql, "sys_auth_role", "导入");
		}
		{	
			String sql = "UPDATE eova_button SET is_hide = 1 WHERE menu_code = ? and ui <> 'query'";
			Db.use(xx.DS_EOVA).update(sql, "sys_log");
		}
		
		System.err.println("初始化EOVA按钮信息成功！");

		renderText("初始化EOVA按钮信息成功！");
	}

	/**
	 * 初始化Oracle类型
	 * 场景:Mysql迁移到Oracle
	 */
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void initOracleType() {

		String isUpgrade = EovaConfig.getProps().get("isUpgrade");
		if (xx.isEmpty(isUpgrade) || !isUpgrade.equals("true")) {
			renderText("未开启升级模式，请启动配置 eova.config isUpgrade = true");
			return;
		}

		if (!xx.isOracle()) {
			renderText("当前DB不是Oracle禁止执行");
			return;
		}

		{
			// VARCHAR2
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'VARCHAR2' WHERE DATA_TYPE_NAME = 'VARCHAR' OR  DATA_TYPE_NAME = 'TEXT'";
			Db.use(xx.DS_EOVA).update(sql);
		}
		{
			// NUMBER
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'NUMBER' WHERE DATA_TYPE_NAME LIKE '%INT%'";
			Db.use(xx.DS_EOVA).update(sql);
		}
		{
			// FLOAT
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'NUMBER', DATA_DECIMAL = 1 WHERE DATA_TYPE_NAME LIKE '%FLOAT%'";
			Db.use(xx.DS_EOVA).update(sql);
		}
		{
			// DOUBLE
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'NUMBER', DATA_DECIMAL = 8 WHERE DATA_TYPE_NAME LIKE '%DOUBLE%'";
			Db.use(xx.DS_EOVA).update(sql);
		}
		{
			// DECIMAL
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'NUMBER', DATA_DECIMAL = 20 WHERE DATA_TYPE_NAME = 'DECIMAL'";
			Db.use(xx.DS_EOVA).update(sql);
		}
		{
			// CHAR
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'CHAR', DATA_SIZE = 1 WHERE DATA_TYPE_NAME = 'BIT'";
			Db.use(xx.DS_EOVA).update(sql);
		}
		{
			// DATE
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'DATE' WHERE DATA_TYPE_NAME LIKE '%DATE%' OR DATA_TYPE_NAME LIKE '%TIME%'";
			Db.use(xx.DS_EOVA).update(sql);
		}


		System.err.println("初始化Oracle类型成功！");

		renderText("初始化Oracle类型成功！");
	}

	/**
	 * 初始化PostgreSql类型
	 * 场景:Mysql迁移到PostgreSql
	 */
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void initPostgreSqlType() {

		String isUpgrade = EovaConfig.getProps().get("isUpgrade");
		if (xx.isEmpty(isUpgrade) || !isUpgrade.equals("true")) {
			renderText("未开启升级模式，请启动配置 eova.config isUpgrade = true");
			return;
		}

		if (!xx.isPgsql()) {
			renderText("当前DB不是PostgreSql禁止执行");
			return;
		}

		{
			// INT
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'INT4' WHERE DATA_TYPE_NAME LIKE '%INT%'";
			Db.use(xx.DS_EOVA).update(sql);
		}
		{
			// float
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'FLOAT4', DATA_DECIMAL = 4 WHERE DATA_TYPE_NAME LIKE '%FLOAT%'";
			Db.use(xx.DS_EOVA).update(sql);
		}
		{
			// double
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'FLOAT8', DATA_DECIMAL = 8 WHERE DATA_TYPE_NAME LIKE '%DOUBLE%'";
			Db.use(xx.DS_EOVA).update(sql);
		}
		{
			// BIT
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'INT2', DATA_SIZE = 1 WHERE DATA_TYPE_NAME = 'BIT'";
			Db.use(xx.DS_EOVA).update(sql);
		}
		{
			// DATETIME
			String sql = "UPDATE EOVA_FIELD SET DATA_TYPE_NAME = 'TIMESTAMP', DATA_SIZE = 1 WHERE DATA_TYPE_NAME = 'DATETIME'";
			Db.use(xx.DS_EOVA).update(sql);
		}

		System.err.println("初始化PostgreSql类型成功！");

		renderText("初始化PostgreSql类型成功！");
	}

	public void flow() {
		render("/eova/menu/flow.html");
	}

	// 创建流程
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void doFlow() {
		Integer dir = getParaToInt("dir", 0);// 默认为根目录
		String name = getPara("name");
		String code = getPara("code");
		String object_code = getPara("object_code");
		String object_field = getPara("object_field");
		String details = xx.replaceBlank(getPara("bs"));

		try {
			// 先生成菜单目录
			Menu menu = new Menu();
			menu.set("parent_id", dir);
			menu.set("name", name);
			menu.set("code", code);
			menu.set("type", TemplateConfig.DIR);
			menu.save();

			Integer mid = menu.getInt("id");

			// 根据命令生成菜单 待支付流程|my_orders10|10|1;
			String[] menus = details.split(";");
			for (String m : menus) {
				System.out.println(m);
				String[] ps = m.split("\\|");
				System.out.println(ps);
				String filter = String.format("%s = %s", object_field, ps[2]);
				createFlowMenu(mid, object_code, ps[0], ps[1], filter, ps[3]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(Easy.fail("创建流程失败:" + e.getMessage()));
			throw new NestedTransactionHelpException("创建流程失败");
		}

		renderJson(Easy.sucess());
	}

	private void createFlowMenu(Integer mid, String object, String name, String menuCode, String filter, String roles) {
		System.out.println("create " + menuCode);
		Menu menu = new Menu();
		menu.set("parent_id", mid);
		menu.set("name", name);
		menu.set("code", menuCode);
		menu.set("type", TemplateConfig.SINGLE_GRID);
		// 菜单状态过滤
		menu.set("filter", filter);

		// 菜单配置
		MenuConfig config = new MenuConfig();
		config.setObjectCode(object);
		menu.setConfig(config);
		menu.save();

		// 创建菜单按钮
		new ButtonFactory(TemplateConfig.SINGLE_GRID).build(menuCode, config);

		// 默认干掉除查询和查看的按钮
		Db.use(xx.DS_EOVA).update("update eova_button set is_hide = 1 where menu_code = ? and name not in ('查询', '查看')", menuCode);

		// 获取查询和查看的ID, 用于自动分配
		List<Integer> ids = Db.use(xx.DS_EOVA).query("select id from eova_button where menu_code = ? and name in ('查询', '查看')", menuCode);

		// 分配权限
		if (xx.isEmpty(roles)) {
			roles = EovaConst.ADMIN_RID + "";
		}
		for (String role : roles.split(",")) {
			ids.forEach(id -> {
				RoleBtn rb = new RoleBtn();
				rb.set("rid", role);
				rb.set("bid", id);
				rb.save();
			});
		}

		// 清菜单缓存
		BaseCache.delSer(EovaConst.ALL_MENU);
	}
}