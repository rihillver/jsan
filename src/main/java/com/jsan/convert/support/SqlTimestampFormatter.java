package com.jsan.convert.support;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.AbstractDateTimeFormatter;

public class SqlTimestampFormatter extends AbstractDateTimeFormatter {

	public SqlTimestampFormatter() {

		super();
	}

	public SqlTimestampFormatter(TimeZone timeZone) {

		super(timeZone);
	}

	public SqlTimestampFormatter(TimeZone timeZone, Locale locale) {

		super(timeZone, locale);
	}

	@Override
	public Timestamp parse(String text) throws Exception {

		return getParse(Timestamp.class, text);
	}

	@Override
	public String print(Object object) throws Exception {

		return getPrint(Timestamp.class, object);
	}
}