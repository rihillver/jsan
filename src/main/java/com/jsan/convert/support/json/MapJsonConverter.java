package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapJsonConverter extends AbstractMapJsonConverter {

	@Override
	public Map<?, ?> convert(Object source, Type type) {

		return getMapConvert(LinkedHashMap.class, source, type);
	}

}
