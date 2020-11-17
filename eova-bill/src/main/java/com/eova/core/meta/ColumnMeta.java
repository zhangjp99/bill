/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.meta;

import com.alibaba.fastjson.JSONObject;
import com.eova.common.utils.xx;
import com.eova.config.EovaConfig;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ColumnMeta {
	
	public String ds;				// 数据源名
	public String table;			// 数据表名
	
	public int position;			// 序号
	public String name;				// 字段名
	
	public int dataType;			// 数据类型
	public String dataTypeName;		// 数据类型名称
	public int dataSize;			// 整数位长度
	public int dataDecimal;			// 小数位长度
	
	public boolean isNull;			// 是否允许空值
	public boolean isAuto;			// 是否自增
	
	public String defaultValue;		// 默认值
	public String remarks;			// 注释
	
	public ColumnMeta(String ds, String table, JSONObject o){
		this.ds = ds;
		this.table = table;
		
		this.position = o.getIntValue("ORDINAL_POSITION");
		this.name = o.getString("COLUMN_NAME").toLowerCase();// G001 表名+主键 小写
		
		this.dataType = o.getIntValue("DATA_TYPE");
		this.dataTypeName = o.getString("TYPE_NAME");
		this.dataSize = o.getIntValue("COLUMN_SIZE");
		this.dataDecimal = o.getIntValue("DECIMAL_DIGITS");
		
		this.isNull = "YES".equalsIgnoreCase(o.getString("IS_NULLABLE")) ? true : false;

		this.defaultValue = buildDefault(o.getString("COLUMN_DEF"));
		this.isAuto = "YES".equalsIgnoreCase(o.getString("IS_AUTOINCREMENT")) ? true : false;
		// Oracle 和 PGSQL 根据默认值判定是否为自增字段
		if (defaultValue != null && defaultValue.contains("nextval")) {
			this.isAuto = true;
		}
		
		this.remarks = o.getString("REMARKS");
		// 月读：自动处理注释 
		buildRemarks();
	}
	
	private String buildDefault(String s) {
		if (xx.isEmpty(s)) {
			return "";
		}
		if (xx.isPgsql()) {
			// 默认值存在类型转换符的处理
			if (s.contains("::")) {
				try {
					// nextval('seq_product'::regclass)
					if (s.startsWith("nextval")) {
						// 自增 新增应禁用
						return "";
					}

					String[] ss = s.split("::");
					String val = ss[0];

					// NULL::character varying,
					if (val.equalsIgnoreCase("NULL")) {
						return "";
					}
					// 'http://www..com'::character varying
					if (val.startsWith("'") && val.endsWith("'")) {
						val = xx.delStart(val, "'");
						val = xx.delEnd(val, "'");
						return val;
					}
				} catch (Exception e) {
					System.err.print("Postgresql 导入元字段, 处理默认数据异常:" + e.getMessage());
					return "";
				}
			}
		}

		return s;
	}
	
	/**
	 * 自动根据注释解析列名和字典
	 * 
	 * @param remarks 注释
	 * @param table 表
	 * @param en 字段
	 * @return
	 */
	public void buildRemarks() {
		if (xx.isEmptyOne(this.ds, this.table, this.remarks)) {
			return;
		}
		// eg. 状态:1=上架，2=售罄,3=下架 ,4=过期
		String[] ss = null;
		try {
			// 获取第1项注释作为列名
			if (remarks.contains(":") || remarks.contains("：")) {
				ss = remarks.split(":|：");
			}
			if (xx.isEmpty(ss)) {
				return;
			}
			this.remarks = ss[0];

			// 获取第2项注释作为字典
			if (ss.length > 1) {
				String ss1 = ss[1];
				// 如果没有=号 说明是其它描述eg. 原价:大于等于0
				if (ss1.contains("=")) {
					
					LogKit.info(String.format("自动生成字典[%s - %s]", this.table, this.name));
					
					String[] dicts = ss1.split(",|，");
					
					String dictTable = EovaConfig.getProps().get("main_dict_table");
					// Eova数据源,固定字典表
					if (this.ds.equals(xx.DS_EOVA)) {
						dictTable = "eova_dict";
					}
					// 清除字典
					Db.use(this.ds).update(String.format("delete from %s where object = ? and field = ?", dictTable), this.table, this.name);
					// 生成字典
					for (String dict : dicts) {
						
						String[] sss = dict.split("=");
						String value = sss[0];
						String key = sss[1];
						
						// 保存字典
						Record r = new Record();
						r.set("value", value);
						r.set("name", key);
						r.set("object", this.table);
						r.set("field", this.name);
						Db.use(this.ds).save(dictTable, r);
					}
				}
			}
		} catch (Exception e) {
			LogKit.error("元字段备注预处理异常:" + remarks, e);
		}
	}
}