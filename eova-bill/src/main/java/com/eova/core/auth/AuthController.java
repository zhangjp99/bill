/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.auth;

import java.util.*;

import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.config.EovaConst;
import com.eova.model.Button;
import com.eova.model.Menu;
import com.eova.model.Role;
import com.eova.model.RoleBtn;
import com.eova.model.User;
import com.eova.service.sm;
import com.eova.widget.WidgetUtil;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;

/**
 * 权限管理
 *
 * @author Jieven
 * @date 2014-9-11
 */
public class AuthController extends BaseController {

	/**
	 * 菜单基本功能管理
	 */
	public void toRoleChoose() {
		setAttr("rid", getPara(0));

		User user = getUser();
		// 当前用户的下级角色
		List<Role> roles = Role.dao.findSubRole(user.getRole().getInt("lv"));
		setAttr("roles", roles);

		// 获取可分配的菜单
		getAuthMenu();

		render("/eova/auth/roleChoose.html");
	}

	private void getAuthMenu() {
		long t = xx.time.now();
		// 查所有节点
		int rootId = 0;

		// 获取登录用户的角色
		User user = getUser();
		int rid = user.getRid();

		// 获取所有菜单信息
		LinkedHashMap<Integer, Menu> allMenu = (LinkedHashMap<Integer, Menu>) sm.auth.getByParentId(rootId);
		// 根据角色获取已授权菜单Code
		List<String> authMenuCodeList = sm.auth.queryMenuCodeByRid(rid);

		// 获取已授权菜单
		// LinkedHashMap<Integer, Menu> authMenu = new LinkedHashMap<Integer, Menu>();
		List<Menu> authMenu = new ArrayList<>();
		for (Map.Entry<Integer, Menu> map : allMenu.entrySet()) {
			Menu menu = map.getValue();
			// 未授权 也不是超级管理员
			if (!authMenuCodeList.contains(menu.getStr("code")) && !user.isAdmin()) {
				continue;
			}
			// authMenu.put(map.getKey(), menu);
			authMenu.add(menu);
		}

		// 获取已授权子菜单的所有上级节点(若功能有授权，需要找到上级才能显示)
		LinkedHashMap<Integer, Menu> authParent = new LinkedHashMap<Integer, Menu>();
		for (Menu m : authMenu) {
			WidgetUtil.getParent(allMenu, authParent, m);
		}

		// 根节点不显示排除
		authParent.remove(rootId);

		// 帮孤儿找亲爹, 把爹放到大儿子前面带队(静态树需要)
		F1: for (Map.Entry<Integer, Menu> map : authParent.entrySet()) {
			for (int i = 0; i < authMenu.size(); i++) {
				Menu m = authMenu.get(i);
				Menu dir = map.getValue();
				// 已存在目录, 跳过
				if (m.getInt("id").equals(dir.getInt("id"))) {
					continue F1;
				}
				if (m.getInt("parent_id").equals(map.getKey())) {
					authMenu.add(i, dir);
					continue F1;
				}
			}
		}

		// 将已授权的子菜单 放入 已授权 父菜单 Map
		// 顺序说明：父在前，子在后,子默认又是有序的
		//authParent.putAll(authMenu);

		// 获取所有按钮信息
		List<Button> btns = Button.dao.find("select * from eova_button where is_hide = 0 order by menu_code,group_num,order_num");
		// 获取已授权功能点
		HashSet<Integer> authBtnIds = new HashSet<>(RoleBtn.dao.queryByRid(rid));

		//		for (int i = 0; i < 8000; i++) {
		//			authBtnIds.add(10000+i);
		//		}
		//		Button tt = btns.get(0);
		//		for (int i = 0; i < 1000; i++) {
		//			Menu tm = authMenu.get(12);
		//			String menuCode = "test" + i;
		//			tm.set("code", menuCode);
		//			authMenu.add(tm);
		//			for (int j = 0; j < 8; j++) {
		//				Button tb = new Button();
		//				tb.put(tt);
		//				tb.set("menu_code", menuCode);
		//				btns.add(tb);
		//			}
		//		}

		// 1000 个菜单作为测试数据
		// 优化前: Load Cost Time:769ms 正想三重重遍历
		// 优化后: Load Cost Time:48ms 逆向单次遍历
		// 优化效果: 性能提升16倍
		// 优化思路:

		// 构建菜单对应功能点
		buildMenuBtn(btns, authMenu, authBtnIds, EovaConst.ADMIN_RID == rid);

		// Map 转 Tree Json
		// String json = WidgetUtil.menu2TreeJson(authParent, rootId);
		// 由前台手工构建静态树, 动态树对于授权并没有太大作用.

		setAttr("menus", authMenu);
	}

