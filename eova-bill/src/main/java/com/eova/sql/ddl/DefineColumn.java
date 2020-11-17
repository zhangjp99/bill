/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.sql.ddl;

public class DefineColumn {

	private String cn = "";
	private String en;
	private String type;
	private int size;
	private int decimal = 0;
	private String defaultValue = null;
	private boolean isNull = true;
	private boolean isAuto = false;
	//	private boolean isPrimary = false;

	public DefineColumn(String en, String type, int size, int decimal, String cn) {
		super();
		this.en = en;
		this.cn = cn;
		this.type = type;
		this.size = size;
		this.decimal = decimal;
	}

	//	public DbColumn isPrimary() {
	//		this.isPrimary = true;
	//		return this;
	//	}
	//
	public DefineColumn auto() {
		this.isAuto = true;
		return this;
	}

	public DefineColumn notNull() {
		this.isNull = false;
		return this;
	}

	public DefineColumn setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}


	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getType() {
		return type.toUpperCase();
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDecimal() {
		return decimal;
	}

	public void setDecimal(int decimal) {
		this.decimal = decimal;
	}

	public boolean isNull() {
		return isNull;
	}

	public String getDefault() {
		return defaultValue;
	}

	public boolean isAuto() {
		return isAuto;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

}