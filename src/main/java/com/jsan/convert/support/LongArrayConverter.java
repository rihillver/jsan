package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public class LongArrayConverter extends AbstractArrayConverter {

	@Override
	public long[] convert(Object source, Type type) {

		return getArrayConvert(long[].class, source, type);
	}

}
