package com.jsan.convert.cache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DateFormatContainer {

	private final Map<String, DateFormat> dateFormatMap = new HashMap<String, DateFormat>();

	public DateFormat getDateFormat(String pattern) {

		DateFormat dateFormat = dateFormatMap.get(pattern);

		if (dateFormat == null) {
			synchronized (dateFormatMap) {
				dateFormat = dateFormatMap.get(pattern);
				if (dateFormat == null) {
					dateFormat = createDateFormat(pattern);
					dateFormatMap.put(pattern, dateFormat);
				}
			}
		}

		return dateFormat;
	}

	private DateFormat createDateFormat(String pattern) {

		DateFormat dateFormat = DateFormat.getDateTimeInstance();

		if (pattern != null && (dateFormat instanceof SimpleDateFormat)) {
			((SimpleDateFormat) dateFormat).applyPattern(pattern);
		}

		return dateFormat;
	}
}
