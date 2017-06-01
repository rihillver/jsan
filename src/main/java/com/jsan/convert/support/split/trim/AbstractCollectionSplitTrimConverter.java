package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractCollectionConverter;
import com.jsan.convert.support.split.SplitFuncUtils;

public abstract class AbstractCollectionSplitTrimConverter extends AbstractCollectionConverter {

	@Override
	protected <T> T getCollectionConvert(Class<T> clazz, Object source, Type type) {

		source = SplitFuncUtils.handleSourceBySplitToArray(source, true);

		return super.getCollectionConvert(clazz, source, type);
	}
}
