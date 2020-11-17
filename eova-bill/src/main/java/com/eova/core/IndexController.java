/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.common.utils.EncryptUtil;
import com.eova.common.utils.xx;
import com.eova.common.utils.web.WebUtil;
import com.eova.config.EovaConfig;
import com.eova.config.EovaConst;
import com.eova.copyright.EovaAuth;
import com.eova.i18n.I18NBuilder;
import com.eova.model.Button;
import com.eova.model.Menu;
import com.eova.model.User;
import com.eova.service.LoginService;
import com.eova.service.sm;
import com.eova.widget.WidgetUtil;
import com.eova.widget.tree.TreeUtil;
import com.jfinal.captcha.CaptchaRender;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 首页相关业务
 * @author Jieven
 *
 */
public class IndexController extends BaseController {

	public void captcha() {
		render(new CaptchaRender());
	}

	public void code() {
		setAttr("exp1", "select id UID,login_id CN from users where <%if(user.id != 0){%>  id > ${user.id}<%}%> order by id desc");
		render("/eova/code.html");
	}

	public void toIndex() {
		boolean isSvg = xx.getConfigBool("ui.svg", false);
		setAttr("isSvg", isSvg);
		render("/eova/index.html");
	}

	// to /menu/icon
	@Deprecated
	public void toIcon() {
		render("/eova/icon.html");
	}

	public void toLogin() {
		boolean isCaptcha = xx.getConfigBool("isCaptcha", true);
		boolean isI18N = xx.getConfigBool("isI18N", false);
		setAttr("isCaptcha", isCaptcha);
		setAttr("isI18N", isI18N);

		// 应用版权配置
		setAttr("COPYRIGHT", xx.getConfig("app_login_copyright"));

		//setAttr("source", get("source"));

		render("/eova/login.html");
	}

	/**
	 * 修改密码
	 */
	public void toUpdatePwd() {
		User user = getUser();
		if (xx.isEmpty(user)) {
			setAttr("msg", "请先登录");
			toLogin();
			return;
		}
		render("/eova/updatePwd.html");
	}

	public void index() {
		/**
		 * 用户授权检查(检查为本地License校验,如果屏蔽检查会变成盗版系统,盗版有风险,请使用正版系统)
		 * 
		 * 注意：EOVA 受国家计算机软件著作权保护（登记号：2018SR1012969），不得分享传播源码、二次转售、组团购买等，违者必究。
		 * <<计算机软件保护条例>>
		 * 第二十四条　未经软件著作权人许可，有下列侵权行为的...依法追究刑事责任：
		 * （一）复制或者部分复制著作权人的软件的；
		 * （二）向公众发行、出租、通过信息网络传播著作权人的软件的；
		 * （三）故意避开或者破坏著作权人为保护其软件著作权而采取的技术措施的；
		 * （四）故意删除或者改变软件权利管理电子信息的；
		 * （五）转让或者许可他人行使著作权人的软件著作权的。
		 * 有前款第一项或者第二项行为的，可以并处每件100元或者货值金额1倍以上5倍以下的罚款；有前款第三项、第四项或者第五项行为的，可以并处20万元以下的罚款。
		 * 
		 * 竭诚为您提供最好的技术服务!
		 * 共建良性生态,合作长期共赢!
		 * 需要服务请联系:admin@eova.cn
		 *
		 **/
		String appId = EovaConfig.APP_ID;
		String appSecret = EovaConfig.APP_SECRET;
		if (xx.isEmpty(appId)) {
			renderHtml("启动之前请先配置应用信息：<br>配置文件：/src/main/resources/default/app.config<br>小贴士：应用ID和应用密钥在【用户中心>专业版应用】<br>立即<a href=\"http://www.eova.cn/my\">立即创建应用</a>");
			return;
		}
		if (!EovaAuth.isAuthApp(appId, appSecret)) {
			renderHtml("您使用了一个无效的应用ID或应用密钥，请仔细检查！<br>应用ID和应用密钥在【用户中心>专业版应用】<br>立即<a href=\"http://www.eova.cn/my\">立即创建应用</a>");
			return;
		}


		User user = getUser();
		// 已经登录
		if (user != null) {
			// 应用域名配置
			setAttr("APP_DOMAIN", xx.getConfig("app_domain"));
			// 默认主页配置
			setAttr("APP_MAIN", xx.getConfig("app_main"));
			setAttr("APP_MAIN_TITLE", xx.getConfig("app_main_title"));
			// 消息读取 
			setAttr("INCLUDE_INDEX", xx.getConfig("ui.include.index"));

			List<Menu> menus = buildMenu();
			if (menus == null) {
				renderText("当前角色无权限, 请联系管理员分配权限!");
				return;
			}

			// 首页初始化
			indexInit(this, user, menus);

			Record tree = TreeUtil.listToTree(WidgetUtil.modelsToRecords(menus), "0", "id", "parent_id", "children");
			setAttr("tree", tree);

			toIndex();
			return;
		}

		// 未登录
		toLogin();
	}

