package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.ArrayDeque;

import com.jsan.convert.AbstractCollectionConverter;

public class ArrayDequeConverter extends AbstractCollectionConverter {

	@Override
	public ArrayDeque<?> convert(Object source, Type type) {

		return getCollectionConvert(ArrayDeque.class, source, type);
	}

}
