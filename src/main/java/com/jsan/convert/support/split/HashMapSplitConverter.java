package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.HashMap;

public class HashMapSplitConverter extends AbstractMapSplitConverter {

	@Override
	public HashMap<?, ?> convert(Object source, Type type) {

		return getMapConvert(HashMap.class, source, type);
	}

}
