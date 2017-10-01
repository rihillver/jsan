package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jsan.convert.AbstractMapConverter;

public class MapConverter extends AbstractMapConverter {

	@Override
	public Map<?, ?> convert(Object source, Type type) {

		return getMapConvert(LinkedHashMap.class, source, type);
	}

}
