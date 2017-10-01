package com.jsan.convert.support;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.jsan.convert.AbstractDateTimeFormatter;

public class CalendarFormatter extends AbstractDateTimeFormatter {

	public CalendarFormatter() {

		super();
	}

	public CalendarFormatter(TimeZone timeZone) {

		super(timeZone);
	}

	public CalendarFormatter(TimeZone timeZone, Locale locale) {

		super(timeZone, locale);
	}

	protected Calendar getCalendar() {

		return Calendar.getInstance();
	}

	@Override
	public Calendar parse(String text) throws Exception {

		Date date = getParse(Date.class, text);
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar;
	}

	@Override
	public String print(Object object) throws Exception {

		Date date = ((Calendar) object).getTime();
		return getPrint(Date.class, date);
	}

}
