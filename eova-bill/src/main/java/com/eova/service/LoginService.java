/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.eova.common.base.BaseCache;
import com.eova.common.utils.EncryptUtil;
import com.eova.common.utils.xx;
import com.eova.model.Session;
import com.eova.model.User;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 登录服务
 */
public class LoginService {

	/**SID Cookie Key**/
	public static final String CKSID = "eovasid";
	/**SID Field Key**/
	public static final String SID = "sid";
	/**Login User Key**/
	public static final String USER = "loginUser";

	// 登录配置信息
	private String userDs = xx.getConfig("login.user.ds", xx.DS_EOVA);
	private String userTable = xx.getConfig("login.user.table", "eova_user");
	private String userId = xx.getConfig("login.user.id", "id");
	private String userAccount = xx.getConfig("login.user.account", "login_id");
	private String userPassword = xx.getConfig("login.user.password", "login_pwd");
	private String userRid = xx.getConfig("login.user.rid", "rid");

	/**
	 * 根据UID查找用户
	 * @param uid 类型不确定 int long String
	 * @return
	 */
	private User findUserByUid(Object uid) {
		Record r = Db.use(userDs).findFirst(String.format("select * from %s where %s = ?", userTable, userId), xx.ET(uid));
		return initUser(r);
	}

	/**
	 * 登录校验
	 * @param loginId 帐号
	 * @param loginPwd 密码
	 * @return
	 */
	private Ret loginValidate(String loginId, String loginPwd) {
		Record r = Db.use(userDs).findFirst(String.format("select * from %s where %s = ?", userTable, userAccount), loginId);
		if (r == null) {
			return Ret.fail("msg", "用户名不存在");
		}
		// 加密方式可配置
		String encrypt = xx.getConfig("eova.pwd.encrypt", "SM32");
		if (encrypt.equals("SM32")) {
			loginPwd = EncryptUtil.getSM32(loginPwd);
		} else {
			loginPwd = EncryptUtil.getMd5(loginPwd);
		}
		if (!r.getStr(userPassword).equals(loginPwd)) {
			return Ret.fail("msg", "密码错误");
		}
		// 初始化用户, 并回登录源数据
		return Ret.ok(USER, initUser(r));
	}

	/**
	 * 初始化用户
	 * @param r
	 * @return
	 */
	private User initUser(Record r) {
		User user = new User();
		user.setData(r);// 登录源数据备用
		user.set("id", r.get(userId));
		user.set("rid", r.getInt(userRid));
		user.put(userAccount, r.get(userAccount));// 帐号备用
		Integer orgId = r.getInt("org_id");
		// 尝试自动续传部门ID
		if (orgId != null) {
			user.set("org_id", orgId);
		}
		// 尝试自动续传姓名
		String name = r.getStr("name");
		if (name != null) {
			user.set("name", name);
		}

		// 初始化角色
		user.initRole();

		// 初始化权限
		initAuth(user);

		return user;
	}

	/**
	 * 退出登录
	 */
	public void logout(String sid) {
		if (sid != null) {
			CacheKit.remove(BaseCache.LOGIN, sid);
			Session.dao.deleteById(sid);
		}
	}

	/**
	 * 登录
	 * @param loginId
	 * @param loginPwd
	 * @param keepLogin
	 * @param ip
	 * @return
	 */
	public Ret login(String loginId, String loginPwd, boolean keepLogin, String ip) {
		loginId = loginId.trim();
		loginPwd = loginPwd.trim();

		Ret ret = loginValidate(loginId, loginPwd);
		if (ret.isFail()) {
			return ret;
		}

		User user = (User) ret.get(USER);

		int sessionMin = xx.getConfigInt("login.user.session", 2);
		// 如果用户勾选保持登录，过期时间为 1 年，否则为 120分钟，单位为秒
		long liveSeconds = keepLogin ? 1 * 365 * 24 * 60 * 60 : sessionMin * 60;
		// 传递给控制层的 cookie
		int maxAgeInSeconds = (int) (keepLogin ? liveSeconds : -1);
		// expireAt 用于设置 session 的过期时间点，需要转换成毫秒
		long expire = System.currentTimeMillis() + (liveSeconds * 1000);
		// 保存登录 session 到数据库
		String sid = StrKit.getRandomUUID();
		Session session = new Session(sid, user.getId(), expire);
		if (!session.save()) {
			return Ret.fail("msg", "会话保存异常，请联系管理员");
		}

		user.put(SID, sid); // SID备用

		CacheKit.put(BaseCache.LOGIN, sid, user);

		loginLog(user.getId(), ip);

		return Ret.ok(USER, user).set("maxAgeInSeconds", maxAgeInSeconds);
	}



