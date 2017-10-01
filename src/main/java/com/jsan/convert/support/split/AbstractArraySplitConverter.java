package com.jsan.convert.support.split;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public abstract class AbstractArraySplitConverter extends AbstractArrayConverter {

	/**
	 * 如果是字符串，则按逗号分割成字符串数组。
	 * 
	 * @param clazz
	 * @param source
	 * @param type
	 * @return
	 */
	@Override
	protected <T> T getArrayConvert(Class<T> clazz, Object source, Type type) {

		source = SplitFuncUtils.handleSourceBySplitToArray(source, false);

		return super.getArrayConvert(clazz, source, type);
	}

}
