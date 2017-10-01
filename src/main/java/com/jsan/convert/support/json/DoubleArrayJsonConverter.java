package com.jsan.convert.support.json;

import java.lang.reflect.Type;

public class DoubleArrayJsonConverter extends AbstractArrayJsonConverter {

	@Override
	public double[] convert(Object source, Type type) {

		return getArrayConvert(double[].class, source, type);
	}

}
