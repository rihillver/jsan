package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public class ByteArrayConverter extends AbstractArrayConverter {

	@Override
	public byte[] convert(Object source, Type type) {

		return getArrayConvert(byte[].class, source, type);
	}

}
