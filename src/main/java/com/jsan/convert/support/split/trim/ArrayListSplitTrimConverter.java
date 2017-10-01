package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayListSplitTrimConverter extends AbstractCollectionSplitTrimConverter {

	@Override
	public ArrayList<?> convert(Object source, Type type) {

		return getCollectionConvert(ArrayList.class, source, type);
	}

}
