package com.jsan.convert.support.split;

import java.lang.reflect.Type;

public class IntegerArraySplitConverter extends AbstractArraySplitConverter {

	@Override
	public int[] convert(Object source, Type type) {

		return getArrayConvert(int[].class, source, type);
	}

}
