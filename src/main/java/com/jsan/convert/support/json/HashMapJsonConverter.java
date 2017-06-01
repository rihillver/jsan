package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.HashMap;

public class HashMapJsonConverter extends AbstractMapJsonConverter {

	@Override
	public HashMap<?, ?> convert(Object source, Type type) {

		return getMapConvert(HashMap.class, source, type);
	}

}
