package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;

public class CollectionSplitTrimConverter extends AbstractCollectionSplitTrimConverter {

	@Override
	public Collection<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
