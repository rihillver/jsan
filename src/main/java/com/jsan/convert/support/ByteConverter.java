package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractPrimitiveConverter;

public class ByteConverter extends AbstractPrimitiveConverter {

	@Override
	public Byte convert(Object source, Type type) {

		return getNumberConvert(Byte.class, source, type);
	}

}
