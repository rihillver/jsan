package com.jsan.convert.support.json;

import java.lang.reflect.Type;

public class LongArrayJsonConverter extends AbstractArrayJsonConverter {

	@Override
	public long[] convert(Object source, Type type) {

		return getArrayConvert(long[].class, source, type);
	}

}
