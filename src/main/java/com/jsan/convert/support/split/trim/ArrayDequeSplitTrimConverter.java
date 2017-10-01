package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.ArrayDeque;

public class ArrayDequeSplitTrimConverter extends AbstractCollectionSplitTrimConverter {

	@Override
	public ArrayDeque<?> convert(Object source, Type type) {

		return getCollectionConvert(ArrayDeque.class, source, type);
	}

}
