package com.huoli.lyantool.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYY_MM_DD_ = "yyyy/MM/dd";
	public static final String YYYY_MM_DDHHmmssSSS = "yyyy-MM-dd HH:mm:ss:SSS";
	public static final String YYYY_MM_DDHHmmss = "yyyy-MM-dd HH:mm:ss";

	/**
	 *
	 */
	public static Date getDate(String dateStr, String format){
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
//			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String timestamp2str(Timestamp time, String format){
		DateFormat df = new SimpleDateFormat(format);
		return df.format(time);
	}

	public static Date getDate(String night, int days){
		Date date = getDate(night, YYYY_MM_DDHHmmss);
		return getDate(date, days);
	}

	public static Date getDate(Date date, int days){
		return new Date(date.getTime()+days*3600*24*1000);
	}

	public static String getDateStr(Date date, String format){
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * Calendar 转化 String 获取当前时间的具体情况,如年,月,日,week,date,分,秒等
	 *
	 * @param format
	 * @return
	 */
	public static String convertCalendarToString(String format) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(cal.getTime());

		return dateStr;
	}

	/**
	 * Calendar 转化 String 获取当前时间的具体情况,如年,月,日,week,date,分,秒等
	 *
	 * @param format
	 * @return
	 */
	public static String convertCalendarToString(Calendar cal, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(cal.getTime());

		return dateStr;
	}

	/**
	 * 距离今天结束的剩余时间数(秒)
	 */
	public static int leftSeconds(){
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			Date tmpNow1 = df1.parse(df1.format(now));
			Date tmpNow2 = df.parse(df.format(now));
			return (int) (((tmpNow2.getTime()+ 24*3600*1000) - tmpNow1.getTime())/1000);
		}catch(ParseException pe){
			return -1;		//error
		}
//		System.out.println(now.getTime());
//		System.out.println()
//		return 12;
	}

	/**
	 * String 转化Calendar
	 *
	 * @param str
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Calendar convertStringToCalendar(String str, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(str);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return Calendar.getInstance();
		}
	}

	/**
	 * 时间相减得到天数
	 *
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr,
			String format) {
		try {
			long day = 0;
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			java.util.Date beginDate = sdf.parse(beginDateStr);
			java.util.Date endDate = sdf.parse(endDateStr);

			day = (endDate.getTime() - beginDate.getTime())
					/ (24 * 60 * 60 * 1000);

			return day;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return 0;
		}
	}

	public static Date convertStringToDate(String date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date d = sdf.parse(date);
			return d;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return Calendar.getInstance().getTime();
		}
	}

	public static String convertStringToStringDate(String date, String format)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String d = sdf.format(date);
		return d;
	}
	
	public static String getMonthStart(String format){
		Calendar calendar = new GregorianCalendar();
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		calendar = new GregorianCalendar(year, month, 1);
		
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(calendar.getTime());
	}
	
	public static String getYearStart(String format){
		Calendar calendar = new GregorianCalendar();
		
		int year = calendar.get(Calendar.YEAR);
		calendar = new GregorianCalendar(year, 0, 1);
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(calendar.getTime());
	}

	public static String convertDateToString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(new Date());
		return dateStr;
	}

	public static String convertDateToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(date);
		return dateStr;
	}

	public static boolean isInTimePeriod() {
		Calendar time = Calendar.getInstance();
		time.set(Calendar.HOUR_OF_DAY, 23);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.MILLISECOND, 0);
		long elevenClock = time.getTimeInMillis();
		time.set(Calendar.HOUR_OF_DAY, 23);
		time.set(Calendar.MINUTE, 30);
		long elevenHalfClock = time.getTimeInMillis();
		long currentTime = System.currentTimeMillis();
		if (currentTime > elevenClock && currentTime < elevenHalfClock) {
			return true;
		} else
			return false;

		// return false;
	}

	public static boolean compareTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 2);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.MILLISECOND, 0);
		long firstClock = calendar.getTimeInMillis();

		calendar.set(Calendar.HOUR_OF_DAY, 5);
		long secondClock = calendar.getTimeInMillis();

		long currentTime = System.currentTimeMillis();

		if (currentTime > firstClock && currentTime < secondClock)
			return true;

		return false;
	}

	public static long getSubTime() {

		return 0;
	}

	/**
	 * 返回当前日期
	 *
	 * @return
	 */
	public static String getCurrentDate(String format) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String date = sdf.format(cal.getTime());
		return date;
	}

	/**
	 * 返回给定天的num天后的字符串
	 *
	 * @param curDay
	 * @param dateFormat
	 * @param num
	 * @return
	 */
	public static String getNextDay(String curDay, String dateFormat, int num) {
		SimpleDateFormat sdf = null;
		Calendar cal = null;
		Date date = null;
		String dateStr = null;

		sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		try {
			date = sdf.parse(curDay);
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, num);
			date = cal.getTime();
			dateStr = sdf.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("错误的日期格式！");
			// e.printStackTrace();
			return curDay;
		}

		return dateStr;
	}

	/**
	 * 返回给定天的下一天的字符串
	 *
	 * @param curDay
	 * @param dateFormat
	 * @return
	 */
	public static String getNextDay(String curDay, String dateFormat) {
		SimpleDateFormat sdf = null;
		Calendar cal = null;
		Date date = null;
		String dateStr = null;

		sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		try {
			date = sdf.parse(curDay);
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = cal.getTime();
			dateStr = sdf.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("错误的日期格式！");
			// e.printStackTrace();
			return curDay;
		}

		return dateStr;
	}

	/**
	 * 返回给定天的前一天的字符串
	 *
	 * @param curDay
	 * @param dateFormat
	 * @return
	 */
	public static String getPreDay(String curDay, String dateFormat) {
		SimpleDateFormat sdf = null;
		Calendar cal = null;
		Date date = null;
		String dateStr = null;

		sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		try {
			date = sdf.parse(curDay);
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			date = cal.getTime();
			dateStr = sdf.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// System.out.println("错误的日期格式！");
			// e.printStackTrace();
			return curDay;
		}

		return dateStr;
	}

	/**
	 * 将日期转换为字符串
	 *
	 * @param date
	 *            要转换为String的日期
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static String date2String(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		String time = df.format(date);
		return time;
	}

	/**
	 * 比较两个时间的差，用时间1-时间2
	 *
	 * @param date1
	 * @param date2
	 * @return 两个时间相差的分钟
	 */
	public static int getMinute(String date1, String date2, String format) {
		try {
			if (date1 == null || date2 == null)
				return 0;

			SimpleDateFormat sdf = new SimpleDateFormat(format);
			java.util.Date beginDate = sdf.parse(date1);
			java.util.Date endDate = sdf.parse(date2);

			Long minute = (endDate.getTime() - beginDate.getTime())
					/ (60 * 1000);

			return minute.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int compare(Date date1, Date date2){
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		if(time1 > time2){
			return 1;
		}else if(time1 == time2){
			return 0;
		}else{
			return -1;
		}
	}

	/**
	 * 毫秒转日期字符串
	 *
	 * @param str
	 * @param dateformat
	 * @return
	 */
	public static String getDateTimeByMillisecond(String str, String dateformat) {
		Date date = new Date(Long.valueOf(str));
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		String time = format.format(date);
		return time;
	}

	public static void main(String[] args) {
		System.out.println("hello"+DateUtil.getMonthStart("yyyy-MM-dd"));
	}
}