	public void doExit() {
		// 登出拦截器 >> 统计, 日志 等
		if (EovaConfig.getEovaIntercept() != null) {
			EovaConfig.getUserSessionIntercept().logout(getUser());
		}
		// 清除登录状态
		sm.login.logout(getCookie(LoginService.CKSID));
		removeCookie(LoginService.CKSID);
		redirect(EovaConfig.EOVA_INDEX);
	}

	public void doLogin() {
		String loginId = getPara("loginId");
		String loginPwd = getPara("loginPwd");
		boolean keepLogin = getParaToBoolean("keepLogin", false);

		keepPara();

		boolean isCaptcha = xx.toBoolean(EovaConfig.getProps().get("isCaptcha"), true);
		if (isCaptcha && !super.validateCaptcha("captcha")) {
			setAttr("msg", "验证码错误，请重新输入！");
			toLogin();
			return;
		}

		String ip = WebUtil.getRealIp(getRequest());
		Ret ret = sm.login.login(loginId, loginPwd, keepLogin, ip);
		if (ret.isFail()) {
			setAttr("msg", ret.getStr("msg"));
			toLogin();
			return;
		}

		User user = (User) ret.get(LoginService.USER); // 用户:id, rid, 帐号

		// 登录前置处理
		if (EovaConfig.getUserSessionIntercept() != null) {
			String msg = EovaConfig.getUserSessionIntercept().loginBefore(user);
			if (msg != null) {
				setAttr("msg", msg);
				toLogin();
				return;
			}
		}

		String sid = user.getStr(LoginService.SID);
		Integer maxAge = ret.getInt("maxAgeInSeconds");
		setCookie(LoginService.CKSID, sid, maxAge, true);
		// 登录拦截器 >> 登录初始化等
		if (EovaConfig.getUserSessionIntercept() != null) {
			EovaConfig.getUserSessionIntercept().login(user);
			updateUser(user);// 更新用户缓存
		}

		initI18N();

		// setAttr(LoginService.SID, ret.get(BaseCache.LOGIN));
		
		// 重定向到首页
		boolean isMobile = WebUtil.isMobile(getRequest());
		String source = EovaConfig.EOVA_INDEX;
		if (isMobile) {
			source = EovaConfig.EOVA_INDEX_H5;
		}
		redirect(source);
	}

	private void initI18N() {
		boolean isI18N = xx.getConfigBool("isI18N", false);
		if (isI18N) {
			String local = getPara("local");
			if (!xx.isEmpty(local)) {
				// 记录当前语种(默认记录1年)
				setCookie(EovaConst.LOCAL, local, 60 * 60 * 24 * 365, true);
				// 加载国际化文案
				List<Record> list = Db.use(xx.DS_EOVA).find("select * from eova_i18n where val is not null or val <> ''");
				I18NBuilder.init(list);
				xx.info("%s语言,加载文案总数:%s", local, list.size());
			}
		}
	}

