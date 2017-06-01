package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

public class ObjectArraySplitTrimConverter extends AbstractArraySplitTrimConverter {

	@Override
	public Object[] convert(Object source, Type type) {

		return getArrayConvert(Object[].class, source, type);
	}

}
