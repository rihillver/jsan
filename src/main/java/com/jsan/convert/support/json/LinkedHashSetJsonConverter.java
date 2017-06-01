package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;

public class LinkedHashSetJsonConverter extends AbstractCollectionJsonConverter {

	@Override
	public LinkedHashSet<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
