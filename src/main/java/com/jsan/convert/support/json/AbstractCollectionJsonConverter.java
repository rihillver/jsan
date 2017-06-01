package com.jsan.convert.support.json;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractCollectionConverter;

public abstract class AbstractCollectionJsonConverter extends AbstractCollectionConverter {

	@Override
	protected <T> T getCollectionConvert(Class<T> clazz, Object source, Type type) {

		source = JsonFuncUtils.handleSourceByJsonToArray(this, source);

		return super.getCollectionConvert(clazz, source, type);
	}
}
