/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.sql.dql;

import java.text.MessageFormat;

/**
 * SQL表相关信息
 * 
 * @author Jieven
 * 
 */
public class TableSource {

	private String table;
	private String alias;

	private String leftField;
	private String leftAlias;

	private String rigthField;
	private String rigthAlias;

	// private Record data;
	
	public String getAlias() {
		if (alias == null) {
			return table;
		}
		return alias;
	}
	
	/**
	 * 获得当前表的关联字段
	 */
	public String getField(){
		if (leftAlias.equals(alias)) {
			return leftField;
		}
		return rigthField;
	}

	public String toString() {
		return MessageFormat.format("{0} as {1} , condition {2}.{3} = {4}.{5}", table, alias, leftAlias, leftField, rigthAlias, rigthField);
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getLeftField() {
		return leftField;
	}

	public void setLeftField(String leftField) {
		this.leftField = leftField;
	}

	public String getLeftAlias() {
		return leftAlias;
	}

	public void setLeftAlias(String leftAlias) {
		this.leftAlias = leftAlias;
	}

	public String getRigthField() {
		return rigthField;
	}

	public void setRigthField(String rigthField) {
		this.rigthField = rigthField;
	}

	public String getRigthAlias() {
		return rigthAlias;
	}

	public void setRigthAlias(String rigthAlias) {
		this.rigthAlias = rigthAlias;
	}
	

}