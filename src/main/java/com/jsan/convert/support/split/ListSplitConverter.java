package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ListSplitConverter extends AbstractCollectionSplitConverter {

	@Override
	public List<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
