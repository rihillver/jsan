package com.jsan.convert.support.json;

import java.lang.reflect.Type;

public class FloatArrayJsonConverter extends AbstractArrayJsonConverter {

	@Override
	public float[] convert(Object source, Type type) {

		return getArrayConvert(float[].class, source, type);
	}

}
