package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.Deque;
import java.util.LinkedList;

public class DequeSplitTrimConverter extends AbstractCollectionSplitTrimConverter {

	@Override
	public Deque<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
