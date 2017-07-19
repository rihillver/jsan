package com.jsan.convert;

import java.lang.reflect.Type;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
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

			object = parseByFormatter((Class<?>) type, (String) source); // 格式化处理

			if (object instanceof Calendar) {
				object = ((Calendar) object).getTime();
			}

			if (!(object instanceof Date)) {
				try {
					object = getDateFormat().parse((String) source);
				} catch (ParseException e) {
					logger.warn("DateFormat cannot parse: {} [by {}]", source, getDateFormat().getClass().getName());
					object = null;
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

	protected static final ThreadLocal<DateFormat> dateFormatThreadLocal = new ThreadLocal<DateFormat>();

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
