package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public class FloatArrayConverter extends AbstractArrayConverter {

	@Override
	public float[] convert(Object source, Type type) {

		return getArrayConvert(float[].class, source, type);
	}

}
