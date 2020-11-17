/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.eova.common.base.BaseModel;
import com.eova.common.utils.xx;
import com.eova.common.utils.time.TimestampUtil;
import com.eova.config.EovaConfig;
import com.eova.config.EovaConst;
import com.eova.core.meta.ColumnMeta;
import com.eova.engine.DynamicParse;
import com.eova.i18n.I18NBuilder;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 字段属性
 * 
 * @author Jieven
 * @date 2014-9-10
 */
public class MetaField extends BaseModel<MetaField> {

	private static final long serialVersionUID = -7381270435240459528L;

	public static final MetaField dao = new MetaField();

	public static final String TYPE_TEXT = "文本框";
	public static final String TYPE_NUM = "数字框";
	public static final String TYPE_COMBO = "下拉框";
	public static final String TYPE_COMBO_TREE = "下拉树";
	public static final String TYPE_FIND = "查找框";
	public static final String TYPE_TIME = "时间框";
	public static final String TYPE_DATE = "日期框";
	public static final String TYPE_TEXTS = "文本域";
	public static final String TYPE_EDIT = "编辑框";
	public static final String TYPE_BOOL = "布尔框";
	public static final String TYPE_AUTO = "自增框";
	public static final String TYPE_IMG = "图片框";
	public static final String TYPE_FILE = "文件框";
	public static final String TYPE_JSON = "JSON";
	public static final String TYPE_RADIO = "单选框";

	/** 字段数据类型-字符 **/
	public static final String DATATYPE_STRING = "string";
	/** 字段数据类型-数值 **/
	public static final String DATATYPE_NUMBER = "number";
	/** 字段数据类型-时间 **/
	public static final String DATATYPE_TIME = "time";

	public MetaField() {

	}

	public MetaField(String objectCode, ColumnMeta col) {

		// TODO 目前统一全部小写,有待观察全小写会导致什么问题
		//		if (EovaConfig.isLowerCase) {
		//			col.name = col.name.toLowerCase();
		//		}

		this.set("object_code", objectCode);
		this.set("en", col.name);
		this.set("cn", col.remarks);
		this.set("order_num", col.position * 10);
		this.set("is_required", !col.isNull);
		this.set("data_type", col.dataType);
		this.set("data_type_name", col.dataTypeName);
		this.set("data_size", col.dataSize);
		this.set("data_decimal", col.dataDecimal);
		this.set("defaulter", col.defaultValue);
		this.set("is_auto", col.isAuto);

		this.set("type", getFormType(col));

		// 是否自增
		if (col.isAuto) {
			// 自增框新增禁用
			this.set("add_status", 50);
			// 自增框编辑隐藏
			this.set("update_status", 20);
		}
		// 将注释作为CN,若为空使用EN,比如某些专有名词缩写,QQ,SKU 等
		if (xx.isEmpty(this.getCn())) {
			this.set("cn", this.getEn().toUpperCase());
		}
		// 默认值
		String defaulter = this.getStr("defaulter");
		if (xx.isEmpty(defaulter)) {
			this.set("defaulter", "");
		} else {
			// 清除Mysql函数,不能作为字符串长传入.如果缺省值应在DB中自动自动执行.
			if (defaulter.indexOf("(") != -1 && defaulter.indexOf(")") != -1) {
				this.set("defaulter", "");
			}
		}
	}

	/**
	 * 获取表单类型
	 * 
	 * @param isAuto 是否自增
	 * @param typeName 类型
	 * @param size 长度
	 * @return
	 */
	public static String getFormType(ColumnMeta col) {
		String t = col.dataTypeName.toLowerCase();
		int s = col.dataSize;
		if (t.contains("time") || t.contains("date")) {
			return TYPE_TIME;
		} else if (col.isAuto) {
			return TYPE_AUTO;
		} else if (s > 50) {
			return TYPE_TEXTS;
		} else if (t.equals("bit")) {
			return TYPE_BOOL;
		} else if (s == 1 && (t.equals("tinyint") || t.equals("char"))) {
			return TYPE_BOOL;
		} else {
			// 默认都是文本框
			return TYPE_TEXT;
		}
	}

	public MetaFieldConfig getConfig() {
		String json = this.getStr("config");
		return parseConfig(json);
	}

	public static MetaFieldConfig parseConfig(String json) {
		if (xx.isEmpty(json)) {
			return null;
		}
		return JSON.parseObject(json, MetaFieldConfig.class);
	}

