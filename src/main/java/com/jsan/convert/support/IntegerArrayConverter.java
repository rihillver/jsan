package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public class IntegerArrayConverter extends AbstractArrayConverter {

	@Override
	public int[] convert(Object source, Type type) {

		return getArrayConvert(int[].class, source, type);
	}

}
