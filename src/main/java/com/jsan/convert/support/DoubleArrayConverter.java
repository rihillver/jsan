package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public class DoubleArrayConverter extends AbstractArrayConverter {

	@Override
	public double[] convert(Object source, Type type) {

		return getArrayConvert(double[].class, source, type);
	}

}
