package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.HashSet;

public class HashSetSplitConverter extends AbstractCollectionSplitConverter {

	@Override
	public HashSet<?> convert(Object source, Type type) {

		return getCollectionConvert(HashSet.class, source, type);
	}

}