	/**
	 * 首页初始化
	 * @param ctrl 
	 * @param user 当前用户
	 * @param menus 当前菜单
	 * @throws Exception
	 */
	protected void indexInit(Controller ctrl, User user, List<Menu> menus) {
		String userAccount = xx.getConfig("login.user.account", "login_id");
		setAttr("LOGIN_INFO", String.format("%s[%s]", user.role.getStr("name"), user.getStr(userAccount)));
	}

	/**
	 * 修改密码
	 */
	public void updatePwd() {
		String oldPwd = getPara("oldPwd");
		String newPwd = getPara("newPwd");
		String confirm = getPara("confirm");

		if (xx.isEmptyOne(oldPwd, newPwd, confirm)) {
			renderJson(new Easy("三个密码都不能为空"));
			return;
		}

		// 新密码和确认密码是否一致
		if (!newPwd.equals(confirm)) {
			renderJson(new Easy("新密码两次输入不一致"));
			return;
		}

		String userDs = xx.getConfig("login.user.ds", xx.DS_EOVA);
		String userTable = xx.getConfig("login.user.table", "eova_user");
		String userId = xx.getConfig("login.user.id", "id");
		String userPassword = xx.getConfig("login.user.password", "login_pwd");
		String uid = getUser().getStr("id");

		Record r = Db.use(userDs).findFirst(String.format("select %s,%s from %s where %s = ?", userId, userPassword, userTable, userId), uid);
		String pwd = r.getStr(userPassword);
		
		// 旧密码是否正确
		if (!pwd.equals(EncryptUtil.getSM32(oldPwd))) {
			renderJson(new Easy("密码错误"));
			return;
		}

		// 修改密码
		// 加密方式可配置
		String encrypt = xx.getConfig("eova.pwd.encrypt", "SM32");
		if (encrypt.equals("SM32")) {
			newPwd = EncryptUtil.getSM32(newPwd);
		} else {
			newPwd = EncryptUtil.getMd5(newPwd);
		}
		r.set(userPassword, newPwd);
		Db.use(userDs).update(userTable, r);

		renderJson(new Easy());
	}

	/**
	 * 构建当前用户可用菜单数据集
	 * @return
	 */
	private List<Menu> buildMenu() {
		User user = getUser();

		// 获取所有菜单
		List<Menu> menus = Menu.dao.queryMenu();

		// 获取已授权菜单ID
		List<Integer> ids = Button.dao.queryMenuIdByRid(user.getRid());
		if (xx.isEmpty(ids)) {
			return null;
		}

		// 递归查找已授权功能的上级节点
		HashSet<Integer> authPid = new HashSet<Integer>();
		for (Integer id : ids) {
			Menu m = getParent(menus, id);
			findParent(authPid, menus, m);
		}

		Iterator<Menu> it = menus.iterator();
		while (it.hasNext()) {
			Menu m = it.next();
			m.put("link", m.getUrl());
			m.remove("url");

			Integer mid = m.getInt("id");

			// 已授权目录
			if (xx.isContains(authPid, mid))
				continue;

			if (!xx.isContains(ids, mid)) {
				it.remove();
			}
		}

		I18NBuilder.models(menus, "name");
		return menus;
	}

	/**
	 * 获取父菜单
	 *
	 * @param menus
	 * @param id
	 * @return
	 */
	private static Menu getParent(List<Menu> menus, int id) {
		for (Menu m : menus) {
			if (m.getInt("id") == id) {
				return m;
			}
		}
		return null;
	}

	/**
	 *
	 * 递归向上查找父节点
	 *
	 * @param authPid 找到的父节点
	 * @param menus 所有菜单
	 * @param m
	 */
	private void findParent(HashSet<Integer> authPid, List<Menu> menus, Menu m) {
		if (m == null) {
			return;
		}
		Integer pid = m.getInt("parent_id");
		if (pid == 0) {
			return;
		}
		authPid.add(pid);

		Menu p = getParent(menus, pid);
		findParent(authPid, menus, p);
	}
}