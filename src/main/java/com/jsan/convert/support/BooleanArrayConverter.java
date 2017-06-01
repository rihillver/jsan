package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public class BooleanArrayConverter extends AbstractArrayConverter {

	@Override
	public boolean[] convert(Object source, Type type) {

		return getArrayConvert(boolean[].class, source, type);
	}

}
