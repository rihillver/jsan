package com.jsan.convert.support;

import java.sql.Time;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.AbstractDateTimeFormatter;

public class SqlTimeFormatter extends AbstractDateTimeFormatter {

	public SqlTimeFormatter() {

		super();
	}

	public SqlTimeFormatter(TimeZone timeZone) {

		super(timeZone);
	}

	public SqlTimeFormatter(TimeZone timeZone, Locale locale) {

		super(timeZone, locale);
	}

	@Override
	public Time parse(String text) throws Exception {

		return getParse(Time.class, text);
	}

	@Override
	public String print(Object object) throws Exception {

		return getPrint(Time.class, object);
	}
}
