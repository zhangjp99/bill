package com.oss.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * 时间工具类
 * @author ruansijin
 *
 */
public class DateUtils {

	/**
	 * yyyy-MM-dd
	 */
	public static SimpleDateFormat SDF1 = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * yyyy/MM/dd
	 */
	public static SimpleDateFormat SDF2 = new SimpleDateFormat("yyyy/MM/dd");
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static SimpleDateFormat SDF3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * yyMMddHHmmss
	 */
	public static SimpleDateFormat SDF4 = new SimpleDateFormat("yyMMddmmss");
	/**
	 * HH:mm yyyy-MM-dd
	 */
	public static SimpleDateFormat SDF5 = new SimpleDateFormat("HH:mm yyyy-MM-dd");

	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static SimpleDateFormat SDF6 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * yyyyMMdd
	 */
	public static SimpleDateFormat SDF7 = new SimpleDateFormat("yyyyMMdd");

	/**
	 * yyyyMMddHHmmss
	 */
	public static SimpleDateFormat SDF8 = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * yyyy-mm-dd-hh
	 */
	public static SimpleDateFormat SDF9 = new SimpleDateFormat("yyyy-MM-dd-hh");

	/**
	 * hh
	 */
	public static SimpleDateFormat SDF10 = new SimpleDateFormat("hh");

	/**
	 * HH:mm:ss
	 */
	public static SimpleDateFormat SDF11 = new SimpleDateFormat("HH:mm:ss");

	/**
	 * yyyyMMddHHmmss
	 */
	public static SimpleDateFormat SDF12 = new SimpleDateFormat("yyyyMMddHHmm");
	/**
	 * yyyyMMddHHmmss
	 */
	public static SimpleDateFormat SDF13 = new SimpleDateFormat("MM.dd HH:mm:ss");

