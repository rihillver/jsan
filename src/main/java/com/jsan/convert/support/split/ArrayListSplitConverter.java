package com.jsan.convert.support.split;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayListSplitConverter extends AbstractCollectionSplitConverter {

	@Override
	public ArrayList<?> convert(Object source, Type type) {

		return getCollectionConvert(ArrayList.class, source, type);
	}

}
