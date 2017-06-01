package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

public class BooleanArraySplitTrimConverter extends AbstractArraySplitTrimConverter {

	@Override
	public boolean[] convert(Object source, Type type) {

		return getArrayConvert(boolean[].class, source, type);
	}

}
