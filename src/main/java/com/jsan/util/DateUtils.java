package com.jsan.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 简易的日期工具类。
 * 
 * <p>
 * 对于ISO8601日期时间格式的处理，Log4j已有现成实现：
 * 
 * <pre>
 * DateFormat format = ISO8601DateFormat.getDateInstance();
 * 
 * <pre>
 * <p>
 * 更专业的日期时间处理可参：Joda-Time。
 * 
 */

public class DateUtils {

	private static final String[] WEEK_CHINESE_ITEM = { "日", "一", "二", "三", "四", "五", "六" };

	private static final String WEEK_CHINESE_PREFIX = "星期";

	private static final String WEEK_CHINESE_PREFIX_SHORT = "周";

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";

	private static final String[] DATE_FORMATS = { "y-M-d H:m:s.S", "y-M-d H:m:s", "y-M-d H:m", "y-M-d", "y-M",
			"y/M/d H:m:s.S", "y/M/d H:m:s", "y/M/d H:m", "y/M/d", "y/M", }; // 此处顺序不可更改，否则会因优先匹配而导致丢失精度

	/**
	 * 返回 SimpleDateFormat 对象。
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static SimpleDateFormat getSimpleDateFormat(String dateFormat) {

		return new SimpleDateFormat(dateFormat);
	}

	/**
	 * 返回 Calendar 对象。
	 * 
	 * @return
	 */
	public static Calendar getCalendar() {

		return Calendar.getInstance();
	}

	/**
	 * 返回 Calendar 对象（指定 Date）。
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar getCalendar(Date date) {

		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 将字符串转换为 Date 对象。
	 * 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String str) {

		Date date = null;

		for (String format : DATE_FORMATS) {
			try {
				date = getSimpleDateFormat(format).parse(str);
				break;
			} catch (ParseException e) {
				// skip...
			}
		}

		// 尝试匹配 "EEE MMM dd HH:mm:ss Z yyyy"
		if (date == null) {
			try {
				date = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(str);
			} catch (ParseException e) {
				// skip...
			}
		}

		return date;
	}

	/**
	 * 将字符串转换为 Date 对象（指定转换格式）。
	 * 
	 * @param str
	 * @param dateFormat
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String str, String dateFormat) {

		try {
			return getSimpleDateFormat(dateFormat).parse(str);
		} catch (ParseException e) {
			// skip...
		}

		return null;
	}

	/**
	 * 转换为 Date 对象（年）。
	 * 
	 * @param year
	 * @return
	 */
	public static Date parseDate(int year) {

		return parseDate(year, 0);
	}

	/**
	 * 转换为 Date 对象（年、月）。
	 * 
	 * @param year
	 * @param month
	 *            起始为 0
	 * @return
	 */
	public static Date parseDate(int year, int month) {

		return parseDate(year, month, 1);
	}

	/**
	 * 转换为 Date 对象（年、月、日）。
	 * 
	 * @param year
	 * @param month
	 *            起始为 0
	 * @param day
	 * @return
	 */
	public static Date parseDate(int year, int month, int day) {

		return parseDate(year, month, day, 0, 0, 0);
	}

	/**
	 * 转换为 Date 对象（年、月、日、时、分、秒）。
	 * 
	 * @param year
	 * @param month
	 *            起始为 0
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date parseDate(int year, int month, int day, int hour, int minute, int second) {

		Calendar calendar = new GregorianCalendar(year, month, day, hour, minute, second);
		return calendar.getTime();
	}

	/**
	 * 返回当前日期的毫秒数。
	 * 
	 * @return
	 */
	public static long getTime() {

		return getDate().getTime();
	}

	/**
	 * 返回当前日期。
	 * 
	 * @return
	 */
	public static Date getDate() {

		return new Date();
	}

	/**
	 * 返回昨天日期。
	 * 
	 * @return
	 */
	public static Date getYesterday() {

		return getOffsetDays(-1);
	}

	/**
	 * 返回明天日期。
	 * 
	 * @return
	 */
	public static Date getTomorrow() {

		return getOffsetDays(1);
	}

	/**
	 * 返回当前日期的字符串形式（格式：yyyy-MM-dd）。
	 * 
	 * @return
	 */
	public static String getDateStr() {

		return getDateStr(getDate(), DEFAULT_DATE_FORMAT);
	}

