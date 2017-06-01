package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapSplitTrimConverter extends AbstractMapSplitTrimConverter {

	@Override
	public Map<?, ?> convert(Object source, Type type) {

		return getMapConvert(LinkedHashMap.class, source, type);
	}

}
