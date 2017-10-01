package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

public class SetSplitConverter extends AbstractCollectionSplitConverter {

	@Override
	public Set<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedHashSet.class, source, type);
	}

}
