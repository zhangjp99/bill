/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.aop;

import java.util.ArrayList;
import java.util.List;

import com.eova.common.base.BaseController;
import com.eova.model.MetaField;
import com.eova.model.MetaObject;
import com.eova.model.User;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;


/**
 * AOP 上下文
 *
 * @author Jieven
 * @date 2014-8-29
 */
public class AopContext {

    /**
     * 当前控制器
     */
	public Controller ctrl;

    /**
     * 当前用户对象
     */
    public User user;
    
    /**
	 * 当前元对象(只读)
	 * PS:仅用于读取, 因为前台是单独获取的, 拦截器变更也不会生效
	 */
    public MetaObject object;

	/**
	 * 当前元字段(读写)
	 */
	public List<MetaField> fields;

    /**
     * 当前操作数据集(批量操作)
     */
	public List<Record> records;

    /**
     * 当前操作对象(单条数据操作)
     */
	public Record record;

    /**
     * 当前操作对象固定值
     * 用途：新增/编辑时预设固定初始值
     * 推荐：固定初始值，建议禁用字段使用addBefore()拦截添加值
     */
    public Record fixed;

    /**
     * 追加SQL条件
     */
	public String condition = "";
    /**
     * 自定义SQL覆盖默认查询条件
     * 格式: where xxx = xxx
     */
    public String where;
    /**
     * 自定义SQL参数
     */
    public List<Object> params = new ArrayList<Object>();
    /**
     * 自定义SQL覆盖默认排序
     * 格式: order by xxx desc
     */
    public String sort;
	/**
	 * 完全自定义整个SQL语句(可以支持任意语法,多层嵌套,多表连接查询等)
	 */
	public String sql;


	public AopContext(Controller ctrl) {
        this.ctrl = ctrl;
		this.user = ((BaseController) ctrl).getUser();
    }

	public AopContext(Controller ctrl, List<Record> records) {
        this(ctrl);
        this.records = records;
		// 小白兼容
		if (records != null && records.size() == 1) {
			this.record = this.records.get(0);
		}
    }

	public AopContext(Controller ctrl, Record record) {
        this(ctrl);
        this.record = record;
    }

	public int UID() {
		return this.user.get("id");
	}

	public String getStart(String en) {
		return ctrl.get("start_" + en);
	}

	public String getEnd(String en) {
		return ctrl.get("end_" + en);
	}
	public String getQuery(String en) {
		return ctrl.get("query_" + en);
	}
}