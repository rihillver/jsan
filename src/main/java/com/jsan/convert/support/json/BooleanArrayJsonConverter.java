package com.jsan.convert.support.json;

import java.lang.reflect.Type;

public class BooleanArrayJsonConverter extends AbstractArrayJsonConverter {

	@Override
	public boolean[] convert(Object source, Type type) {

		return getArrayConvert(boolean[].class, source, type);
	}

}
