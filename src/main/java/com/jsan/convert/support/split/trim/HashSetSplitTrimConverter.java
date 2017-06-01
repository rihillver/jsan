package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.HashSet;

public class HashSetSplitTrimConverter extends AbstractCollectionSplitTrimConverter {

	@Override
	public HashSet<?> convert(Object source, Type type) {

		return getCollectionConvert(HashSet.class, source, type);
	}

}
