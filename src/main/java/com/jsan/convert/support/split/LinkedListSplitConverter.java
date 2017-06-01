package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.LinkedList;

public class LinkedListSplitConverter extends AbstractCollectionSplitConverter {

	@Override
	public LinkedList<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
