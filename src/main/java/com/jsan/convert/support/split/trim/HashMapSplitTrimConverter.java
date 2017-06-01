package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.HashMap;

public class HashMapSplitTrimConverter extends AbstractMapSplitTrimConverter {

	@Override
	public HashMap<?, ?> convert(Object source, Type type) {

		return getMapConvert(HashMap.class, source, type);
	}

}
