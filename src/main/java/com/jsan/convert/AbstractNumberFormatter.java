package com.jsan.convert;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.jsan.convert.AbstractFormatter;
import com.jsan.convert.cache.NumberFormatCache;

public abstract class AbstractNumberFormatter extends AbstractFormatter {

	protected Locale locale;

	public AbstractNumberFormatter() {

		this(null);
	}

	public AbstractNumberFormatter(Locale locale) {

		this.locale = locale;
	}

	protected NumberFormat getNumberFormat() {

		NumberFormat numberFormat;

		if (locale != null) {
			numberFormat = createNumberFormat();
		} else {
			numberFormat = NumberFormatCache.getNumberFormat(pattern); // 常规情况下从缓存中获取
		}

		return numberFormat;
	}

	protected NumberFormat createNumberFormat() {

		// NumberFormat numberFormat = locale == null ?
		// NumberFormat.getInstance() : NumberFormat.getInstance(locale);
		NumberFormat numberFormat = NumberFormat.getInstance(locale);

		if (pattern != null && (numberFormat instanceof DecimalFormat)) {
			((DecimalFormat) numberFormat).applyPattern(pattern);
		}

		return numberFormat;
	}

}
