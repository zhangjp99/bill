/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.model;

/**
 * 元字段配置
 * 
 * @author Jieven
 *
 */
public class MetaFieldConfig {
	
	// 文件保存目录
	private String filedir;
	// 固定文件名(带后缀,会自动覆盖同名文件)
	private String filename;
	// 备注
	private String memo;
	// 图片宽度(多图)
	private String width;
	// 图片高度(多图)
	private String height;

	// 自定义选项
	private String items;

	// 是否进行合计
	private boolean totalRow = false;

	public String getFiledir() {
		return filedir;
	}

	public void setFiledir(String filedir) {
		this.filedir = filedir;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public boolean isTotalRow() {
		return totalRow;
	}

	public void setTotalRow(boolean totalRow) {
		this.totalRow = totalRow;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

}