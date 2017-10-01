package com.jsan.convert.support.json;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractMapConverter;

public abstract class AbstractMapJsonConverter extends AbstractMapConverter {

	@Override
	protected <T> T getMapConvert(Class<T> clazz, Object source, Type type) {

		source = JsonFuncUtils.handleSourceForJsonToMap(source);

		return super.getMapConvert(clazz, source, type);
	}

	

}
