package com.jsan.convert.support;

import java.util.Locale;

import com.jsan.convert.AbstractNumberFormatter;

public class NumberFormatter extends AbstractNumberFormatter {

	public NumberFormatter() {

		super();
	}

	public NumberFormatter(Locale locale) {

		super(locale);
	}

	@Override
	public Number parse(String text) throws Exception {

		return getNumberFormat().parse(text);
	}

	@Override
	public String print(Object object) throws Exception {

		return getNumberFormat().format(object);
	}

}
