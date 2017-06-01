package com.jsan.convert.support.json;

import java.lang.reflect.Type;

public class ObjectArrayJsonConverter extends AbstractArrayJsonConverter {

	@Override
	public Object[] convert(Object source, Type type) {

		return getArrayConvert(Object[].class, source, type);
	}

}
