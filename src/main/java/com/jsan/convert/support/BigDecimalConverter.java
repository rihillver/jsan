package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.jsan.convert.AbstractPrimitiveConverter;

public class BigDecimalConverter extends AbstractPrimitiveConverter {

	@Override
	public BigDecimal convert(Object source, Type type) {

		return getNumberConvert(BigDecimal.class, source, type);
	}

}
