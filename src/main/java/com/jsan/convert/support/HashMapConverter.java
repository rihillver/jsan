package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.jsan.convert.AbstractMapConverter;

public class HashMapConverter extends AbstractMapConverter {

	@Override
	public HashMap<?, ?> convert(Object source, Type type) {

		return getMapConvert(HashMap.class, source, type);
	}

}
