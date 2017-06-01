package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.HashSet;

public class HashSetJsonConverter extends AbstractCollectionJsonConverter {

	@Override
	public HashSet<?> convert(Object source, Type type) {

		return getCollectionConvert(HashSet.class, source, type);
	}

}
