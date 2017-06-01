package com.jsan.convert.support.split;

import java.lang.reflect.Type;

public class LongArraySplitConverter extends AbstractArraySplitConverter {

	@Override
	public long[] convert(Object source, Type type) {

		return getArrayConvert(long[].class, source, type);
	}

}
