package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractMapConverter;
import com.jsan.convert.support.split.SplitFuncUtils;

public abstract class AbstractMapSplitTrimConverter extends AbstractMapConverter {

	@Override
	protected <T> T getMapConvert(Class<T> clazz, Object source, Type type) {

		source = SplitFuncUtils.handleSourceBySplitToMap(source, true);

		return super.getMapConvert(clazz, source, type);
	}

}
