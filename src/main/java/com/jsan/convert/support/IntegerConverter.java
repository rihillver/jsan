package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractPrimitiveConverter;

public class IntegerConverter extends AbstractPrimitiveConverter {

	@Override
	public Integer convert(Object source, Type type) {

		return getNumberConvert(Integer.class, source, type);
	}

}
