package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.Deque;
import java.util.LinkedList;

public class DequeSplitConverter extends AbstractCollectionSplitConverter {

	@Override
	public Deque<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
