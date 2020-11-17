/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.template.singletree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eova.model.Button;
import com.eova.template.common.config.TemplateConfig;
import com.eova.template.common.util.TemplateUtil;
import com.eova.template.single.SingleTemplate;

public class SingleTreeTemplate extends SingleTemplate {

	@Override
	public String name() {
		return "单表树";
	}

	@Override
	public String code() {
		return TemplateConfig.SINGLE_TREE;
	}

	@Override
	public Map<Integer, List<Button>> getBtnMap() {
		Map<Integer, List<Button>> btnMap = new HashMap<>();

		{
			List<Button> btns = new ArrayList<>();
			btns.add(TemplateUtil.getQueryButton());
			btns.add(new Button("新增", "/eova/template/singletree/btn/add.html", false));
			btns.add(new Button("修改", "/eova/template/singletree/btn/update.html", false));
			btns.add(new Button("删除", "/eova/template/singletree/btn/delete.html", false));
			// btns.add(new Button("查看", "/eova/template/singletree/btn/detail.html", false)); 废弃鸡肋的查看按钮
			btns.add(new Button("隐藏", "/eova/template/singletree/btn/hide.html", true));
			
			btnMap.put(0, btns);
		}

		return btnMap;
	}

}