package com.jsan.util;

import java.util.Date;

import junit.framework.TestCase;

public class DateUtilsTest extends TestCase {

	public void testFoo() {

		Date date = DateUtils.parseDate("2014-06-11 15:23:59");
		Date date1 = DateUtils.parseDate("2015-06-28 15:23:59");
		Date date2 = DateUtils.parseDate("1983-08-2 16:23:58");

		println("字符串（默认）转Date对象", DateUtils.parseDate("2015-2-25 11:25"));
		println("字符串（指定格式）转Date对象", DateUtils.parseDate("2015*2*25 11时25分33秒", "yyyy*MM*dd HH时mm分ss秒"));
		println(null, null);
		println("年转Date对象", DateUtils.parseDate(2013));
		println("年、月转Date对象", DateUtils.parseDate(2013, 11));
		println("年、月、日转Date对象", DateUtils.parseDate(2013, 11, 22));
		println("年、月、日、时、分、秒转Date对象", DateUtils.parseDate(2013, 11, 22, 5, 6, 7));
		println(null, null);
		println("今天的Date对象的毫秒数", DateUtils.getTime());
		println("今天的Date对象", DateUtils.getDate());
		println("昨天的Date对象", DateUtils.getYesterday());
		println("明天的Date对象", DateUtils.getTomorrow());
		println(null, null);
		println("今天日期的字符串（默认）", DateUtils.getDateStr());
		println("date日期的字符串（默认）", DateUtils.getDateStr(date));
		println("今天日期的字符串（指定格式）", DateUtils.getDateStr("yy年MM月dd日 HH时mm分ss秒"));
		println("date日期的字符串（指定格式）", DateUtils.getDateStr(date, "yy年MM月dd日  HH时mm分ss秒"));
		println("今天日期和时间的字符串（默认）", DateUtils.getDateTimeStr());
		println("date日期和时间的字符串（默认）", DateUtils.getDateTimeStr(date));
		println(null, null);
		println("今天的最小时间值", DateUtils.getDateMin());
		println("date的最小时间值", DateUtils.getDateMin(date));
		println("今天的最大时间值", DateUtils.getDateMax());
		println("date的最大时间值", DateUtils.getDateMax(date));
		println(null, null);
		println("date是否在当前时间之前", DateUtils.isBefore(date));
		println("date是否在date1之前", DateUtils.isBefore(date1, date));
		println("date是否在当前时间之后", DateUtils.isAfter(date));
		println("date是否在date1之后", DateUtils.isAfter(date1, date));
		println("date是否等于当前时间", DateUtils.isEqual(date));
		println("date是否等于date1", DateUtils.isEqual(date1, date));
		println("当前时间是否在date1和date2之间", DateUtils.isBetween(date1, date2));
		println("date是否是否在date1和date2之间", DateUtils.isBetween(date1, date2, date));
		println(null, null);
		println("此时偏移100毫秒", DateUtils.getOffsetMillisecond(100));
		println("date偏移-59毫秒", DateUtils.getOffsetMillisecond(date, -59));
		println("此时偏移100秒", DateUtils.getOffsetSeconds(100));
		println("date偏移-59秒", DateUtils.getOffsetSeconds(date, -59));
		println("此时偏移-83分钟", DateUtils.getOffsetMinutes(-83));
		println("date偏移40分钟", DateUtils.getOffsetMinutes(date, 40));
		println("此时偏移-30小时", DateUtils.getOffsetHours(-30));
		println("date偏移3小时", DateUtils.getOffsetHours(date, 3));
		println("本日偏移365天", DateUtils.getOffsetDays(365));
		println("date偏移-11天", DateUtils.getOffsetDays(date, -11));
		println("本月偏移-24个月", DateUtils.getOffsetMonths(-24));
		println("date偏移3个月", DateUtils.getOffsetMonths(date, 3));
		println("今年偏移-20年", DateUtils.getOffsetYears(-20));
		println("date偏移5年", DateUtils.getOffsetYears(date, 5));
		println(null, null);
		println("当前所在周的第一天", DateUtils.getFirstDateOfWeek());
		println("date所在周的第一天", DateUtils.getFirstDateOfWeek(date));
		println("当前所在周的最后一天", DateUtils.getLastDateOfWeek());
		println("date所在周的最后一天", DateUtils.getLastDateOfWeek(date));
		println("当前所在月的第一天", DateUtils.getFirstDateOfMonth());
		println("date所在月的第一天", DateUtils.getFirstDateOfMonth(date));
		println("当前所在月的最后一天", DateUtils.getLastDateOfMonth());
		println("date所在月的最后一天", DateUtils.getLastDateOfMonth(date));
		println("当前所在年的第一天", DateUtils.getFirstDateOfYear());
		println("date所在年的第一天", DateUtils.getFirstDateOfYear(date));
		println("当前所在年的最后一天", DateUtils.getLastDateOfYear());
		println("date所在年的最后一天", DateUtils.getLastDateOfYear(date));
		println(null, null);
		println("date与当前时间的间隔数（毫秒）", DateUtils.getIntervalMilliseconds(date));
		println("date1与date2的间隔数（毫秒）", DateUtils.getIntervalMilliseconds(date1, date2));
		println("date与当前时间的间隔数（秒）", DateUtils.getIntervalSeconds(date));
		println("date1与date2的间隔数（秒）", DateUtils.getIntervalSeconds(date1, date2));
		println("date与当前时间的间隔数（分钟）", DateUtils.getIntervalMinutes(date));
		println("date1与date2的间隔数（分钟）", DateUtils.getIntervalMinutes(date1, date2));
		println("date与当前时间的间隔数（小时）", DateUtils.getIntervalHours(date));
		println("date1与date2的间隔数（小时）", DateUtils.getIntervalHours(date1, date2));
		println("date与当前时间的间隔数（天）", DateUtils.getIntervalDays(date));
		println("date1与date2的间隔数（天）", DateUtils.getIntervalDays(date1, date2));
		println("date与当前时间的间隔数（周）", DateUtils.getIntervalWeeks(date));
		println("date1与date2的间隔数（周）", DateUtils.getIntervalWeeks(date1, date2));
		println("date与当前时间的间隔数（月）", DateUtils.getIntervalMonths(date));
		println("date1与date2的间隔数（月）", DateUtils.getIntervalMonths(date1, date2));
		println("date与当前时间的间隔数（年）", DateUtils.getIntervalYears(date));
		println("date1与date2的间隔数（年）", DateUtils.getIntervalYears(date1, date2));
		println(null, null);
		println("本周的周日", DateUtils.getDateOfWeek(1));
		println("date所在周的周三", DateUtils.getDateOfWeek(date, 4));
		println("本月的1号", DateUtils.getDateOfMonth(1));
		println("date所在月的30号", DateUtils.getDateOfMonth(date, 30));
		println("本年的第1天", DateUtils.getDateOfYear(1));
		println("date所在年的365天", DateUtils.getDateOfYear(date, 365));
		println("本月的天数", DateUtils.getDaysOfMonth());
		println("date所在月的天数", DateUtils.getDaysOfMonth(date));
		println("本年的天数", DateUtils.getDaysOfYear());
		println("date所在年的天数", DateUtils.getDaysOfYear(date));
		println("本月的周数", DateUtils.getWeeksOfMonth());
		println("date所在月的周数", DateUtils.getWeeksOfMonth(date));
		println("本年的周数", DateUtils.getWeeksOfYear());
		println("date所在年的周数", DateUtils.getWeeksOfYear(date));
		println(null, null);
		println("今天是周的几（中文）", DateUtils.getWeekChinese());
		println("date是周的几（中文）", DateUtils.getWeekChinese(date));
		println("今天是星期几（中文）", DateUtils.getWeekChineseFull());
		println("date是星期几（中文）", DateUtils.getWeekChineseFull(date));
		println("今天是星期几（中文简）", DateUtils.getWeekChineseShort());
		println("date是星期几（中文简）", DateUtils.getWeekChineseShort(date));
		println(null, null);
		println("今天是几号", DateUtils.getDay());
		println("date是几号", DateUtils.getDay(date));
		println("今天是周几", DateUtils.getWeek());
		println("date是周几", DateUtils.getWeek(date));
		println("今天是几月份", DateUtils.getMonth());
		println("date是几月份", DateUtils.getMonth(date));
		println("今天是什么年份", DateUtils.getYear());
		println("date是什么年份", DateUtils.getYear(date));
		println("今年是否闰年", DateUtils.isLeapYear());
		println("date是否闰年", DateUtils.isLeapYear(date));
		println("0004年是否闰年", DateUtils.isLeapYear("0004"));
		println("1900年是否闰年", DateUtils.isLeapYear(1900));
		println("计算得出年龄", DateUtils.getAge(DateUtils.parseDate("1983-7-5")));
		println("计算得出年龄(精确)", DateUtils.getExactAge(DateUtils.parseDate("1983-7-2 11:34:59")));

	}

	public void println(String str, Object obj) {

		if (obj instanceof Date) {
			System.out.println(str + " : " + DateUtils.getDateTimeStr((Date) obj));
		} else {
			if (str == null) {
				System.out.println();
			} else {
				System.out.println(str + " : " + obj);
			}
		}

	}

}
