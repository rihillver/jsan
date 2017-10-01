package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public class ShortArrayConverter extends AbstractArrayConverter {

	@Override
	public short[] convert(Object source, Type type) {

		return getArrayConvert(short[].class, source, type);
	}

}