	public void setConfig(MetaFieldConfig config) {
		this.set("config", JSON.toJSONString(config));
	}

	/**
	 * 获取字段英文名
	 * 
	 * @return
	 */
	public String getEn() {
		return this.getStr("en");
	}

	/**
	 * 获取字段中文名
	 * 
	 * @return
	 */
	public String getCn() {
		return this.getStr("cn");
	}

	/**
	 * 是否存在Eova表达式(sql和固定)
	 * @return
	 */
	public boolean isEovaExp() {
		return getExp() != null || getItems() != null;
	}

	/**
	 * 获取Eova表达式
	 * @return
	 */
	public String getExp() {
		String exp = this.getStr("exp");
		if (getItems() != null) {
			// 排除固定值域
			return null;
		}
		return exp;
	}

	/**
	 * 获取固定值域
	 * @return
	 */
	public List<Record> getItems() {
		String exp = this.getStr("exp");
		if (xx.isEmpty(exp)) {
			return null;
		}
		exp = exp.trim();
		if (!exp.toLowerCase().startsWith("select")) {
			List<Record> arrs = new ArrayList<>();

			String[] ss = xx.splitBlank(exp);
			for (String s : ss) {
				String[] item = s.split("=");
				arrs.add(new Record().set("id", item[0]).set("cn", item[1]));
			}

			return arrs;
		} else {
			return null;
		}
	}


	/**
	 * 获取控件类型
	 * 
	 * @return
	 */
	public String getType() {
		return this.getStr("type");
	}

	/**
	 * 获取数据类型
	 * 
	 * @return
	 */
	public int getDataType() {
		return this.getInt("data_type");
	}

	/**
	 * 是否是虚拟字段
	 * @return
	 */
	public boolean isVirtual(){
		if (xx.isEmpty(getStr("table_name"))) {
			return false;
		}
		return getStr("table_name").equals(EovaConst.VIRTUAL);
	}
	
	public String getDataTypeName() {
		return this.getStr("data_type_name").toUpperCase();
	}

	public int getDataSize() {
		return this.getInt("data_size");
	}

	public int getDataDecimal() {
		return this.getInt("data_decimal");
	}
	
	// TODO PGSQL 为强类型, 为了兼容性使用INT2, 需要自动识别为Bool后使用
	/**
	 * 是否显示字段
	 * @return
	 */
	public boolean isShow() {
		return xx.isTrue(this.get("is_show"));
	}

	/**
	 * 是否多选
	 * @return
	 */
	public boolean isMultiple() {
		return xx.isTrue(this.get("is_multiple"));
	}

	/**
	 * 获取元字段数据模版(用于模拟元数据)
	 * 
	 * @return
	 */
	public MetaField getTemplate() {
		MetaField mf = this.queryFisrtByCache("select * from eova_field where id = 1");
		mf.remove("id");
		return mf;
	}

	/**
	 * 获取元字段信息
	 * 
	 * @param objectCode 对象Code
	 * @return
	 */
	public List<MetaField> queryByObjectCode(String objectCode) {
		List<MetaField> list = MetaField.dao.queryByCache("select * from eova_field where object_code = ? order by fieldnum,order_num", objectCode);
		I18NBuilder.models(list, "cn", "fieldset", "placeholder");
		return list;
	}

	/**
	 * 获取字段
	 * 
	 * @param objectCode 对象Code
	 * @param en 字段Key
	 * @return
	 */
	public MetaField getByObjectCodeAndEn(String objectCode, String en) {
		MetaField ei = MetaField.dao.queryFisrtByCache("select * from eova_field where object_code = ? and en = ? order by order_num", objectCode, en);
		return ei;
	}

	public void deleteByObjectId(int objectId) {
		String sql = "delete from eova_field where object_code = (select code from eova_object where id = ?)";
		Db.use(xx.DS_EOVA).update(sql, objectId);
	}

	public void deleteByObjectCode(String objectCode) {
		String sql = "delete from eova_field where object_code = ?";
		Db.use(xx.DS_EOVA).update(sql, objectCode);
	}

	public List<MetaField> queryShowFieldByObjectCode(String objectCode) {
		return MetaField.dao.queryByCache("select * from eova_field where object_code = ? and is_show = 1 order by fieldnum,order_num", objectCode);
	}

	/**
	 * 查询视图包含的持久化对象Code
	 * 
	 * @param objectCode 视图对象编码
	 * @return
	 */
	public List<MetaField> queryPoCodeByObjectCode(String objectCode) {
		return MetaField.dao.find("SELECT DISTINCT(po_code) from eova_field where object_code = ?", objectCode);
	}

