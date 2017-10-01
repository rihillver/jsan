package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;

public class CollectionSplitConverter extends AbstractCollectionSplitConverter {

	@Override
	public Collection<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
