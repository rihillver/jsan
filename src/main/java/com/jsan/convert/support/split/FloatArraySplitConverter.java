package com.jsan.convert.support.split;

import java.lang.reflect.Type;

public class FloatArraySplitConverter extends AbstractArraySplitConverter {

	@Override
	public float[] convert(Object source, Type type) {

		return getArrayConvert(float[].class, source, type);
	}

}
