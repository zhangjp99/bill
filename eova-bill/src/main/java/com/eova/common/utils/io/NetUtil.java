/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.common.utils.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * 网络文件操作工具类
 * 
 * @author Jieven
 * 
 */
public class NetUtil {

	/**
	 * 异步从URL下载文件
	 * 
	 * @param url 目标URL
	 * @param file 输出文件
	 * @throws IOException
	 */
	public static void downloadAsync(String url, File file) throws IOException {
		InputStream inputStream = new URL(url).openStream();
		ReadableByteChannel rbc = Channels.newChannel(inputStream);
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, 1 << 24);
		fos.close();
	}

	/**
	 * 从URL下载文件
	 * 
	 * @param url 目标URL
	 * @param path 输出文件
	 * @throws IOException
	 */
	public static void download(String url, String path) throws Exception {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new URL(url).openStream();
			byte[] bs = new byte[1024];
			int len;
			os = new FileOutputStream(path);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				is.close();
				os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}