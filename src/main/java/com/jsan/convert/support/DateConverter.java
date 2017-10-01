package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.AbstractDateTimeConverter;

public class DateConverter extends AbstractDateTimeConverter {

	public DateConverter() {

	}

	public DateConverter(TimeZone timeZone) {

		super(timeZone);
	}

	public DateConverter(TimeZone timeZone, Locale locale) {

		super(timeZone, locale);
	}

	@Override
	public Date convert(Object source, Type type) {

		return getDateConvert(Date.class, source, type);
	}

}
