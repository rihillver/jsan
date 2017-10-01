package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.ArrayDeque;

public class ArrayDequeSplitConverter extends AbstractCollectionSplitConverter {

	@Override
	public ArrayDeque<?> convert(Object source, Type type) {

		return getCollectionConvert(ArrayDeque.class, source, type);
	}

}
