package com.jsan.convert;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.cache.DateFormatCache;

public abstract class AbstractDateTimeFormatter extends AbstractFormatter {

	protected Locale locale;
	protected TimeZone timeZone;

	public AbstractDateTimeFormatter() {

	}

	public AbstractDateTimeFormatter(TimeZone timeZone) {

		this(timeZone, null);
	}

	public AbstractDateTimeFormatter(TimeZone timeZone, Locale locale) {

		this.timeZone = timeZone;
		this.locale = locale;

	}

	protected <T> T getParse(Class<T> clazz, String text) throws Exception {

		Date date = getDateFormat().parse(text);

		if (clazz == Date.class) {
			return clazz.cast(date);
		} else if (clazz == Timestamp.class) {
			Timestamp timestamp = new Timestamp(date.getTime());
			return clazz.cast(timestamp);
		} else if (clazz == java.sql.Date.class) {
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			return clazz.cast(sqlDate);
		} else if (clazz == Time.class) {
			Time time = new Time(date.getTime());
			return clazz.cast(time);
		} else {
			return clazz.cast(date);
		}
	}

	protected String getPrint(Class<?> clazz, Object object) throws Exception {

		return getDateFormat().format(object);
	}

	protected DateFormat getDateFormat() {

		DateFormat dateFormat;

		if (locale != null || timeZone != null) {
			dateFormat = createDateFormat();
		} else {
			dateFormat = DateFormatCache.getDateFormat(pattern); // 常规情况下从缓存中获取
		}

		return dateFormat;

	}

	protected DateFormat createDateFormat() {

		DateFormat dateFormat = locale == null ? DateFormat.getDateTimeInstance()
				: DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);

		if (timeZone != null) {
			dateFormat.setTimeZone(timeZone);
		}

		if (pattern != null && (dateFormat instanceof SimpleDateFormat)) {
			((SimpleDateFormat) dateFormat).applyPattern(pattern);
		}

		return dateFormat;
	}

}
