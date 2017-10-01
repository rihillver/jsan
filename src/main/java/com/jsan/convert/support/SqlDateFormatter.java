package com.jsan.convert.support;

import java.sql.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.AbstractDateTimeFormatter;

public class SqlDateFormatter extends AbstractDateTimeFormatter {

	public SqlDateFormatter() {

		super();
	}

	public SqlDateFormatter(TimeZone timeZone) {

		super(timeZone);
	}

	public SqlDateFormatter(TimeZone timeZone, Locale locale) {

		super(timeZone, locale);
	}

	@Override
	public Date parse(String text) throws Exception {

		return getParse(Date.class, text);
	}

	@Override
	public String print(Object object) throws Exception {

		return getPrint(Date.class, object);
	}
}
