package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;

import com.jsan.convert.AbstractCollectionConverter;

public class CollectionConverter extends AbstractCollectionConverter {

	@Override
	public Collection<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
