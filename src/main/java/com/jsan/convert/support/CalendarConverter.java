package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.AbstractDateTimeConverter;

public class CalendarConverter extends AbstractDateTimeConverter {

	public CalendarConverter() {

	}

	public CalendarConverter(TimeZone timeZone) {

		super(timeZone);
	}

	public CalendarConverter(TimeZone timeZone, Locale locale) {

		super(timeZone, locale);
	}

	@Override
	public Calendar convert(Object source, Type type) {

		return getDateConvert(Calendar.class, source, type);
	}

}
