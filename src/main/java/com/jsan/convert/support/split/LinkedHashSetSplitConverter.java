package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;

public class LinkedHashSetSplitConverter extends AbstractCollectionSplitConverter {

	@Override
	public LinkedHashSet<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
