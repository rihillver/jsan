package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;

public class CollectionJsonConverter extends AbstractCollectionJsonConverter {

	@Override
	public Collection<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
