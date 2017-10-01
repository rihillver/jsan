package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Queue;

public class QueueSplitTrimConverter extends AbstractCollectionSplitTrimConverter {

	@Override
	public Queue<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
