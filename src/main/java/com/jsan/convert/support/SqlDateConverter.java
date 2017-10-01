package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.AbstractDateTimeConverter;

public class SqlDateConverter extends AbstractDateTimeConverter {

	public SqlDateConverter() {

	}

	public SqlDateConverter(TimeZone timeZone) {

		super(timeZone);
	}

	public SqlDateConverter(TimeZone timeZone, Locale locale) {

		super(timeZone, locale);
	}

	@Override
	public Date convert(Object source, Type type) {

		return getDateConvert(Date.class, source, type);
	}

}
