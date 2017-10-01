package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ListSplitTrimConverter extends AbstractCollectionSplitTrimConverter {

	@Override
	public List<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
