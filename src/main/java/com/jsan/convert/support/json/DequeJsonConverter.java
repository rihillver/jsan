package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.Deque;
import java.util.LinkedList;

public class DequeJsonConverter extends AbstractCollectionJsonConverter {

	@Override
	public Deque<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