	/**
	 * 通过 sessionId 获取登录用户信息
	 * sessoin表结构：session(id, uid, expire)
	 *
	 * 1：先从缓存里面取，如果取到则返回该值，如果没取到则从数据库里面取
	 * 2：在数据库里面取，如果取到了，则检测是否已过期，如果过期则清除记录，
	 *     如果没过期则先放缓存一份，然后再返回
	 */
	public User loginBySid(String sid, String ip) {
		Session session = Session.dao.findById(sid);
		if (session == null) {      // session 不存在
			return null;
		}
		if (session.isExpired()) {  // session 已过期
			session.delete();		// 被动式删除过期数据，此外还需要定时线程来主动清除过期数据
			return null;
		}

		User user = findUserByUid(session.getUid());
		if (user != null) {//  && user.getStatus()
			user.put(SID, sid); // 备份SID
			CacheKit.put(BaseCache.LOGIN, sid, user);

			loginLog(user.getId(), ip);
			return user;
		}
		return null;
	}

	/**
	 * 获取已登录用户
	 * @param sid
	 * @return
	 */
	public User getLoginUser(String sid) {
		return CacheKit.get(BaseCache.LOGIN, sid);
	}

	private void loginLog(Object uid, String ip) {
		Record loginLog = new Record().set("user_id", uid).set("ip", ip);
		Db.use(xx.DS_EOVA).save("eova_login_log", loginLog);
	}

	/**
	 * 更新登录用户
	 * @param user
	 */
	public void update(User user) {
		String sid = user.get(SID);
		CacheKit.put(BaseCache.LOGIN, sid, user);
	}

	//	/**
	//	 * 重载用户
	//	 * @param user
	//	 */
	//	public void reload(User user) {
	//		String sid = user.get(SID);
	//		User dbUser = userDao.findById(user.getId());
	//		dbUser.put(SID, sid); // 备份SID
	//
	//		// 集群方式下，要做一通知其它节点的机制，让其它节点使用缓存更新后的数据，
	//		// 将来可能把 user 用 id : obj 的形式放缓存，更新缓存只需要 CacheKit.remove("account", id) 就可以了，
	//		// 其它节点发现数据不存在会自动去数据库读取，所以未来可能就是在 AccountService.getById(int id)的方法引入缓存就好
	//		// 所有用到 user 对象的地方都从这里去取
	//		CacheKit.put(BaseCache.LOGIN, sid, user);
	//	}

	/**
	 * 初始化授权
	 * @param user 当前登录用户数据
	 * @throws Exception
	 */
	private void initAuth(User user) {
		// 初始化获取授权信息
		Set<String> auths = new HashSet<String>();
		String sql = "SELECT bs FROM eova_role_btn rf LEFT JOIN eova_button b ON rf.bid = b.id WHERE rf.rid = ?";
		List<Record> bss = Db.use(xx.DS_EOVA).find(sql, user.getRid());
		for (Record r : bss) {
			String bs = r.getStr("bs");
			if (xx.isEmpty(bs)) {
				continue;
			}
			if (!bs.contains(";")) {
				auths.add(bs);
				continue;
			}
			String[] strs = bs.split(";");
			for (String str : strs) {
				auths.add(str);
			}
		}
		if (xx.isEmpty(auths)) {
			LogKit.error("用户角色没有任何授权,请联系管理员授权");
		}
		user.put("auths", auths);
	}
}