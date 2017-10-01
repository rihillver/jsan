package com.jsan.convert.support.json;

import java.lang.reflect.Type;

public class ByteArrayJsonConverter extends AbstractArrayJsonConverter {

	@Override
	public byte[] convert(Object source, Type type) {

		return getArrayConvert(byte[].class, source, type);
	}

}