	/**
	 * 构建菜单按钮
	 * eg. [玩家管理] 口查询 口新增 口修改 口删除
	 * @param btns
	 * @param authMenu
	 * @param authBtnIds
	 * @param isAdmin
	 */
	private void buildMenuBtn(List<Button> btns, List<Menu> authMenu, HashSet<Integer> authBtnIds, boolean isAdmin){

		HashMap<String, Menu> menuMap = new HashMap<>();
		authMenu.forEach(x -> menuMap.put(x.getStr("code"), x));

		// 按钮比菜单多, 优先级遍历 例如:200菜单 对应 1000个功能, 多重循环消耗巨变
		for (Button btn : btns) {
			int bid = btn.getInt("id");
			String name = btn.getStr("name");
			String menuCode = btn.getStr("menu_code");

			Menu menu = menuMap.get(menuCode);
			if (menu == null) {
				System.out.println(menuCode + " 菜单不存在");
				continue;
			}
			// 不是超管 且 未授权 -> 忽略此按钮
			if (!isAdmin && !authBtnIds.contains(bid)) {
				continue;
			}

			String btnId = menu.get("btnId", "");
			String btnName = menu.get("btnName", "");

			btnId += bid + ",";
			btnName += name + ",";

			menu.put("btnId", btnId);
			menu.put("btnName", btnName);
		}

	}

	/**
	 * 获取角色已分配功能JSON
	 */
	public void getRoleFunJson() {
		int rid = getParaToInt(0);
		if (xx.isEmpty(rid)) {
			renderJson(new Easy("参数缺失!"));
			return;
		}
		List<Integer> list = RoleBtn.dao.queryByRid(rid);
		String json = JsonKit.toJson(list);
		renderJson(json);
	}

	/**
	 * 授权
	 */
	@Before(Tx.class)
	@TxConfig(xx.DS_EOVA)
	public void roleChoose() {
		int rid = getParaToInt(0);
		if (xx.isEmpty(rid)) {
			renderJson(new Easy("参数缺失!"));
			return;
		}
		// 获取选中功能点
		String checks = getPara("checks");
		if (xx.isEmpty(checks)) {
			renderJson(new Easy("请至少勾选一个功能点"));
			return;
		}

		String[] ids = checks.split(",");
		User user = getUser();
		int userRid = user.getRid();
		// 如果不是超管角色进行授权操作,只允许分配已拥有的权限
		if (!user.isAdmin()) {
			List<Integer> list = RoleBtn.dao.queryByRid(userRid);
			for (String id : ids) {
				if (!xx.isContains(list, id)) {
					renderJson(new Easy("禁止越权操作"));
					return;
				}
			}
		}

		// 删除历史授权
		Db.use(xx.DS_EOVA).update("delete from eova_role_btn where rid = ?", rid);
		if (xx.isEmpty(checks)) {
			renderJson(new Easy());
			return;
		}

		HashMap<String, RoleBtn> map = new HashMap<>();
		// 当前勾选的授权
		for (String id : ids) {
			if (!map.containsKey(id)) {
				RoleBtn x = new RoleBtn();
				x.set("rid", xx.toInt(rid));
				x.set("bid", xx.toInt(id));
				map.put(id, x);
			}
		}

		// 批量更新
		for (String key : map.keySet()) {
			RoleBtn rf = map.get(key);
			rf.remove("id");
			rf.set("rid", rid);
			rf.save();
		}
		
		renderJson(new Easy());
	}

	// 授权功能给某些角色
	public void update() {
		Boolean isCheck = getBoolean("is_check");
		Integer rid = getInt("rid");
		Integer bid = getInt("bid");

		if (isCheck) {
			RoleBtn x = new RoleBtn();
			x.set("rid", rid);
			x.set("bid", bid);
			x.save();
		} else {
			RoleBtn.dao.deleteByBidAndRid(bid, rid);
		}

		renderJson(Easy.sucess());
	}
}