package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.LinkedList;

import com.jsan.convert.AbstractCollectionConverter;

public class LinkedListConverter extends AbstractCollectionConverter {

	@Override
	public LinkedList<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
