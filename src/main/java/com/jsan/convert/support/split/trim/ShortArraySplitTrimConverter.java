package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

public class ShortArraySplitTrimConverter extends AbstractArraySplitTrimConverter {

	@Override
	public short[] convert(Object source, Type type) {

		return getArrayConvert(short[].class, source, type);
	}

}
