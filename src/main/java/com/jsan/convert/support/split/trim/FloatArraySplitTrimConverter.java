package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

public class FloatArraySplitTrimConverter extends AbstractArraySplitTrimConverter {

	@Override
	public float[] convert(Object source, Type type) {

		return getArrayConvert(float[].class, source, type);
	}

}
