package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.LinkedList;

public class LinkedListJsonConverter extends AbstractCollectionJsonConverter {

	@Override
	public LinkedList<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
