/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.mod;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.eova.model.Mod;

public class EovaModClassLoader extends URLClassLoader {

	private List<String> classNameList;

	public EovaModClassLoader(Mod mod) throws IOException {
		super(new URL[] {}, Thread.currentThread().getContextClassLoader());

		String path = String.format("%s%s-%s.jar", EovaModConst.DIR_MOD, mod.getGroup(), mod.getCode());
		File jar = new File(path);

		this.addURL(jar.toURI().toURL());
		this.classNameList = new ArrayList<>();
		// this.initClassNameList(jar);
	}

	public List<String> getClassNameList() {
		return classNameList;
	}

	@SuppressWarnings({ "resource", "unused" })
	private void initClassNameList(File jar) throws IOException {
		Enumeration<JarEntry> entries = new JarFile(jar).entries();
		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			String entryName = jarEntry.getName();
			if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
				String className = entryName.replace("/", ".").substring(0, entryName.length() - 6);
				classNameList.add(className);
				System.out.println(className);
			}
		}
	}
}