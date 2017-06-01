package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractPrimitiveConverter;

public class BooleanConverter extends AbstractPrimitiveConverter {

	@Override
	public Boolean convert(Object source, Type type) {

		return getBooleanConvert(source, type);
	}

}
