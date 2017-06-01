package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.AbstractDateTimeConverter;

public class SqlTimeConverter extends AbstractDateTimeConverter {

	public SqlTimeConverter() {

	}

	public SqlTimeConverter(TimeZone timeZone) {

		super(timeZone);
	}

	public SqlTimeConverter(TimeZone timeZone, Locale locale) {

		super(timeZone, locale);
	}

	@Override
	public Time convert(Object source, Type type) {

		return getDateConvert(Time.class, source, type);
	}

}
