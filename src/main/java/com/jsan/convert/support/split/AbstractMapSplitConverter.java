package com.jsan.convert.support.split;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractMapConverter;

public abstract class AbstractMapSplitConverter extends AbstractMapConverter {

	@Override
	protected <T> T getMapConvert(Class<T> clazz, Object source, Type type) {

		source = SplitFuncUtils.handleSourceBySplitToMap(source, false);

		return super.getMapConvert(clazz, source, type);
	}

}
