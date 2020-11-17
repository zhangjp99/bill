package com.oss.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间类
 * 
 * @author wjzhu
 *
 */
public class DateUtility {
	public static volatile String dateStr = "";

	public static synchronized String getDateHours() {
		if (dateStr == null || dateStr.isEmpty()) {
			try {
				SimpleDateFormat dft = new SimpleDateFormat("yyyyMMddHH");
				Calendar date = Calendar.getInstance();
				dateStr = dft.format(date.getTime());
			} catch (Exception e) {
				dateStr = "";
			}
		}
		return dateStr;
	}

	public static synchronized String getDateHours(String datetimes) {
		Date date;
		if (dateStr == null || dateStr.isEmpty()) {
			try {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetimes);
				String result = new SimpleDateFormat("yyyyMMddHH").format(date);
				dateStr = result;
			} catch (Exception e) {
				dateStr = "";
			}
		}
		return dateStr;
		/*
		 * Date date; try { date = new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetimes); String
		 * result=new SimpleDateFormat("yyyyMMddHH").format(date); //
		 * System.out.println("【获取服务器时间戳并转换】："+result); return result; } catch
		 * (ParseException e) {
		 * 
		 * e.printStackTrace(); } return "";
		 */
	}

}
