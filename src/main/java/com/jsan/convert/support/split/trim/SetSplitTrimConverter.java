package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

public class SetSplitTrimConverter extends AbstractCollectionSplitTrimConverter {

	@Override
	public Set<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
