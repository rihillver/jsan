package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Queue;

public class QueueSplitConverter extends AbstractCollectionSplitConverter {

	@Override
	public Queue<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