	/**
	 * 返回指定日期的字符串形式（格式：yyyy-MM-dd）。
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date) {

		return getDateStr(date, DEFAULT_DATE_FORMAT);
	}

	/**
	 * 返回当前日期的字符串形式（指定格式）。
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String getDateStr(String dateFormat) {

		return getDateStr(getDate(), dateFormat);
	}

	/**
	 * 返回指定日期的字符串形式（指定格式）。
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String getDateStr(Date date, String dateFormat) {

		SimpleDateFormat simpleDateFormat = getSimpleDateFormat(dateFormat);
		return simpleDateFormat.format(date);
	}

	/**
	 * 返回当前日期的字符串形式（格式：yyyy-MM-dd HH:mm:ss）。
	 * 
	 * @return
	 */
	public static String getDateTimeStr() {

		return getDateStr(getDate(), DEFAULT_DATETIME_FORMAT);
	}

	/**
	 * 返回指定日期的字符串形式（格式：yyyy-MM-dd HH:mm:ss）。
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTimeStr(Date date) {

		return getDateStr(date, DEFAULT_DATETIME_FORMAT);
	}

	/**
	 * 返回当前日期的最小时间。
	 * 
	 * @return
	 */
	public static Date getDateMin() {

		return getDateMin(getDate());
	}

	/**
	 * 返回指定日期的最小时间。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateMin(Date date) {

		Calendar calendar = getCalendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * 返回当前日期的最大时间。
	 * 
	 * @return
	 */
	public static Date getDateMax() {

		return getDateMax(getDate());
	}

