package com.jsan.convert.support.json;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayListJsonConverter extends AbstractCollectionJsonConverter {

	@Override
	public ArrayList<?> convert(Object source, Type type) {

		return getCollectionConvert(ArrayList.class, source, type);
	}

}
