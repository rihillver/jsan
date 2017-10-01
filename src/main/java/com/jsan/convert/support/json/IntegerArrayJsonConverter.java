package com.jsan.convert.support.json;

import java.lang.reflect.Type;

public class IntegerArrayJsonConverter extends AbstractArrayJsonConverter {

	@Override
	public int[] convert(Object source, Type type) {

		return getArrayConvert(int[].class, source, type);
	}

}
