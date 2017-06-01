package com.jsan.convert.support.split;

import java.lang.reflect.Type;

public class ObjectArraySplitConverter extends AbstractArraySplitConverter {

	@Override
	public Object[] convert(Object source, Type type) {

		return getArrayConvert(Object[].class, source, type);
	}

}
