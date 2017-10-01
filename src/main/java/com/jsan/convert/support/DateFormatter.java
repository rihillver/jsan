package com.jsan.convert.support;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.AbstractDateTimeFormatter;

/**
 * DateFormat 是非线程安全的。
 *
 */

public class DateFormatter extends AbstractDateTimeFormatter {

	public DateFormatter() {

		super();
	}

	public DateFormatter(TimeZone timeZone) {

		super(timeZone);
	}

	public DateFormatter(TimeZone timeZone, Locale locale) {

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
