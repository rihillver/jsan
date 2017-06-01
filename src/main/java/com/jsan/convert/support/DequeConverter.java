package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.Deque;
import java.util.LinkedList;

import com.jsan.convert.AbstractCollectionConverter;

public class DequeConverter extends AbstractCollectionConverter {

	@Override
	public Deque<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
