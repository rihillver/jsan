package com.jsan.convert;

import java.lang.reflect.Type;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public abstract class AbstractDateTimeConverter extends AbstractRecursiveableConverter {

	protected Locale locale;
	protected TimeZone timeZone;

	public AbstractDateTimeConverter() {

	}

	public AbstractDateTimeConverter(TimeZone timeZone) {

		this(timeZone, null);
	}

	public AbstractDateTimeConverter(TimeZone timeZone, Locale locale) {

		this.timeZone = timeZone;
		this.locale = locale;
	}

	protected <T> T getDateConvert(Class<T> clazz, Object source, Type type) {

		source = getArrayOrCollectionFirstObject(source);

		if (source == null) {
			return null;
		}

		Object object = null;

		if (source instanceof Date) {
			object = getDateObject(clazz, (Date) source);
		} else if (source instanceof Long) {
			object = getDateObject(clazz, new Date((Long) source));
		} else if (source instanceof Calendar) {
			object = getDateObject(clazz, ((Calendar) source).getTime());
		} else if (source instanceof String) {

			String dateStr = (String) source;

			object = parseByFormatter((Class<?>) type, dateStr); // 指定的格式化处理

			if (object instanceof Calendar) {
				object = ((Calendar) object).getTime();
			}

			if (!(object instanceof Date)) {
				try {
					object = getCommonDateFormat(dateStr).parse(dateStr);
				} catch (ParseException e) {
					try {
						object = getDateTimeFormat().parse(dateStr); // 不得已的情况下第一次以DateFormat.getDateTimeInstance()进行尝试（精确到秒）
					} catch (ParseException ex) {
						try {
							object = getDateFormat().parse(dateStr); // 不得已的情况下第二次以DateFormat.getDateInstance()进行尝试（精确到天）
						} catch (ParseException exc) {
							logger.warn("DateFormat cannot parse: {}", dateStr);
							object = null;
						}
					}
				}
			}
			object = getDateObject(clazz, (Date) object);
		}

		return clazz.cast(object);
	}

	protected Object getDateObject(Class<?> clazz, Date date) {

		if (date == null) {
			return null;
		}

		if (clazz == Date.class) {
			return date;
		} else if (clazz == Timestamp.class) {
			return new Timestamp(date.getTime());
		} else if (clazz == java.sql.Date.class) {
			return new java.sql.Date(date.getTime());
		} else if (clazz == Time.class) {
			return new Time(date.getTime());
		} else if (clazz == Calendar.class) {
			Calendar calendar = getCalendar();
			calendar.setTime(date);
			return calendar;
		} else {
			return date;
		}
	}

	protected static final String COMMON_DATE_FORMAT = "yyyy-MM-dd";
	protected static final String COMMON_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	protected static final String COMMON_DATE_TIME_MILLI_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	protected static final ThreadLocal<DateFormat> commonDateFormatThreadLocal = new ThreadLocal<DateFormat>();
	protected static final ThreadLocal<DateFormat> commonDateTimeFormatThreadLocal = new ThreadLocal<DateFormat>();
	protected static final ThreadLocal<DateFormat> commonDateTimeMilliFormatThreadLocal = new ThreadLocal<DateFormat>();

	protected DateFormat createCommonDateFormat(String pattern) {

		DateFormat dateFormat;

		if (locale == null) {
			dateFormat = new SimpleDateFormat(pattern);
		} else {
			dateFormat = new SimpleDateFormat(pattern, locale);
		}

		if (timeZone != null) {
			dateFormat.setTimeZone(timeZone);
		}

		return dateFormat;
	}

	protected DateFormat getCommonDateFormat(String dateStr) {

		DateFormat dateFormat;

		if (dateStr.length() == COMMON_DATE_FORMAT.length()) {
			dateFormat = commonDateFormatThreadLocal.get();
			if (dateFormat == null) {
				dateFormat = createCommonDateFormat(COMMON_DATE_FORMAT);
				commonDateFormatThreadLocal.set(dateFormat);
			}
		} else if (dateStr.length() == COMMON_DATE_TIME_FORMAT.length()) {
			dateFormat = commonDateTimeFormatThreadLocal.get();
			if (dateFormat == null) {
				dateFormat = createCommonDateFormat(COMMON_DATE_TIME_FORMAT);
				commonDateTimeFormatThreadLocal.set(dateFormat);
			}
		} else {
			dateFormat = commonDateTimeMilliFormatThreadLocal.get();
			if (dateFormat == null) {
				dateFormat = createCommonDateFormat(COMMON_DATE_TIME_MILLI_FORMAT);
				commonDateTimeMilliFormatThreadLocal.set(dateFormat);
			}
		}

		return dateFormat;
	}

	protected static final ThreadLocal<DateFormat> dateFormatThreadLocal = new ThreadLocal<DateFormat>();
	protected static final ThreadLocal<DateFormat> dateTimeFormatThreadLocal = new ThreadLocal<DateFormat>();

	protected DateFormat getDateFormat() {

		DateFormat dateFormat = dateFormatThreadLocal.get();

		if (dateFormat == null) {
			dateFormat = createDateFormat();
			dateFormatThreadLocal.set(dateFormat);
		}

		return dateFormat;
	}

	protected DateFormat createDateFormat() {

		DateFormat dateFormat;

		if (locale == null) {
			dateFormat = DateFormat.getDateInstance();
		} else {
			dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
		}

		if (timeZone != null) {
			dateFormat.setTimeZone(timeZone);
		}

		return dateFormat;
	}

	protected DateFormat getDateTimeFormat() {

		DateFormat dateFormat = dateTimeFormatThreadLocal.get();

		if (dateFormat == null) {
			dateFormat = createDateTimeFormat();
			dateTimeFormatThreadLocal.set(dateFormat);
		}

		return dateFormat;
	}

	protected DateFormat createDateTimeFormat() {

		DateFormat dateFormat;

		if (locale == null) {
			dateFormat = DateFormat.getDateTimeInstance();
		} else {
			dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
		}

		if (timeZone != null) {
			dateFormat.setTimeZone(timeZone);
		}

		return dateFormat;
	}

	protected Calendar getCalendar() {

		Calendar calendar;

		if (locale == null) {
			calendar = Calendar.getInstance();
		} else {
			calendar = Calendar.getInstance(locale);
		}

		if (timeZone != null) {
			calendar.setTimeZone(timeZone);
		}

		return calendar;
	}

}
