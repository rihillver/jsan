package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.HashSet;

import com.jsan.convert.AbstractCollectionConverter;

public class HashSetConverter extends AbstractCollectionConverter {

	@Override
	public HashSet<?> convert(Object source, Type type) {

		return getCollectionConvert(HashSet.class, source, type);
	}

}
