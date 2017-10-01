package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractPrimitiveConverter;

public class DoubleConverter extends AbstractPrimitiveConverter {

	@Override
	public Double convert(Object source, Type type) {

		return getNumberConvert(Double.class, source, type);
	}

}
