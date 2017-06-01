package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractRecursiveableConverter;

public class ObjectConverter extends AbstractRecursiveableConverter {

	@Override
	public Object convert(Object source, Type type) {

		return source;
	}

}
