package com.jsan.convert.cache;

import java.text.NumberFormat;

/**
 * NumberFormat 缓存。
 *
 */

public class NumberFormatCache {

	private static final ThreadLocal<NumberFormatContainer> numberFormatContainerThreadLocal = new ThreadLocal<NumberFormatContainer>() {
		protected NumberFormatContainer initialValue() {
			return new NumberFormatContainer();
		}
	};

	private static final ThreadLocal<NumberFormat> numberFormatThreadLocal = new ThreadLocal<NumberFormat>() {
		protected NumberFormat initialValue() {
			return NumberFormat.getInstance();
		}
	};

	public static NumberFormat getNumberFormat(String pattern) {

		return numberFormatContainerThreadLocal.get().getNumberFormat(pattern);
	}

	public static NumberFormat getNumberFormat() {

		return numberFormatThreadLocal.get();
	}

}
