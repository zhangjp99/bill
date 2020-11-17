/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.vo;

import java.io.Serializable;

/**
 * 键值对
 * @author Jieven
 *
 */
public class KeyVal implements Serializable {

	private static final long serialVersionUID = -9095802999489821104L;

	private Object key;

	private Object val;

	private Object txt;

	public KeyVal() {
	}

	public KeyVal(Object key, Object val) {
		super();
		this.key = key;
		this.val = val;
	}

	public KeyVal(Object key, Object val, Object txt) {
		super();
		this.key = key;
		this.val = val;
		this.txt = txt;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getVal() {
		return val;
	}

	public void setVal(Object val) {
		this.val = val;
	}

	public Object getTxt() {
		return txt;
	}

	public void setTxt(Object txt) {
		this.txt = txt;
	}

	@Override
	public String toString() {
		return "[key=" + key + ", value=" + val + "]";
	}

}