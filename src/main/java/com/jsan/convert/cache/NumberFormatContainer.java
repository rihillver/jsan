package com.jsan.convert.cache;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class NumberFormatContainer {

	private final Map<String, NumberFormat> numberFormatMap = new HashMap<String, NumberFormat>();

	public NumberFormat getNumberFormat(String pattern) {

		NumberFormat numberFormat = numberFormatMap.get(pattern);

		if (numberFormat == null) {
			synchronized (numberFormatMap) {
				numberFormat = numberFormatMap.get(pattern);
				if (numberFormat == null) {
					numberFormat = createNumberFormat(pattern);
					numberFormatMap.put(pattern, numberFormat);
				}
			}
		}

		return numberFormat;
	}

	private NumberFormat createNumberFormat(String pattern) {

		NumberFormat numberFormat = NumberFormat.getInstance();

		if (pattern != null && (numberFormat instanceof DecimalFormat)) {
			((DecimalFormat) numberFormat).applyPattern(pattern);
		}

		return numberFormat;
	}

}
