package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.jsan.convert.AbstractCollectionConverter;

public class ListConverter extends AbstractCollectionConverter {

	@Override
	public List<?> convert(Object source, Type type) {

		return getCollectionConvert(LinkedList.class, source, type);
	}

}
