package com.jsan.convert.support.split;

import java.lang.reflect.Type;

public class ByteArraySplitConverter extends AbstractArraySplitConverter {

	@Override
	public byte[] convert(Object source, Type type) {

		return getArrayConvert(byte[].class, source, type);
	}

}
