package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ListJsonConverter extends AbstractCollectionJsonConverter {

	@Override
	public List<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
