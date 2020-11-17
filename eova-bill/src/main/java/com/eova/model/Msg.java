/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.model;

import java.util.List;

import com.eova.common.base.BaseModel;
import com.eova.common.utils.xx;
import com.jfinal.plugin.activerecord.Db;

/**
 * 系统消息
 *
 * @author Jieven
 * @date 2014-9-10
 */
public class Msg extends BaseModel<Msg> {

	private static final long serialVersionUID = -1592533967096109392L;

	public static final Msg dao = new Msg().dao();

	public static final int APPRO = 1;
	public static final int NOTICE = 2;

	/**
	 * 审批消息
	 * @param refId 流程实例ID
	 * @param fromUid 发信人
	 * @param toUid 收信人
	 */
	public void appro(int refId, int fromUid, int toUid) {
		saveMsg(APPRO, refId, fromUid, toUid);
	}

	/**
	 * 抄送消息
	 * @param refId 流程实例ID
	 * @param fromUid 发信人
	 * @param toUid 收信人
	 */
	public void notice(int refId, int fromUid, int toUid) {
		saveMsg(NOTICE, refId, fromUid, toUid);
	}

	/**
	 * 已读
	 * @param toUid 收信人
	 * @param refId 关联ID
	 */
	public void read(int type, int toUid, int refId) {
		Db.use(xx.DS_EOVA).update("update eova_msg set status = 1 where type = ? and to_uid = ? and ref_id = ?", type, toUid, refId);
	}

	/**
	 * 撤销未读
	 * @param type 消息类型
	 * @param refId 关联ID
	 */
	public void deleteNoRead(int type, int refId) {
		Db.use(xx.DS_EOVA).delete("delete from eova_msg where status = 0 and type = ? and ref_id = ?", type, refId);
	}

	private void saveMsg(int type, int refId, int fromUid, int toUid) {
		Msg msg = new Msg();
		msg.set("type", type);
		msg.set("ref_id", refId);
		msg.set("from_uid", fromUid);
		msg.set("to_uid", toUid);
		msg.save();
	}

	/**
	 * 我的消息
	 * @param uid 我的ID
	 * @param type 消息类型
	 * @param status 0=未读/1=已读
	 * @return
	 */
	public List<Msg> my(int uid, int type, int status) {
		return this.find("select * from eova_msg where type = ? and to_uid = ? and status = ?", type, uid, status);
	}
}