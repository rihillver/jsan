package com.jsan.convert.support.split;

import java.lang.reflect.Type;

public class DoubleArraySplitConverter extends AbstractArraySplitConverter {

	@Override
	public double[] convert(Object source, Type type) {

		return getArrayConvert(double[].class, source, type);
	}

}
