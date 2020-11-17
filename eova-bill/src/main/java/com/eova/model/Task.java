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
 * 定时任务
 *
 * @author Jieven
 * @date 2014-9-10
 */
public class Task extends BaseModel<Task> {

	private static final long serialVersionUID = 4254060861819273244L;
	
	public static final Task dao = new Task();
	
	/** 暂停 **/
	public static final int STATE_STOP = 0;
	/** 运行 **/
	public static final int STATE_START = 1;

	public List<Task> findAll(){
		return this.find("select * from eova_task");
	}
	
	public List<Task> findByStart(){
		return this.find("select * from eova_task where state = 2");
	}
	
	public int updateState(int id, int state){
		return Db.use(xx.DS_EOVA).update("update eova_task set state = ? where id = ?", state, id);
	}
}