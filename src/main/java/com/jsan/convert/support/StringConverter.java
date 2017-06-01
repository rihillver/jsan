package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractRecursiveableConverter;

public class StringConverter extends AbstractRecursiveableConverter {

	@Override
	public String convert(Object source, Type type) {

		Object object = getArrayOrCollectionFirstObject(source);

		object = printByFormatter(object);

		return object == null ? null : object.toString();
	}

}
