package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.AbstractDateTimeConverter;

public class SqlTimestampConverter extends AbstractDateTimeConverter {

	public SqlTimestampConverter() {

	}

	public SqlTimestampConverter(TimeZone timeZone) {

		super(timeZone);
	}

	public SqlTimestampConverter(TimeZone timeZone, Locale locale) {

		super(timeZone, locale);
	}

	@Override
	public Timestamp convert(Object source, Type type) {

		return getDateConvert(Timestamp.class, source, type);
	}

}
