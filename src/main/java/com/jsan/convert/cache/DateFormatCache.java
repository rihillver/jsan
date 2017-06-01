package com.jsan.convert.cache;

import java.text.DateFormat;

/**
 * DateFormat 缓存。
 *
 */

public class DateFormatCache {

	private static final ThreadLocal<DateFormatContainer> dateFormatContainerThreadLocal = new ThreadLocal<DateFormatContainer>() {
		protected DateFormatContainer initialValue() {
			return new DateFormatContainer();
		}
	};

	private static final ThreadLocal<DateFormat> dateFormatThreadLocal = new ThreadLocal<DateFormat>() {
		protected DateFormat initialValue() {
			return DateFormat.getDateTimeInstance();
		}
	};

	public static DateFormat getDateFormat(String pattern) {

		return dateFormatContainerThreadLocal.get().getDateFormat(pattern);
	}

	public static DateFormat getDateFormat() {

		return dateFormatThreadLocal.get();
	}
}
