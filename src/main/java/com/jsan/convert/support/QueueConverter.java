package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Queue;

import com.jsan.convert.AbstractCollectionConverter;

public class QueueConverter extends AbstractCollectionConverter {

	@Override
	public Queue<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
