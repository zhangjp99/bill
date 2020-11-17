/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.widget.tree;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

/**
 * Tree Node VO
 *
 * @author Jieven
 * @date 2014-9-8
 */
public class TreeNode extends Record {

	private static final long serialVersionUID = -5190761342805087001L;
	
	// 子节点
	private List<TreeNode> childs;

	public List<TreeNode> getChildList() {
		return childs;
	}

	public void setChildList(List<TreeNode> childList) {
		this.childs = childList;
	}

}