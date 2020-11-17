/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.i18n;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.eova.common.utils.xx;
import com.eova.common.utils.util.RegexUtil;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 国际化构建器
 * 
 * @author Jieven
 *
 */

public class I18NBuilder {
	
	private static HashMap<String, I18N> i18nMap = new HashMap<>();
	private static ThreadLocal<String> local = new ThreadLocal<>();
	
	public static void init(List<Record> list) {
		for (Record e : list) {
			String code = e.getStr("code");
			I18N i18n = i18nMap.get(code);
			if (i18n == null) {
				i18n = new I18N();
				i18nMap.put(code, i18n);
			}
			i18n.put(e.getStr("txt"), e.getStr("val"));
		}
	}
	
	public static String getLocal() {
		return local.get();
	}

	public static void setLocal(String code){
		local.set(code);
	}

	public static String get(String txt){
		if (xx.isEmpty(txt)) {
			return "";
		}
		if (i18nMap == null) {
			return txt;
		}
		I18N i18n = i18nMap.get(local.get());
		if (i18n == null) {
			return txt;
		}
		String s = i18n.get(txt);
		if (s.isEmpty()) {
			return txt;
		}
		return s;
	}
	
	public static void models(List<? extends Model> ms, String... fileds) {
		if (i18nMap.isEmpty())
			return;
		for (Model m : ms) {
			model(m, fileds);
		}
	}

	public static void model(Model m, String... fileds) {
		if (i18nMap.isEmpty() || m == null)
			return;
		for (String filed : fileds) {
			String s = get(m.getStr(filed));
			if (!xx.isEmpty(s)) {
				m.set(filed, s);
			}
		}
	}

	public static void records(List<Record> rs, String... fileds) {
		if (i18nMap.isEmpty())
			return;
		for (Record m : rs) {
			record(m, fileds);
		}
	}

	public static void record(Record e, String... fileds) {
		if (i18nMap.isEmpty() || e == null)
			return;
		for (String filed : fileds) {
			String s = get(e.getStr(filed));
			if (!xx.isEmpty(s)) {
				e.set(filed, s);
			}
		}
	}

	/**
	 * 文案混杂，提取中文词分别翻译
	 * @param str
	 */
	public static String blend(String str) {
		if (xx.isEmpty(str))
			return "";
		HashSet<String> cns = RegexUtil.getChinese(str);
		for (String cn : cns) {
			String s = get(cn);
			str = str.replaceAll(cn, s);
		}
		return str;
	}

}