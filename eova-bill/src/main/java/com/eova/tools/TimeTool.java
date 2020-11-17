/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class TimeTool {

	private static String TIME = "yyyy-MM-dd HH:mm:ss";
	private static String DATE = "yyyy-MM-dd";
	
	/**
	 * 	获得当前毫秒数1
	 * @return
	 */
	public long now() {
		return System.currentTimeMillis();
	}
	
	/**
	 * 毫秒格式化
	 * @param pattern 格式
	 * @param ms 毫秒数
	 * @return
	 */
	public String formatMS(long ms, String pattern) {
		return new SimpleDateFormat(pattern).format(new Date(ms));
	}
	
	/**
	 * 毫秒格式化(yyyy-MM-dd)
	 * @param ms 毫秒
	 * @return
	 */
	public String formatLongDate(long ms) {
		return formatMS(ms, DATE);
	}
	
	/**
	 * 毫秒格式化(yyyy-MM-dd HH:mm:ss)
	 * @param ms 毫秒
	 * @return
	 */
	public String formatLongTime(long ms) {
		return formatMS(ms, TIME);
	}

	/**
	 * 格式化当前时间(自定义规则)
	 * @param pattern
	 * @return
	 */
	public String formatNow(String pattern) {
		return formatMS(now(), pattern);
	}
	
	/**
	 * 当前时间格式化(yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	public String formatNowTime() {
		return formatMS(now(), TIME);
	}
	
	/**
	 * 当前时间格式化(yyyy-MM-dd)
	 * @return
	 */
	public String formatNowDate() {
		return formatMS(now(), DATE);
	}
	
	/**
	 * 时间转毫秒
	 * @param time 时间(yyyy-MM-dd HH:mm:ss)
	 * @return 毫秒
	 */
	public long parse(String time) {
		LocalDateTime time1 = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return LocalDateTime.from(time1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
}