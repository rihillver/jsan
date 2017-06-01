package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

public class DoubleArraySplitTrimConverter extends AbstractArraySplitTrimConverter {

	@Override
	public double[] convert(Object source, Type type) {

		return getArrayConvert(double[].class, source, type);
	}

}
