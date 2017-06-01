package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.ArrayDeque;

public class ArrayDequeJsonConverter extends AbstractCollectionJsonConverter {

	@Override
	public ArrayDeque<?> convert(Object source, Type type) {

		return getCollectionConvert(ArrayDeque.class, source, type);
	}

}
