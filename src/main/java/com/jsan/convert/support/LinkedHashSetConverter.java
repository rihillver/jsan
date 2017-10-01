package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;

import com.jsan.convert.AbstractCollectionConverter;

public class LinkedHashSetConverter extends AbstractCollectionConverter {

	@Override
	public LinkedHashSet<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
