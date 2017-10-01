package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

public class ByteArraySplitTrimConverter extends AbstractArraySplitTrimConverter {

	@Override
	public byte[] convert(Object source, Type type) {

		return getArrayConvert(byte[].class, source, type);
	}

}
