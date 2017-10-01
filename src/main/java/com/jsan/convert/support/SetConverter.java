package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import com.jsan.convert.AbstractCollectionConverter;

public class SetConverter extends AbstractCollectionConverter {

	@Override
	public Set<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
