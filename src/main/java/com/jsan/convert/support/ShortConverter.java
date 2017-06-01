package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractPrimitiveConverter;

public class ShortConverter extends AbstractPrimitiveConverter {

	@Override
	public Short convert(Object source, Type type) {

		return getNumberConvert(Short.class, source, type);
	}

}
