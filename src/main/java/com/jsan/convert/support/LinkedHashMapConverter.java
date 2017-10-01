package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import com.jsan.convert.AbstractMapConverter;

public class LinkedHashMapConverter extends AbstractMapConverter {

	@Override
	public LinkedHashMap<?, ?> convert(Object source, Type type) {

		return getMapConvert(LinkedHashMap.class, source, type);
	}

}
