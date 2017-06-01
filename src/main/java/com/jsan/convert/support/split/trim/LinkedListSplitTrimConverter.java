package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.LinkedList;

public class LinkedListSplitTrimConverter extends AbstractCollectionSplitTrimConverter {

	@Override
	public LinkedList<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
