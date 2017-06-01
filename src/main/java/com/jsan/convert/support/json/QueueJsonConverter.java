package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Queue;

public class QueueJsonConverter extends AbstractCollectionJsonConverter {

	@Override
	public Queue<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
