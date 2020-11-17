/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.ext.beetl;
import java.io.IOException;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.AllowAllMatcher;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.CompositeResourceLoader;
import org.beetl.core.resource.StartsWithMatcher;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.jfinal3.JFinal3BeetlRender;

import com.jfinal.kit.PathKit;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;

public class BeetlEovaRenderFactory extends RenderFactory {
	public  GroupTemplate groupTemplate = null;
	
	public Render getRender(String view) {
		return new JFinal3BeetlRender(groupTemplate,view);
	}
	
	public void config(boolean webappMode) {
		
		if (groupTemplate != null)
		{
			groupTemplate.close();
		}
				
		try
		{
			String root = PathKit.getWebRootPath();
			WebAppResourceLoader webappLoader = new WebAppResourceLoader(root);

			// 组合加载器, Eova资源通过class loader加载
			CompositeResourceLoader loaders = new CompositeResourceLoader();

			StartsWithMatcher startsWithMatcher = new StartsWithMatcher("/eova");
			startsWithMatcher.setWithPrefix(true);// 保留完整模版路径
			loaders.addResourceLoader(startsWithMatcher, new ClasspathResourceLoader("webapp"));

			loaders.addResourceLoader(new AllowAllMatcher(), webappLoader);

			Configuration cfg = Configuration.defaultConfiguration();
			if (webappMode) {
				// 仅从/src/main/webapp 加载模版
				groupTemplate = new GroupTemplate(webappLoader, cfg);
			} else {
				// 混合加载
				groupTemplate = new GroupTemplate(loaders, cfg);
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException("加载GroupTemplate失败", e);
		}
	}
	
	
}