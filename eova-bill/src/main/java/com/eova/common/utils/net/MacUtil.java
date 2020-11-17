/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.utils.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/*
 * 物理地址是48位，别和ipv6搞错了
 */
public class MacUtil {
	public static void main(String[] args) throws UnknownHostException, SocketException {

		InetAddress ia = InetAddress.getLocalHost();
		System.out.println(ia);
		getLocalMac(ia);
	}

	private static void getLocalMac(InetAddress ia) throws SocketException {
		//获取网卡，获取地址
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		System.out.println("mac数组长度：" + mac.length);
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			//字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			System.out.println("每8位:" + str);
			if (str.length() == 1) {
				sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		System.out.println("本机MAC地址:" + sb.toString().toUpperCase());
	}
}