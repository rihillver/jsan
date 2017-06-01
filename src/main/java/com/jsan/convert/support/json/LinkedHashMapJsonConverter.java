package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

public class LinkedHashMapJsonConverter extends AbstractMapJsonConverter {

	@Override
	public LinkedHashMap<?, ?> convert(Object source, Type type) {

		return getMapConvert(LinkedHashMap.class, source, type);
	}

}
