package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

public class LinkedHashMapSplitConverter extends AbstractMapSplitConverter {

	@Override
	public LinkedHashMap<?, ?> convert(Object source, Type type) {

		return getMapConvert(LinkedHashMap.class, source, type);
	}

}
