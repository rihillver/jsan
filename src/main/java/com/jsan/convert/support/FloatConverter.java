package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractPrimitiveConverter;

public class FloatConverter extends AbstractPrimitiveConverter {

	@Override
	public Float convert(Object source, Type type) {

		return getNumberConvert(Float.class, source, type);
	}

}
