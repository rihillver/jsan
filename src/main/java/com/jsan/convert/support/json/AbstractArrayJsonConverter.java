package com.jsan.convert.support.json;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public abstract class AbstractArrayJsonConverter extends AbstractArrayConverter {

	@Override
	protected <T> T getArrayConvert(Class<T> clazz, Object source, Type type) {

		source = JsonFuncUtils.handleSourceByJsonToArray(source);

		return super.getArrayConvert(clazz, source, type);
	}

}
