package com.jsan.convert.support.json;

import java.lang.reflect.Type;

public class ShortArrayJsonConverter extends AbstractArrayJsonConverter {

	@Override
	public short[] convert(Object source, Type type) {

		return getArrayConvert(short[].class, source, type);
	}

}
