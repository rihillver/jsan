package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.jsan.convert.AbstractCollectionConverter;

public class ArrayListConverter extends AbstractCollectionConverter {

	@Override
	public ArrayList<?> convert(Object source, Type type) {

		return getCollectionConvert(ArrayList.class, source, type);
	}

}
