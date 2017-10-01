package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

public class LongArraySplitTrimConverter extends AbstractArraySplitTrimConverter {

	@Override
	public long[] convert(Object source, Type type) {

		return getArrayConvert(long[].class, source, type);
	}

}