	/**
	 * 仅比较年月日 相等
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDate(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return false;
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
				&& c1.get(Calendar.DATE) == c2.get(Calendar.DATE);

	}

	/**
	 * 比较年月日 时分秒 相等
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDateTime(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return false;
		}
		if (d1.getTime() == d2.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * getDateAddMinutes:(获取指定时间之后多少分钟的时间).
	 *
	 * @author sid
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date getDateAddMinutes(Date date, int minutes) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.MINUTE, minutes);
		return cl.getTime();
	}

	public static Date getDateAddSeconds(Date date, int second) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.SECOND, second);
		return cl.getTime();
	}
	
	public static Date getDateAddDays(Date date, int second) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.DATE, second);
		return cl.getTime();
	}

	public static Date getDateAddHours(Date date, int hours) {

		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.HOUR, hours);

		return cl.getTime();
	}

	/**
	 * 保留日期 ，把时间设置为 0 <br>
	 * HOUR_OF_DAY<br>
	 * MINUTE<br>
	 * SECOND<br>
	 * MILLISECOND<br>
	 * 
	 * @param d
	 * @return
	 */
	public static Date clearTime(Date d) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(d);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}

	/**
	 * 增加天数(负值为减)
	 * 
	 * @param d
	 * @param dayToAdd
	 * @return
	 */
	public static Date addDay(Date d, int dayToAdd) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(d);
		ca.add(Calendar.DAY_OF_MONTH, dayToAdd);
		return ca.getTime();
	}

	/**
	 * 增加天数(负值为减)
	 * 
	 * @param d
	 * @param dayToAdd
	 * @return
	 */
	public static Date addDay(String d, int dayToAdd) {
		Date date = getYYYY_MM_dd_HH_mm_ssToDate(d);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DAY_OF_MONTH, dayToAdd);
		return ca.getTime();
	}

	/**
	 * 是否为"今天"
	 * 
	 * @param d
	 * @return
	 */
	public static boolean isToday(Date d) {
		return isSameDate(d, new Date());
	}

	public static boolean isToday(String d) {
		return isToday(getYYYY_MM_dd_HH_mm_ssToDate(d));
	}

	/**
	 * 是否是指定日期
	 *
	 * @param date
	 * @param day
	 * @return
	 */
	public static boolean isTheDay(final Date date, final Date day) {
		return date.getTime() >= DateUtils.dayBegin(day).getTime() && date.getTime() <= DateUtils.dayEnd(day).getTime();
	}

	/**
	 * 获取指定时间的那天 00:00:00.000 的时间
	 *
	 * @param date
	 * @return
	 */
	public static Date dayBegin(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取今天 00:00:00.000 的时间
	 *
	 * @param date
	 * @return
	 */
	public static Date dayBegin() {
		return dayBegin(now());
	}

	/**
	 * 获取指定时间的那天 23:59:59.999 的时间
	 *
	 * @param date
	 * @return
	 */
	public static Date dayEnd(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/**
	 * 获取今天 23:59:59.999 的时间
	 *
	 * @param date
	 * @return
	 */
	public static Date dayEnd() {
		return dayEnd(now());
	}

	/**
	 * 获取当前时间
	 *
	 * @return
	 */
	public static Date now() {
		return new Date();
	}

	/**
	 * 取得今天的日期加最小时间
	 * 
	 * @return
	 */
	public static String getTheTodayMin() {
		return getDateToyyyy_MM_dd_HH_mm_ss(getTheDayMin(new Date()));
	}

	/**
	 * 取得今天的日期加最大时间
	 * 
	 * @return
	 */
	public static String getTheTodayMax() {
		return getDateToyyyy_MM_dd_HH_mm_ss(getTheDayMax(new Date()));
	}

	/**
	 * 取得某天的日期加最小时间
	 * 
	 * @return
	 */
	public static Date getTheDayMin(Date d) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String ds = f.format(d) + " 00:00:00";
		try {
			f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return f.parse(ds);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 取得某天的日期加最大时间
	 * 
	 * @return
	 */
	public static Date getTheDayMax(Date d) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String ds = f.format(d) + " 23:59:59";
		try {
			f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return f.parse(ds);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getTheDayMax(String d) {
		Date date = getYYYY_MM_dd_HH_mm_ssToDate(d);
		date = getTheDayMax(date);
		return getDateToyyyy_MM_dd_HH_mm_ss(date);
	}

	public static String getTheDayMin(String d) {
		Date date = getYYYY_MM_dd_HH_mm_ssToDate(d);
		date = getTheDayMin(date);
		return getDateToyyyy_MM_dd_HH_mm_ss(date);
	}

	/**
	 * 
	 * 将字符串格式的日期转换为Date型的日期
	 * <p>
	 * modify by wjz 0326
	 * <p>
	 * 考虑到日期格式比较复杂，在转换之前先做如下假定：
	 * <p>
	 * 都是按照年－月－日的格式排列
	 * <p>
	 * 年都是4位
	 * <p>
	 * strToDate:
	 *
	 * @author sid
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		if (strDate == null || strDate.length() < 6) {
			throw new IllegalArgumentException("illeage date format");
		}
		String fmt = "yyyy-MM-dd HH:mm:ss";
		if (strDate.length() == 19) {
			if (strDate.indexOf("-") > 0) {
				fmt = "yyyy-MM-dd HH:mm:ss";
			} else if (strDate.indexOf("/") > 0) {
				fmt = "yyyy/MM/dd HH:mm:ss";
			}
		} else if (strDate.length() == 18) {
			if (strDate.indexOf("-") > 0) {
				fmt = "yyyy-MM-ddHH:mm:ss";
			} else if (strDate.indexOf("/") > 0) {
				fmt = "yyyy/MM/ddHH:mm:ss";
			}
		} else if (strDate.length() == 16) {
			if (strDate.indexOf("-") > 0) {
				fmt = "yyyy-MM-dd HH:mm";
			} else if (strDate.indexOf("/") > 0) {
				fmt = "yyyy/MM/dd HH:mm";
			}
		} else if (strDate.length() == 14) {

			fmt = "yyyyMMddHHmmss";
		} else if (strDate.length() == 10) {
			if (strDate.indexOf("-") > 0) {
				fmt = "yyyy-MM-dd";
			} else if (strDate.indexOf("/") > 0) {
				fmt = "yyyy/MM/dd";
			} else if (strDate.indexOf(".") > 0) {
				fmt = "yyyy.MM.dd";
			}
		} else if (strDate.length() == 8) {
			if (strDate.indexOf("-") > 0) {
				fmt = "yy-MM-dd";
			} else if (strDate.indexOf("/") > 0) {
				fmt = "yy/MM/dd";
			} else if (strDate.indexOf(".") > 0) {
				fmt = "yy.MM.dd";
			} else {
				fmt = "yyyyMMdd";
			}

		}

		SimpleDateFormat formatter = new SimpleDateFormat(fmt);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		if (date == null) {
			return "";
		}
		if (format == null) {
			format = "yyyy-MM-dd hh:mm:ss";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 日期转字符串 12小时制
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	/**
	 * add by Bill 2011-07-07 格式化为yyyyMMddHHmmss的形式
	 * 
	 * @param datestr
	 * @return
	 */
	public static String dateToString() {
		return SDF4.format(new Date());
	}

	/**
	 * add by Bill 日期转字符串 24小时制
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date24ToString(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	public static String dataToStringYMD() {
		return SDF7.format(new Date());
	}

	/**
	 * 日期转时间戳
	 * 
	 * @param date
	 * @return
	 */
	public static long dateToTimeMillis(Date date) {
		if (date == null) {
			return 0;
		}
		return date.getTime() / 1000;
	}

	/**
	 * add by Bill 2011-07-07
	 * 
	 * @param datestr
	 * @return
	 */
	public static Date StringToDate(String datestr) {
		Date dt = null;
		if (datestr == null || "".equals(datestr)) {
			dt = new Date();
		}
		try {
			dt = SDF3.parse(datestr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dt;
	}

	public static Date StringToDateTime(String datestr) {
		Date dt = null;
		if (datestr == null || "".equals(datestr)) {
			dt = new Date();
		}
		try {
			dt = SDF11.parse(datestr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dt;
	}

	public static Date String2Date(String datestr) {
		Date dt = null;
		if (datestr == null || "".equals(datestr)) {
			dt = new Date();
		}
		try {
			dt = SDF6.parse(datestr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dt;
	}

	public static boolean getIsSaleBegin() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 0);
		Date date = DateUtils.clearTime(calendar.getTime());
		try {
			if (date.compareTo(DateUtils.SDF3.parse("2012-10-24 00:00:00")) == 0) {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * public static void main(String [] args){ //System.out.println(clearTime(new
	 * Date()));
	 * 
	 * //System.out.println(StringToDate(dateToString(new Date())));
	 * //System.out.println(date24ToString(new Date()));
	 * 
	 * System.out.println(dateToString());
	 * 
	 * try { System.out.println(DateUtils.SDF3.toPattern());
	 * 
	 * System.out.println("getIsSaleBegin=="+getIsSaleBegin());
	 * 
	 * System.out.println(DateUtils.dateToTimeMillis(new Date())>
	 * DateUtils.dateToTimeMillis(DateUtils.SDF3.parse("2012-08-1 23:59:59"))); }
	 * catch (ParseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 */
	/**
	 * 返回自定义格式的日期
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateFormatstr(Date date, String format) {
		String rstr = "";
		try {
			SimpleDateFormat df = new SimpleDateFormat(format);
			rstr = df.format(date);
		} catch (Exception e) {
			return "";
		}
		return rstr;
	}

	/**
	 * 获取当天日期
	 * 
	 * @return
	 */
	public static String getTodayYYYYMMDD() {
		return getDateToyyyy_MM_dd(new Date());
	}

	/**
	 * 返回yyyy-MM-dd:HH:mm:ss格式的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateToyyyy_MM_dd_HH_mm_ss(Date date) {
		return getDateFormatstr(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 返回yyyyMMdd格式的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateToyyyyMMdd(Date date) {
		return getDateFormatstr(date, "yyyyMMdd");
	}

	/**
	 * 返回yyyy-MM-dd格式的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateToyyyy_MM_dd(Date date) {
		return getDateFormatstr(date, "yyyy-MM-dd");
	}

	/**
	 * 返回yyyy-MM-dd格式的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateToyyyy_MM(Date date) {
		return getDateFormatstr(date, "yyyy-MM");
	}

	/**
	 * 返回HH:mm:ss格式的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateToHH_mm_ss(Date date) {
		return getDateFormatstr(date, "HH:mm:ss");
	}

	/**
	 * 返回HH:mm格式的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateToHH_mm(Date date) {
		return getDateFormatstr(date, "HH:mm");
	}

	/**
	 * 返回HHmmss格式的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateToHHmmss(Date date) {
		return getDateFormatstr(date, "HHmmss");
	}

	/**
	 * 根据yyyy-MM-dd HH:mm:ss返回日期类型
	 * 
	 * @param dateStr
	 * @return Date 日期类型
	 */
	public static Date getYYYY_MM_dd_HH_mm_ssToDate(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getYYYY_MM_dd_HH_mm_ssToYYYY_MM_dd(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getDateToyyyy_MM_dd(date);
	}

	/**
	 * 日期互转 yyyy-MM-dd HH:mm:ss 转换为 HH:mm
	 * 
	 * @param dateStr
	 * @return String HH:mm
	 */
	public static String getDateStrToHH_mm(String dateStr) {
		Date date = getYYYY_MM_dd_HH_mm_ssToDate(dateStr);
		return getDateFormatstr(date, "HH:mm");
	}

	/**
	 * 时长处理方法 传入开日期，和时长，转换为结束日期 如：传入2015-12-12 13:23:21 加上分钟转换为 2015-12-12 14:23:21
	 * 
	 * @param 开始日期
	 * @param 时长   return 结束日期
	 */
	public static Date getEndDate(Date startDate, int time) {
		Date endDate = new Date(startDate.getTime() + time * 60 * 1000);
		return endDate;
	}

	/**
	 * 时长处理方法 传入开日期，和时长，转换为结束日期 如：传入2015-12-12 13:23:21 加上秒转换为 2015-12-12 14:23:21
	 * 
	 * @param 开始日期
	 * @param 时长   return 结束日期
	 */
	public static Date getEndDateSecond(Date startDate, int time) {
		Date endDate = new Date(startDate.getTime() + time * 1000);
		return endDate;
	}

	/**
	 * 时长处理方法 传入开始日期，和时长，返回结束日期 如：传入2015-12-12 13:23:21 加上60分钟转换为 2015-12-12
	 * 14:23:21
	 * 
	 * @param startDateStr 开始日期字符串，格式：2015-12-12 13:23:21
	 * @param time         时长
	 * @param              return 结束日期字符串
	 */

	public static String getEndDateStr(String startDateStr, int time) {
		Date startDate = getYYYY_MM_dd_HH_mm_ssToDate(startDateStr);
		Date endDate = getEndDate(startDate, time);
		return getDateToyyyy_MM_dd_HH_mm_ss(endDate);
	}

	/**
	 * 判断当前时间是否在区间内
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean between(Date startTime, Date endTime) {
		long currentTime = System.currentTimeMillis();
		if (currentTime > startTime.getTime() && currentTime < endTime.getTime()) {
			return true;
		}
		return false;
	}

	/**
	 * 获取总耗时
	 * 
	 * @param start 开时时间System.currentTimeMillis();
	 * @return
	 */
	public static long getTotalTime(long start) {
		return System.currentTimeMillis() - start;
	}

	/**
	 * by yaoxing 计算出离当前日期datas天的日期,若datas小于0表示当前日期之前datas天，若datas大于0表当前日期之后datas天
	 * 参数传值参考：30（后推30天）,-30（后推30天）
	 * 
	 * @param 要计算的天数
	 * @return 得到日期
	 */
	public static Date getComputerDate(int datas) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(GregorianCalendar.DATE, datas);
		String begin = new java.sql.Date(calendar.getTime().getTime()).toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = null;
		try {
			beginDate = sdf.parse(begin);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return beginDate;
	}

	/**
	 * by yaoxing 计算出离当前日期datas天的日期,若datas小于0表示当前日期之前datas天，若datas大于0表当前日期之后datas天
	 * 参数传值参考：30（后推30天）,-30（后推30天）
	 * 
	 * @param 要计算的天数
	 * @return 得到日期
	 */
	public static Date getComputerDate(Date date, int datas) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(GregorianCalendar.DATE, datas);
		String begin = new java.sql.Date(date.getTime()).toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = null;
		try {
			beginDate = sdf.parse(begin);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return beginDate;
	}

	/**
	 * by yaoxing 计算出离当前日期datas天的日期,若datas小于0表示当前日期之前datas天，若datas大于0表当前日期之后datas天
	 * 参数传值参考：30（后推30天）,-30（后推30天）
	 * 
	 * @param 要计算的天数
	 * @return 得到日期字符串，如20140728
	 */
	public static String getComputeFormatDate(int datas) {
		Date date = getComputerDate(datas);
		String dateStr = getDateToyyyy_MM_dd(date);
		return dateStr + " 00:00:00";
	}

	/**
	 * 获取当天的时间
	 * 
	 * @return
	 */
	public static String getStartFormatToday() {
		return getTodayYYYYMMDD() + " 00:00:00";
	}

	/**
	 * 获取当天的时间
	 * 
	 * @return
	 */
	public static String getCurrentFromtDate() {
		return getDateToyyyy_MM_dd_HH_mm_ss(new Date());
	}

	/**
	 * 获取当天的时间
	 * 
	 * @return
	 */
	public static String getCurrentFromtDateTime() {
		return getDateToyyyyMMddHHmmss(new Date());
	}

	private static String getDateToyyyyMMddHHmmss(Date date) {
		return getDateFormatstr(date, "yyyyMMddHHmmss");
	}

	/**
	 * 获取之前一天的最小时间
	 * 
	 * @return
	 */
	public static String getTheDayBeforeMin() {
		return getDateBeforeMin(1);
	}

	/**
	 * 获取7天前的最小时间
	 * 
	 * @return
	 */
	public static String getSevenDateBeforeMin() {
		return getDateBeforeMin(7);
	}

	/**
	 * 获取30天前的最小时间
	 * 
	 * @return
	 */
	public static String getThirtyDateBeforeMin() {
		return getDateBeforeMin(30);
	}

	/**
	 * 获取之前一天的最大时间
	 * 
	 * @return
	 */
	public static String getTheDayBeforeMax() {
		return getDateBeforeMax(1);
	}

	/**
	 * 获取7天前的最大时间
	 * 
	 * @return
	 */
	public static String getSevenDateBeforeMax() {
		return getDateBeforeMax(7);
	}

	/**
	 * 获取30天前的最大时间
	 * 
	 * @return
	 */
	public static String getThirtyDateBeforeMax() {
		return getDateBeforeMax(30);
	}

	/**
	 * 获取一天后的最小时间
	 * 
	 * @return
	 */
	public static String getTheDayAfterMin() {
		return getDateAfterMin(1);
	}

	/**
	 * 获取7天后的最小时间
	 * 
	 * @return
	 */
	public static String getSevenDateAfterMin() {
		return getDateAfterMin(7);
	}

	/**
	 * 获取30天后的最小时间
	 * 
	 * @return
	 */
	public static String getThirtyDateAfterMin() {
		return getDateAfterMin(30);
	}

	/**
	 * 获取一天后的最大时间
	 * 
	 * @return
	 */
	public static String getTheDayAfterMax() {
		return getDateAfterMax(1);
	}

	/**
	 * 获取7天后的最大时间
	 * 
	 * @return
	 */
	public static String getSevenDateAfterMax() {
		return getDateAfterMax(7);
	}

	/**
	 * 获取30天后的最大时间
	 * 
	 * @return
	 */
	public static String getThirtyDateAfterMax() {
		return getDateAfterMax(30);
	}

	/**
	 * 获取一个月后最大时间
	 * 
	 * @param date
	 * @param i
	 * @return
	 * @throws ParseException
	 */
	public static String getMonthDateAfterMax(String date, int i) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date p1 = df.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(p1);
		cal.add(Calendar.MONTH, i);
		return getDateToyyyy_MM_dd_HH_mm_ss(DateUtils.getTheDayMin(cal.getTime()));
	}

	/**
	 * 获取一个月后的时间
	 * 
	 * @param date
	 * @param i
	 * @return
	 * @throws ParseException
	 */
	public static Date getMonthDateAfter(int i) throws ParseException {
		Date p1 = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(p1);
		cal.add(Calendar.MONTH, i);
		return cal.getTime();
	}

	/**
	 * 获取一年后最大时间
	 * 
	 * @param date
	 * @param i
	 * @return
	 * @throws ParseException
	 */
	public static String getYearDateAfterMax(String date, int i) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date p1 = df.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(p1);
		cal.add(Calendar.YEAR, i);
		return getDateToyyyy_MM_dd_HH_mm_ss(DateUtils.getTheDayMax(cal.getTime()));
	}

	/**
	 * 获取一年后最大时间
	 * 
	 * @param date
	 * @param i
	 * @return
	 * @throws ParseException
	 */
	public static Date getYearDateAfter(int i) throws ParseException {
		Date p1 = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(p1);
		cal.add(Calendar.YEAR, i);
		return cal.getTime();
	}

	/**
	 * 获取指定天数前的最小日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getDateBeforeMin(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -days);
		return (getDateToyyyy_MM_dd_HH_mm_ss(getTheDayMin(cal.getTime())));
	}

	/**
	 * 获取指定天数前的最大日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getDateBeforeMax(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -days);
		return (getDateToyyyy_MM_dd_HH_mm_ss(getTheDayMax(cal.getTime())));
	}

	/**
	 * 获取指定天数后的最小日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getDateAfterMin(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, days);
		return (getDateToyyyy_MM_dd_HH_mm_ss(getTheDayMin(cal.getTime())));
	}

	/**
	 * 获取指定天数后的最大日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getDateAfterMax(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, days);
		return (getDateToyyyy_MM_dd_HH_mm_ss(getTheDayMax(cal.getTime())));
	}

	/**
	 * 获取本周第一天最小的日期
	 * 
	 * @return yyyy_MM_dd
	 */
	public static String getWeekFirstDayMin() {
		Calendar cal = Calendar.getInstance();
		// 获取本周一的日期
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		if (DateUtils.getTodayWeek() == 7) {
			cal.add(Calendar.WEEK_OF_YEAR, -1);
		}
		return (getDateToyyyy_MM_dd_HH_mm_ss(getTheDayMin(cal.getTime())));
	}

	/**
	 * 获取本周最后一天最大的日期
	 * 
	 * @return yyyy_MM_dd
	 */
	public static String getWeekLastDayMax() {
		Calendar cal = Calendar.getInstance();
		// 获取本周日的日期
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		if (DateUtils.getTodayWeek() != 7) {
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		}
		return (getDateToyyyy_MM_dd_HH_mm_ss(getTheDayMax(cal.getTime())));
	}

	/**
	 * 获取本周第一天的日期
	 * 
	 * @return yyyy_MM_dd
	 */
	public static String getWeekFirstDay() {
		Calendar cal = Calendar.getInstance();
		// 获取本周一的日期
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return (getDateToyyyy_MM_dd(cal.getTime()));
	}

	/**
	 * 获取本周最后一天的日期
	 * 
	 * @return yyyy_MM_dd
	 */
	public static String getWeekLastDay() {
		Calendar cal = Calendar.getInstance();
		// 获取本周日的日期
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		return (getDateToyyyy_MM_dd(cal.getTime()));
	}

	/**
	 * 获取本月第一天的日期
	 * 
	 * @return yyyy_MM_dd
	 */
	public static String getMonFirstDay() {
		Calendar cal = Calendar.getInstance();
		// 获取本月第一天的日期
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return (getDateToyyyy_MM_dd(cal.getTime()));
	}

	/**
	 * 获取本年第一天的日期
	 * 
	 * @return yyyy_MM_dd
	 */
	public static String getYearFirstDay() {
		Calendar cal = Calendar.getInstance();
		// 获取本年第一天的日期
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return (getDateToyyyy_MM_dd(cal.getTime()));
	}

	/**
	 * 和当前时间进行比较
	 * 
	 * @param date1
	 * @param date2
	 * @return 大于当前时间返回true
	 */
	public static boolean compareCurrentDate(Date date) {
		Date currentFromtDate = new Date();
		try {
			if (date.getTime() > currentFromtDate.getTime()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	/**
	 * 和当前时间进行比较
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean compareCurrentDate(String date) {
		String currentFromtDate = getCurrentFromtDate();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date dt1 = df.parse(date);
			Date dt2 = df.parse(currentFromtDate);
			if (dt1.getTime() > dt2.getTime()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	/**
	 * 比较两个日期大小，date1大于date2,则返回true
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean compareDate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	public static boolean compareDate(Date date1, Date date2) {
		try {
			Date dt1 = date1;
			Date dt2 = date2;
			if (dt1.getTime() > dt2.getTime()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	/**
	 * 返回某个时间点多少天后的时间点
	 * 
	 * @param morningEndTime
	 * @return yyyy-MM-dd hh:mm
	 */
	public static String getDayNewTime(String time, int day) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date dt1 = df.parse(time);
			long newTime = dt1.getTime() + 1000L * 3600 * 24 * day;
			Date newDate = new Date(newTime);
			return df.format(newDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 给时间加上几个小时
	 * 
	 * @param day  当前时间 格式：yyyy-MM-dd HH:mm:ss
	 * @param hour 需要加的时间
	 * @return
	 */
	public static String addDateMinut(String day, int hour) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null) {
			return "";
		}
		// System.out.println("front:" + format.format(date)); //显示输入的日期
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 24小时制
		cal.add(Calendar.HOUR, hour);
		date = cal.getTime();
		// System.out.println("after:" + format.format(date)); //显示更新后的日期
		cal = null;
		return format.format(date);
	}

	/**
	 * 给时间加上几个小时
	 * 
	 * @param day  当前时间 格式：yyyy-MM-dd HH:mm:ss
	 * @param hour 需要加的时间
	 * @return
	 */
	public static String addDateMinuts(String day, int minuts) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null) {
			return "";
		}
		// System.out.println("front:" + format.format(date)); //显示输入的日期
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// cal.get(Calendar.HOUR)
		// 24小时制
		cal.add(Calendar.MINUTE, minuts);
		date = cal.getTime();
		// System.out.println("after:" + format.format(date)); //显示更新后的日期
		cal = null;
		return format.format(date);
	}

	public static String getAgeByBirthday(String birthDay) throws Exception {
		if (birthDay.length() == 10) {
			birthDay = birthDay + " 00:00:00";
		}
		int age = getAge(getYYYY_MM_dd_HH_mm_ssToDate(birthDay));
		if (age < 0) {
			age = 0;
		}
		return age + "";
	}

	/**
	 * 日期转换年龄
	 * 
	 * @param birthDay
	 * @return
	 * @throws Exception
	 */
	public static int getAge(Date birthDay) throws Exception {
		// 获取当前系统时间
		Calendar cal = Calendar.getInstance();
		// 如果出生日期大于当前时间，则抛出异常
		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}
		// 取出系统当前时间的年、月、日部分
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		// 将日期设置为出生日期
		cal.setTime(birthDay);
		// 取出出生日期的年、月、日部分
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		// 当前年份与出生年份相减，初步计算年龄
		int age = yearNow - yearBirth;
		// 当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
		if (monthNow <= monthBirth) {
			// 如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周岁
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}

	/**
	 * 判断时间是否在时间段内
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean isDateSectionByString(String start, String end) {
		return isDateSection(getYYYY_MM_dd_HH_mm_ssToDate(start), getYYYY_MM_dd_HH_mm_ssToDate(end), new Date());

	}
	

	/**
	 * 判断时间是否在时间段内
	 * 
	 * @param start
	 * @param end
	 * @param newDate
	 * @return
	 */
	public static boolean isDateSection(Date start, Date end, Date newDate) {
		if (newDate.getTime() >= start.getTime() && newDate.getTime() <= end.getTime()) {
			return true;
		}
		return false;
	}

	/**
	 * 获取今天星期几
	 * 
	 * @return
	 */
	public static int getTodayWeek() {
		Calendar now = Calendar.getInstance();
		// 一周第一天是否为星期天
		boolean isFirstSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
		// 获取周几
		int weekDay = now.get(Calendar.DAY_OF_WEEK);
		// 若一周第一天为星期天，则-1
		if (isFirstSunday) {
			weekDay = weekDay - 1;
			if (weekDay == 0) {
				weekDay = 7;
			}
		}
		// 打印周几
		return weekDay;
	}

	/**
	 * 获取两个日期相减后的日期数
	 * 
	 * @param first_date
	 * @param tow_date
	 * @return
	 * @throws ParseException
	 */
	public static int getSubDate(String first_date, String tow_date) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date f = df.parse(first_date);
		Date t = df.parse(tow_date);
		long time = f.getTime() - t.getTime();
		long d = time / (1000 * 60 * 60 * 24);
		return (int) d;
	}

	public static int getSubSecond(String first_date, String tow_date) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date f = df.parse(first_date);
		Date t = df.parse(tow_date);
		long time = f.getTime() - t.getTime();
		long d = time;
		return (int) d;
	}

	public static String zeroContat(int num) {
		String result = "";
		if (num < 10) {
			result = "-0" + num;
		} else {
			result = "-" + num;
		}
		return result;
	}

	public static String[] getMonthDays(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = new GregorianCalendar();
		Date date1 = null;
		try {
			date1 = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 放入你的日期
		calendar.setTime(date1);
		int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		String[] days = new String[actualMaximum];
		for (int i = 0; i < actualMaximum; i++) {
			days[i] = strDate + zeroContat(i + 1);
		}
		return days;
	}

	/**
	 * 两个日期相减 返回秒值, 相减为负数时返回0
	 * 
	 * @param date1
	 * @param date2
	 * @return date1- date2
	 * @author zhoubin
	 * @time 2018年8月14日下午9:25:40
	 *
	 */
	public static int subtractBetweenDate(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("参数错误");
		}
		long second = 0;
		try {
			second = date1.getTime() - date2.getTime() > 0 ? (date1.getTime() - date2.getTime()) / 1000 : 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Integer.parseInt(String.valueOf(second));
	}

	/**
	 * 判断某个日期是否是昨天
	 * 
	 * @param d
	 * @return
	 * @author zhoubin
	 * @time 2018年9月26日下午2:30:50
	 *
	 */
	public static boolean isYesterday(Date d) {
		if (d == null) {
			return false;
		}
		Date yesterday = addDay(new Date(), -1);
		return isSameDate(d, yesterday);
	}

	/**
	 * 将日期转换成某些格式的字符串
	 * 
	 * @param date
	 * @return
	 * @author zhoubin
	 * @time 2018年9月26日下午2:24:10
	 *
	 */
	public static String getFormateDate(Date date) {
		if (date == null) {
			return "";
		}
		// 今天 返回时分秒
		if (isToday(date)) {
			return getDateToHH_mm(date);
		} else if (isYesterday(date)) {
			return "昨天" + getDateToHH_mm(date);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			// 取出系统当前时间的年、月、日部分
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			return year + "年" + month + "月" + dayOfMonth + "日" + getDateToHH_mm(date);
		}
	}

	/**
	 * 根据年月获取该月份最大天数
	 * @param year  年
	 * @param month 月 1-12
	 * @return
	 */
	public static int getMonthMaxDays(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		// 把日期设置为当月第一天
		a.set(Calendar.DATE, 1);
		// 日期回滚一天，也就是最后一天
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 计算两个日期之间的天数 日期格式是yyyy-MM-dd
	 * 
	 * @date 2019年2月25日上午11:59:15
	 * @author zhoub
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int countTwoDate(Date startDate, Date endDate) {
		if (startDate != null && endDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(endDate);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			return Integer.parseInt(String.valueOf(between_days));
		}
		return 0;
	}
	/**
	 * 根据时间计算+-分钟
	 * @param strDate
	 * @param min
	 * @return
	 */
	public static String getFormateAddDate(String strDate,Integer min) {
		Date date = strToDate(strDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, min);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(c.getTime());
	}
	/**
	 * 计算时间差 分钟
	 * 向上取整
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public static Long getMin(String sdate, String edate) {
		Date start = strToDate(sdate);
		Date end = strToDate(edate);
		long between = (end.getTime() - start.getTime());
		long min = between / 60000 + (between % 60000 == 0?0:1);
		return min;
	}
	
	/**
	 * 计算时间差 分钟
	 * 向上取整
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public static Long getMin(Date sdate, Date edate) {
		Date start = sdate;
		Date end = edate;
		long between = (end.getTime() - start.getTime());
		long min = between / 60000 + (between % 60000 == 0?0:1);
		return min;
	}
	
	/**
	 * 判断当前时间是否大于
	 * @param end
	 * @return
	 */
	public static Boolean isBoolean(String end) {
		String date = DateUtils.date24ToString(new Date());
		if(!end.isEmpty() && DateUtils.compareDate(end, date)) {
			return true;
		}
		return false;
	}
	/**
     * 获取上(下)周周几的日期
     * 
     * @param firstDayOfWeek {@link Calendar} 值范围 <code>SUNDAY</code>,
     *                       <code>MONDAY</code>, <code>TUESDAY</code>,
     *                       <code>WEDNESDAY</code>, <code>THURSDAY</code>,
     *                       <code>FRIDAY</code>, and <code>SATURDAY</code>
     * @param dayOfWeek      {@link Calendar}
     * @param weekOffset     周偏移，上周为-1，本周为0，下周为1，以此类推
     * @return
     */
    public static Date getDayOfWeek(int firstDayOfWeek, int dayOfWeek, int weekOffset) {
        if (dayOfWeek > Calendar.SATURDAY || dayOfWeek < Calendar.SUNDAY) {
            return null;
        }
        if (firstDayOfWeek > Calendar.SATURDAY || firstDayOfWeek < Calendar.SUNDAY) {
            return null;
        }
        Calendar date = Calendar.getInstance(Locale.CHINA);
        date.setFirstDayOfWeek(firstDayOfWeek);
        // 周数减一，即上周
        date.add(Calendar.WEEK_OF_MONTH, weekOffset);
        // 日子设为周几
        date.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        // 时分秒全部置0
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }
    /**
     * 获取上周周几的日期,默认一周从周一开始
     * 
     * @param dayOfWeek
     * @param weekOffset
     * @return
     */
    public static Date getDayOfWeek(int dayOfWeek, int weekOffset) {
        return getDayOfWeek(Calendar.MONDAY, dayOfWeek, weekOffset);
    }
	/**
	 * 星期文字转换
	 * @param week
	 * @return
	 */
	public static String getWeek(String week) {
		String str ="";
		switch (week) {
		case "1":
			str = "星期一";
			break;
		case "2":
			str = "星期二";
			break;
		case "3":
			str = "星期三";
			break;
		case "4":
			str = "星期四";
			break;
		case "5":
			str = "星期五";
			break;
		case "6":
			str = "星期六";
			break;
		case "7":
			str = "星期日";
			break;
		}
		return str;
	}
	
	/**
	 * 获取开始时间到结束时间的日期
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public static List<String> getStartAndEndDay(String beginDate,String endDate) {
		List<String> list = new ArrayList<String>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(beginDate));
			
			for (long d = cal.getTimeInMillis(); d <= sdf.parse(endDate).getTime(); d = get_D_Plaus_1(cal)) {
				list.add(sdf.format(d));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
 
	public static long get_D_Plaus_1(Calendar c) {
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
		return c.getTimeInMillis();
	}
	/**
	 * 与1970-01-01与当前时间相差日期
	 * @return
	 */
	 public static Integer getDate() {
	        LocalDate now = LocalDate.now();
	        return (int) ChronoUnit.DAYS.between(LocalDate.ofEpochDay(0), now);
	  }
	 
	 /**
	  * 指定时间+天数
	  * @param date
	  * @param day
	  * @return
	  */
	 public static String addDate(String date,int day) {
		 Date d = strToDate(date);
		 Calendar ca = Calendar.getInstance();
		 ca.setTime(d);
		 ca.add(Calendar.DAY_OF_MONTH, day);
		 return date24ToString(ca.getTime());
	  }
	 /**
	  * 指定时间+分钟
	  * @param date
	  * @param min
	  * @return
	  */
	 public static String addMinute(String date,int min) {
		 Date d = strToDate(date);
		 Calendar ca = Calendar.getInstance();
		 ca.setTime(d);
		 ca.add(Calendar.MINUTE, min);
		 return date24ToString(ca.getTime());
	  }
	 /**
	     * 功能描述：返回分
	     *
	     * @param date
	     *            日期
	     * @return 返回分钟
	     */
	    public static int getMinute(Date date) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        return calendar.get(Calendar.MINUTE);
	    }
	    
	    /**
	     * 功能描述：返回小时
	     *
	     * @param date
	     *            日期
	     * @return 返回小时
	     */
	    public static int getHour(Date date) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        return calendar.get(Calendar.HOUR_OF_DAY);
	    }

	    /**
	     * 获取指定时间的分钟数
	     * @param date
	     * @return
	     */
	    public static int getDateMinute(Date date) {
	    	  Calendar calendar = Calendar.getInstance();
		      calendar.setTime(date);
		     int hour = calendar.get(Calendar.HOUR_OF_DAY);
		     return calendar.get(Calendar.MINUTE)+(hour*60);
	    }
	    
	    /**
	     * 日期格式转换 yyyy-MM-dd HH:mm:ss  TO yyyy-MM-dd'T'HH:mm:ss.SSSXXX  (yyyy-MM-dd'T'HH:mm:ss.SSSZ)
	     * 2020-04-09 23:00:00 TO 2020-04-09T23:00:00.000+08:00
	     * @throws ParseException
	     */
	    public static String dealDateFormatReverse(String oldDateStr) {
	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	    	try {
		     DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		     Date date1 = df2.parse(oldDateStr);
			return df.format(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return null;
	    }
	    
	 public static void main(String[] args) {
//		System.out.println(DateUtils.getDate());
//		//18562
//		Date date = strToDate("19700101");
//		System.out.println(addDay(strToDate("19700101"), 18563));
//		 System.out.println(getDate());
//		 System.out.println(addDate("19700101", 18535));
//		 System.out.println(addMinute(addDate("19700101", 18570), 569));
//		 System.out.println(getDateMinute(new Date())-6);
//		 System.out.println(new Date());
		 String str = "2020-11-09 15:54:00";

//		 System.out.println(addDate("19700101", 18563));
//		 System.out.println(addMinute(addDate("19700101", 18563), 10));
	 }
	
}