/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.template.single;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import com.eova.aop.AopContext;
import com.eova.common.utils.xx;
import com.eova.common.utils.excel.ExcelUtil;
import com.eova.model.MetaObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

public class SingleAtom implements IAtom {

	private final Controller ctrl;
	private File file;
	private MetaObject object;
	private SingleIntercept intercept;
	private List<Record> records;
	private Exception runExp;

	public SingleAtom(File file, MetaObject object, SingleIntercept intercept, Controller ctrl) {
		super();
		this.file = file;
		this.object = object;
		this.intercept = intercept;
		this.ctrl = ctrl;
	}

	@Override
	public boolean run() throws SQLException {
		InputStream is = null;
		try {
			// 导入Excel
			is = new FileInputStream(file);
			records = ExcelUtil.importExcel(is, object.getFields());
			
			if (xx.isEmpty(records)) {
				throw new Exception("无法从导入的Excel获取到任何数据，请认真检查Excel模版！");
			}

			// 导入前置任务
			if (intercept != null) {
				AopContext ac = new AopContext(ctrl, records);
				intercept.importBefore(ac);
			}

			// 保存导入数据
			for (Record re : records) {
				// TODO 拦截器 控制字段是否持久化，不需要的 比如 自增的 foreach remove 即可
				// 有主键是更新，没主键是新增
				Object pk = re.get(object.getPk());
				if (pk == null) {
					Db.use(object.getDs()).save(object.getTable(), object.getPk(), re);
				} else {
					Db.use(object.getDs()).update(object.getTable(), object.getPk(), re);
				}
			}

			// 新增后置任务
			if (intercept != null) {
				AopContext ac = new AopContext(ctrl, records);
				intercept.importAfter(ac);
			}
		} catch (Exception e) {
			runExp = e;
			return false;
		} finally {
			if (is != null) try { is.close(); } catch (IOException e) { e.printStackTrace(); }
			// 自动删除废弃临时文件
			if (file.exists()) file.delete();
		}
		return true;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public MetaObject getObject() {
		return object;
	}

	public void setObject(MetaObject object) {
		this.object = object;
	}

	public SingleIntercept getIntercept() {
		return intercept;
	}

	public void setIntercept(SingleIntercept intercept) {
		this.intercept = intercept;
	}

	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	public Exception getRunExp() {
		return runExp;
	}

	public void setRunExp(Exception runExp) {
		this.runExp = runExp;
	}

}