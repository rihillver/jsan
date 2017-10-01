package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

public class LinkedHashMapSplitTrimConverter extends AbstractMapSplitTrimConverter {

	@Override
	public LinkedHashMap<?, ?> convert(Object source, Type type) {

		return getMapConvert(LinkedHashMap.class, source, type);
	}

}
