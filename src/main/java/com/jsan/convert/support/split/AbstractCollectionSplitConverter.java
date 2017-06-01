package com.jsan.convert.support.split;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractCollectionConverter;

public abstract class AbstractCollectionSplitConverter extends AbstractCollectionConverter {

	@Override
	protected <T> T getCollectionConvert(Class<T> clazz, Object source, Type type) {

		source = SplitFuncUtils.handleSourceBySplitToArray(source, false);

		return super.getCollectionConvert(clazz, source, type);
	}
}
