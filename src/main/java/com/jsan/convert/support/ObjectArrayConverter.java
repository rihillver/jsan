package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public class ObjectArrayConverter extends AbstractArrayConverter {

	@Override
	public Object[] convert(Object source, Type type) {

		return getArrayConvert(Object[].class, source, type);
	}

}