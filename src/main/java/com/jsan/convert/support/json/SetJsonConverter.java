package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

public class SetJsonConverter extends AbstractCollectionJsonConverter {

	@Override
	public Set<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
