package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

public class IntegerArraySplitTrimConverter extends AbstractArraySplitTrimConverter {

	@Override
	public int[] convert(Object source, Type type) {

		return getArrayConvert(int[].class, source, type);
	}

}
