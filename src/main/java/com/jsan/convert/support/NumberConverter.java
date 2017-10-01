package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractPrimitiveConverter;

public class NumberConverter extends AbstractPrimitiveConverter {

	@Override
	public Number convert(Object source, Type type) {

		return getNumberConvert(Number.class, source, type);
	}

}