	/**
	 * 返回指定日期的最大时间。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateMax(Date date) {

		Calendar calendar = getCalendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTime();
	}

	/**
	 * 判断指定时间是否在当前时间之前（小于）。
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isBefore(Date date) {

		return isBefore(getDate(), date);
	}

	/**
	 * 判断指定时间是否在参照时间之前（小于）。
	 * 
	 * @param referenceDate
	 * @param date
	 * @return
	 */
	public static boolean isBefore(Date referenceDate, Date date) {

		int i = date.compareTo(referenceDate);
		if (i < 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断指定时间是否在当前时间之后（大于）。
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isAfter(Date date) {

		return isAfter(getDate(), date);
	}

	/**
	 * 判断指定时间是否在参照时间之后（大于）。
	 * 
	 * @param referenceDate
	 * @param date
	 * @return
	 */
	public static boolean isAfter(Date referenceDate, Date date) {

		int i = date.compareTo(referenceDate);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断指定时间是否与当前时间相同（等于）。
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isEqual(Date date) {

		return isEqual(getDate(), date);
	}

	/**
	 * 判断两个时间是否相同（等于）。
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isEqual(Date date1, Date date2) {

		int i = date2.compareTo(date1);
		if (i == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断当前时间是否在两个时间之中（大于等于或小于等于）。
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean isBetween(Date startDate, Date endDate) {

		return isBetween(startDate, endDate, getDate());
	}

	/**
	 * 判断指定时间是否在两个时间之中（大于等于或小于等于）。
	 * 
	 * @param startDate
	 * @param endDate
	 * @param date
	 * @return
	 */
	public static boolean isBetween(Date startDate, Date endDate, Date date) {

		int i = endDate.compareTo(startDate);
		int j = date.compareTo(startDate);
		int k = date.compareTo(endDate);

		if ((i >= 0 && j >= 0 && k <= 0) || (i <= 0 && k >= 0 && j <= 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回当前日期在指定毫秒偏移量后的日期。
	 * 
	 * @param millisecond
	 *            正负长整数
	 * @return
	 */
	public static Date getOffsetMillisecond(long millisecond) {

		return getOffsetMillisecond(getDate(), millisecond);
	}

	/**
	 * 返回指定日期在指定毫秒偏移量后的日期。
	 * 
	 * @param date
	 * @param millisecond
	 *            正负长整数
	 * @return
	 */
	public static Date getOffsetMillisecond(Date date, long millisecond) {

		long time = date.getTime();
		time += millisecond;
		return new Date(time);
	}

	/**
	 * 返回当前日期在指定秒偏移量后的日期。
	 * 
	 * @param seconds
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetSeconds(int seconds) {

		return getOffsetSeconds(getDate(), seconds);
	}

	/**
	 * 返回指定日期在指定秒偏移量后的日期。
	 * 
	 * @param date
	 * @param seconds
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetSeconds(Date date, int seconds) {

		Calendar calendar = getCalendar(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}

	/**
	 * 返回当前日期在指定分钟偏移量后的日期。
	 * 
	 * @param minutes
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetMinutes(int minutes) {

		return getOffsetMinutes(getDate(), minutes);
	}

	/**
	 * 返回指定日期在指定分钟偏移量后的日期。
	 * 
	 * @param date
	 * @param minutes
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetMinutes(Date date, int minutes) {

		Calendar calendar = getCalendar(date);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}

	/**
	 * 返回当前日期在指定小时偏移量后的日期。
	 * 
	 * @param hours
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetHours(int hours) {

		return getOffsetHours(getDate(), hours);
	}

	/**
	 * 返回指定日期在指定小时偏移量后的日期。
	 * 
	 * @param date
	 * @param hours
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetHours(Date date, int hours) {

		Calendar calendar = getCalendar(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}

	/**
	 * 返回当前日期在指定天数偏移量后的日期。
	 * 
	 * @param days
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetDays(int days) {

		return getOffsetDays(getDate(), days);
	}

	/**
	 * 返回指定日期在指定天数偏移量后的日期。
	 * 
	 * @param date
	 * @param days
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetDays(Date date, int days) {

		Calendar calendar = getCalendar(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * 返回当前日期在指定周数偏移量后的日期。
	 * 
	 * @param weeks
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetWeeks(int weeks) {

		return getOffsetWeeks(getDate(), weeks);
	}

	/**
	 * 返回指定日期在指定周数偏移量后的日期。
	 * 
	 * @param date
	 * @param weeks
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetWeeks(Date date, int weeks) {

		return getOffsetDays(date, weeks * 7);
	}

	/**
	 * 返回当前日期在指定月份偏移量后的日期。
	 * 
	 * @param date
	 * @param months
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetMonths(int months) {

		return getOffsetMonths(getDate(), months);
	}

	/**
	 * 返回指定日期在指定月份偏移量后的日期。
	 * 
	 * @param date
	 * @param months
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetMonths(Date date, int months) {

		Calendar calendar = getCalendar(date);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}

	/**
	 * 返回当前日期在指定年份偏移量后的日期。
	 * 
	 * @param years
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetYears(int years) {

		return getOffsetYears(getDate(), years);
	}

	/**
	 * 返回指定日期在指定年份偏移量后的日期。
	 * 
	 * @param date
	 * @param years
	 *            正负整数
	 * @return
	 */
	public static Date getOffsetYears(Date date, int years) {

		Calendar calendar = getCalendar(date);
		calendar.add(Calendar.YEAR, years);
		return calendar.getTime();
	}

	/**
	 * 返回当前日期所在周的第一天（即周日）。
	 * 
	 * @return
	 */
	public static Date getFirstDateOfWeek() {

		return getFirstDateOfWeek(getDate());
	}

	/**
	 * 返回指定日期所在周的第一天（即周日）。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfWeek(Date date) {

		return getDateOfWeek(date, 1);
	}

	/**
	 * 返回当前日期所在周的最后一天（即周六）。
	 * 
	 * @return
	 */
	public static Date getLastDateOfWeek() {

		return getLastDateOfWeek(getDate());
	}

	/**
	 * 返回指定日期所在周的最后一天（即周六）。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfWeek(Date date) {

		return getDateOfWeek(date, 7);
	}

	/**
	 * 返回当前日期所在月份的第一天。
	 * 
	 * @return
	 */
	public static Date getFirstDateOfMonth() {

		return getFirstDateOfMonth(getDate());
	}

	/**
	 * 返回指定日期所在月份的第一天。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {

		return getDateOfMonth(date, 1);
	}

	/**
	 * 返回当前日期所在月份的最后一天。
	 * 
	 * @return
	 */
	public static Date getLastDateOfMonth() {

		return getLastDateOfMonth(getDate());
	}

	/**
	 * 返回指定日期所在月份的最后一天。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {

		return getDateOfMonth(date, getDaysOfMonth(date));
	}

	/**
	 * 返回当前日期所在年份的第一天。
	 * 
	 * @return
	 */
	public static Date getFirstDateOfYear() {

		return getFirstDateOfYear(getDate());
	}

	/**
	 * 返回指定日期所在年份的第一天。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfYear(Date date) {

		return getDateOfYear(date, 1);
	}

	/**
	 * 返回当前日期所在年份的最后一天。
	 * 
	 * @return
	 */
	public static Date getLastDateOfYear() {

		return getLastDateOfYear(getDate());
	}

	/**
	 * 返回指定日期所在年份的最后一天。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfYear(Date date) {

		return getDateOfYear(date, getDaysOfYear(date));
	}

	/**
	 * 返回指定时间与当前时间的间隔数（毫秒）。
	 * 
	 * @param Date
	 * @return
	 */
	public static long getIntervalMilliseconds(Date date) {

		return getIntervalMilliseconds(date, getDate());
	}

	/**
	 * 返回两个时间的间隔数（毫秒）。
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getIntervalMilliseconds(Date startDate, Date endDate) {

		return endDate.getTime() - startDate.getTime();
	}

	/**
	 * 返回指定时间与当前时间的间隔数（秒）。
	 * 
	 * @param date
	 * @return
	 */
	public static long getIntervalSeconds(Date date) {

		return getIntervalSeconds(getDate(), date);
	}

	/**
	 * 返回两个时间的间隔数（秒）。
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getIntervalSeconds(Date startDate, Date endDate) {

		Calendar startCalendar = getCalendar(startDate);
		startCalendar.set(Calendar.MILLISECOND, 0);

		Calendar endCalendar = getCalendar(endDate);
		endCalendar.set(Calendar.MILLISECOND, 0);

		long startTime = startCalendar.getTimeInMillis() / 1000;
		long endTime = endCalendar.getTimeInMillis() / 1000;
		return endTime - startTime;
	}

	/**
	 * 返回指定时间与当前时间的间隔数（分钟）。
	 * 
	 * @param date
	 * @return
	 */
	public static long getIntervalMinutes(Date date) {

		return getIntervalMinutes(getDate(), date);
	}

	/**
	 * 返回两个时间的间隔数（分钟）。
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getIntervalMinutes(Date startDate, Date endDate) {

		Calendar startCalendar = getCalendar(startDate);
		startCalendar.set(Calendar.MILLISECOND, 0);
		startCalendar.set(Calendar.SECOND, 0);

		Calendar endCalendar = getCalendar(endDate);
		endCalendar.set(Calendar.MILLISECOND, 0);
		endCalendar.set(Calendar.SECOND, 0);

		long startTime = startCalendar.getTimeInMillis() / 1000 / 60;
		long endTime = endCalendar.getTimeInMillis() / 1000 / 60;
		return endTime - startTime;
	}

	/**
	 * 返回指定时间与当前时间的间隔数（小时）。
	 * 
	 * @param date
	 * @return
	 */
	public static long getIntervalHours(Date date) {

		return getIntervalHours(getDate(), date);
	}

	/**
	 * 返回两个时间的间隔数（小时）。
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getIntervalHours(Date startDate, Date endDate) {

		Calendar startCalendar = getCalendar(startDate);
		startCalendar.set(Calendar.MILLISECOND, 0);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MINUTE, 0);

		Calendar endCalendar = getCalendar(endDate);
		endCalendar.set(Calendar.MILLISECOND, 0);
		endCalendar.set(Calendar.SECOND, 0);
		endCalendar.set(Calendar.MINUTE, 0);

		long startTime = startCalendar.getTimeInMillis() / 1000 / 60 / 60;
		long endTime = endCalendar.getTimeInMillis() / 1000 / 60 / 60;
		return endTime - startTime;
	}

	/**
	 * 返回指定时间与当前时间的间隔数（天）。
	 * 
	 * @param date
	 * @return
	 */
	public static int getIntervalDays(Date date) {

		return getIntervalDays(getDate(), date);
	}

	/**
	 * 返回两个时间的间隔数（天）。
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getIntervalDays(Date startDate, Date endDate) {

		Calendar startCalendar = getCalendar(startDate);
		startCalendar.set(Calendar.MILLISECOND, 0);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.HOUR_OF_DAY, 0);

		Calendar endCalendar = getCalendar(endDate);
		endCalendar.set(Calendar.MILLISECOND, 0);
		endCalendar.set(Calendar.SECOND, 0);
		endCalendar.set(Calendar.MINUTE, 0);
		endCalendar.set(Calendar.HOUR_OF_DAY, 0);

		long startTime = (startCalendar.getTimeInMillis() / 1000 / 60 / 60 / 24);
		long endTime = (endCalendar.getTimeInMillis() / 1000 / 60 / 60 / 24);

		return (int) (endTime - startTime);
	}

	/**
	 * 返回指定时间与当前时间的间隔数（周）。
	 * 
	 * @param date
	 * @return
	 */
	public static int getIntervalWeeks(Date date) {

		return getIntervalWeeks(getDate(), date);
	}

	/**
	 * 返回两个时间的间隔数（周）。
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getIntervalWeeks(Date startDate, Date endDate) {

		int d = getIntervalDays(startDate, endDate);
		int startWeek = getWeek(startDate);
		int endWeek = getWeek(endDate);
		d = (d + startWeek + 7 - endWeek) / 7 - 1;

		return d;
	}

	/**
	 * 返回指定时间与当前时间的间隔数（月）。
	 * 
	 * @param date
	 * @return
	 */
	public static int getIntervalMonths(Date date) {

		return getIntervalMonths(getDate(), date);
	}

	/**
	 * 返回两个时间的间隔数（月）。
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getIntervalMonths(Date startDate, Date endDate) {

		int startMonth = getMonth(startDate);
		int endMonth = getMonth(endDate);
		int startYear = getYear(startDate);
		int endYear = getYear(endDate);
		int y = endYear - startYear;
		y = y * 12 + endMonth - startMonth;

		return y;
	}

	/**
	 * 返回指定时间与当前时间的间隔数（年）。
	 * 
	 * @param date
	 * @return
	 */
	public static int getIntervalYears(Date date) {

		return getIntervalYears(getDate(), date);
	}

	/**
	 * 返回两个时间的间隔数（年）。
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getIntervalYears(Date startDate, Date endDate) {

		return getYear(endDate) - getYear(startDate);
	}

	/**
	 * 返回当前日期所属周的周几（星期日：1、星期一：2、星期二：3、星期三：4、星期四：5、星期五：6、星期六：7）。
	 * 
	 * @param dayOfWeek
	 * @return
	 */
	public static Date getDateOfWeek(int dayOfWeek) {

		return getDateOfWeek(getDate(), dayOfWeek);
	}

	/**
	 * 返回指定日期所属周的周几（星期日：1、星期一：2、星期二：3、星期三：4、星期四：5、星期五：6、星期六：7）。
	 * 
	 * @param date
	 * @param dayOfWeek
	 * @return
	 */
	public static Date getDateOfWeek(Date date, int dayOfWeek) {

		int w = getCalendar(date).get(Calendar.DAY_OF_WEEK);
		return getOffsetDays(date, dayOfWeek - w);
	}

	/**
	 * 返回当前日期所属月的几号（1号：1、2号：2、3号：3 ...）。
	 * 
	 * @param dayOfMonth
	 * @return
	 */
	public static Date getDateOfMonth(int dayOfMonth) {

		return getDateOfMonth(getDate(), dayOfMonth);
	}

	/**
	 * 返回指定日期所属月的几号（1号：1、2号：2、3号：3 ...）。
	 * 
	 * @param date
	 * @param dayOfMonth
	 * @return
	 */
	public static Date getDateOfMonth(Date date, int dayOfMonth) {

		int w = getCalendar(date).get(Calendar.DAY_OF_MONTH);
		return getOffsetDays(date, dayOfMonth - w);
	}

	/**
	 * 返回当前日期所属年的第几天。
	 * 
	 * @param dayOfYear
	 * @return
	 */
	public static Date getDateOfYear(int dayOfYear) {

		return getDateOfYear(getDate(), dayOfYear);
	}

	/**
	 * 返回指定日期所属年的第几天。
	 * 
	 * @param date
	 * @param dayOfYear
	 * @return
	 */
	public static Date getDateOfYear(Date date, int dayOfYear) {

		int w = getCalendar(date).get(Calendar.DAY_OF_YEAR);
		return getOffsetDays(date, dayOfYear - w);
	}

	/**
	 * 返回当前月份的天数。
	 * 
	 * @return
	 */
	public static int getDaysOfMonth() {

		return getDaysOfMonth(getDate());
	}

	/**
	 * 返回指定日期所在月份的天数。
	 * 
	 * @param date
	 * @return
	 */
	public static int getDaysOfMonth(Date date) {

		Calendar calendar = getCalendar(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回当前年份的天数。
	 * 
	 * @return
	 */
	public static int getDaysOfYear() {

		return getDaysOfYear(getDate());
	}

	/**
	 * 返回指定日期所在年份的天数。
	 * 
	 * @param date
	 * @return
	 */
	public static int getDaysOfYear(Date date) {

		Calendar calendar = getCalendar(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 返回当前月份的周数。
	 * 
	 * @return
	 */
	public static int getWeeksOfMonth() {

		return getWeeksOfMonth(getDate());
	}

	/**
	 * 返回指定日期所在月份的周数。
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeeksOfMonth(Date date) {

		Calendar calendar = getCalendar(date);
		return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 返回当前年份的周数。
	 * 
	 * @return
	 */
	public static int getWeeksOfYear() {

		return getWeeksOfYear(getDate());
	}

	/**
	 * 返回指定日期所在年份的周数。
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeeksOfYear(Date date) {

		Calendar calendar = getCalendar(date);
		return calendar.getActualMaximum(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 返回当前日期所属的中文周（日 、一、二、三、四、五、六）。
	 * 
	 * @return
	 */
	public static String getWeekChinese() {

		return getWeekChinese(getDate());
	}

	/**
	 * 返回指定日期所属的中文周（日 、一、二、三、四、五、六）。
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekChinese(Date date) {

		int i = getWeek(date);
		return WEEK_CHINESE_ITEM[--i];
	}

	/**
	 * 返回当前日期所属的中文周（星期日 、星期一、星期二、星期三、星期四、星期五、星期六）。
	 * 
	 * @return
	 */
	public static String getWeekChineseFull() {

		return getWeekChineseFull(getDate());
	}

	/**
	 * 返回指定日期所属的中文周（星期日 、星期一、星期二、星期三、星期四、星期五、星期六）。
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekChineseFull(Date date) {

		return WEEK_CHINESE_PREFIX + getWeekChinese(date);
	}

	/**
	 * 返回当前日期所属的中文周（周日 、周一、周二、周三、周四、周五、周六）。
	 * 
	 * @return
	 */
	public static String getWeekChineseShort() {

		return getWeekChineseShort(getDate());
	}

	/**
	 * 返回指定日期所属的中文周（周日 、周一、周二、周三、周四、周五、周六）。
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekChineseShort(Date date) {

		return WEEK_CHINESE_PREFIX_SHORT + getWeekChinese(date);
	}

	/**
	 * 返回当前日期是几号（1号：1、2号：2、3号：3 ...）。
	 * 
	 * @return
	 */
	public static int getDay() {

		return getDay(getDate());
	}

	/**
	 * 返回指定日期是几号（1号：1、2号：2、3号：3 ...）。
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {

		Calendar calendar = getCalendar(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回当前日期是周几（星期日：1、星期一：2、星期二：3、星期三：4、星期四：5、星期五：6、星期六：7）。
	 * 
	 * @return
	 */
	public static int getWeek() {

		return getWeek(getDate());
	}

	/**
	 * 返回指定日期是周几（星期日：1、星期一：2、星期二：3、星期三：4、星期四：5、星期五：6、星期六：7）。
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date) {

		Calendar calendar = getCalendar(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 返回当前日期属于几月份（一月：0、二月：1、三月：2 ...）。
	 * 
	 * @return
	 */
	public static int getMonth() {

		return getMonth(getDate());
	}

	/**
	 * 返回指定日期属于几月份（一月：0、二月：1、三月：2 ...）。
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {

		Calendar calendar = getCalendar(date);
		return calendar.get(Calendar.MONTH);
	}

	/**
	 * 返回当前日期属于某年。
	 * 
	 * @return
	 */
	public static int getYear() {

		return getYear(getDate());
	}

	/**
	 * 返回指定日期属于某年。
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {

		Calendar calendar = getCalendar(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 判断当前日期所属年份是否是闰年。
	 * 
	 * @return
	 */
	public static boolean isLeapYear() {

		return isLeapYear(getDate());
	}

	/**
	 * 判断指定日期所属年份是否是闰年。
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isLeapYear(Date date) {

		String year = getDateStr(date, "y");
		return isLeapYear(year);
	}

	/**
	 * 判断指定年份是否是闰年。
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {

		return new GregorianCalendar().isLeapYear(year);
	}

	/**
	 * 判断指定年份是否是闰年。
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(String year) {

		int y = Integer.parseInt(year);
		return isLeapYear(y);
	}

	/**
	 * 返回指定生日至今的年龄（按年计算）。
	 * 
	 * @param date
	 * @return
	 */
	public static int getAge(Date date) {

		return getIntervalYears(date, getDate());
	}

	/**
	 * 返回指定生日至今的年龄（精确到按日计算）。
	 * 
	 * @param date
	 * @return
	 */
	public static int getExactAge(Date date) {

		int age = getAge(date);
		if (isBefore(getOffsetYears(date, age), getDate())) {
			age -= 1;
		}
		return age;
	}

}
