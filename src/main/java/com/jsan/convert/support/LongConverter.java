package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractPrimitiveConverter;

public class LongConverter extends AbstractPrimitiveConverter {

	@Override
	public Long convert(Object source, Type type) {

		return getNumberConvert(Long.class, source, type);
	}

}