	/**
	 * 查询元字段(Eova内部使用)
	 * 
	 * @param objectCode 元对象编码
	 * @return
	 */
	public List<MetaField> queryFields(String objectCode) {
		return MetaField.dao.queryByObjectCode(objectCode);
	}

	/**
	 * 
	 * 查询元字段(前端直接获取)
	 * 敏感信息屏蔽
	 * 权限处理
	 * 
	 * @param objectCode
	 * @param user
	 * @return
	 */
	public List<MetaField> queryFields(String objectCode, User user) {
		List<MetaField> mfs = MetaField.dao.queryByObjectCode(objectCode);
		buildMetaField(objectCode, user, mfs);
		return mfs;
	}

	/**
	 * 分组查询元字段(用于表单分组)
	 * 
	 * @param objectCode
	 * @param user
	 * @return
	 */
	public List<MetaField> queryFieldsGroup(String objectCode, User user) {
		List<MetaField> mfs = MetaField.dao.queryByObjectCode(objectCode);
		buildMetaField(objectCode, user, mfs);
		return mfs;
	}

	/**
	 * 构建前端所需数据
	 * @param objectCode
	 * @param user
	 * @param mfs
	 */
	private void buildMetaField(String objectCode, User user, List<MetaField> mfs) {
		for (MetaField f : mfs) {
			String en = f.getStr("en");
			String exp = f.getExp();
			
			if(xx.isEmpty(exp)){
				if (f.getType().equals(MetaField.TYPE_FIND)) {
					throw new RuntimeException(String.format("元对象[%s]中的元字段[%s]是查找框，该字段必须填写Eova表达式！", objectCode, f.getEn()));
				} else if(f.getType().equals(MetaField.TYPE_COMBO)){
					throw new RuntimeException(String.format("元对象[%s]中的元字段[%s]是下拉框，该字段必须填写Eova表达式！", objectCode, f.getEn()));
				}
			}

			// Exp 固定值的处理(非sql, 即固定值)
			//			String items = f.getItems();
			//			if (!xx.isEmpty(items)) {
			//				
			//
			//				f.put("items", arrs);// 用于EOVA控件遍历
			//			}

			// 元字段默认值的处理
			String def = f.getStr("defaulter");
			if (!xx.isEmpty(def)) {
				if (def.equalsIgnoreCase("now()") || def.equalsIgnoreCase("NOW") || def.equalsIgnoreCase("CURRENT_TIMESTAMP") || def.equalsIgnoreCase("SYSDATE") || def.startsWith("0000-")) {
					f.set("defaulter", TimestampUtil.getNow());
				} else if (def.equalsIgnoreCase("UUID")) {
					f.set("defaulter", UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
				} else {
					f.set("defaulter", DynamicParse.buildSql(f.getStr("defaulter"), user));
				}
			}
			
			// 角色字段授权
			Map<String, Set<String>> authFields = EovaConfig.getAuthFields();
			if (!xx.isEmpty(authFields) && user != null) {
				Integer rid = user.getInt("rid");
				String field = objectCode + "." + en;
				Set<String> set = authFields.get(field);
				if (!xx.isEmpty(set) && !set.contains(rid.toString())) {
					f.set("is_query", false);
					f.set("is_show", false);
					f.set("add_status", 20);
					f.set("update_status", 20);
					// 没权限的按钮,不能禁用,只能隐藏,比如走默认值的字段,禁用会出现其它角色编辑持久化缺字段异常
				}
			}
			
		}
	}

	/**
	 * 更新原字段归属表
	 * 
	 * @param objectCode 对象编码
	 * @param en 字段名
	 * @param tableName 字段归属表
	 * @return
	 */
	public int updateTableNameByCode(String objectCode, String en, String tableName) {
		return Db.use(xx.DS_EOVA).update("update eova_field set table_name = ? where object_code = ? and en = ?", tableName, objectCode, en);
	}

	/**
	 * 更改排序
	 * 
	 * @param s 原字段
	 * @param t 目标字段
	 */
	public void updateOrderNum(String objectCode, String sen, String ten) {
		MetaField t = MetaField.dao.getByObjectCodeAndEn(objectCode, ten);
		Db.use(xx.DS_EOVA).update("update eova_field set order_num = ? where object_code = ? and en = ?", t.getInt("order_num") + 1, objectCode, sen);
	}

}