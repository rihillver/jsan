package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapSplitConverter extends AbstractMapSplitConverter {

	@Override
	public Map<?, ?> convert(Object source, Type type) {

		return getMapConvert(LinkedHashMap.class, source, type);
	}

}
