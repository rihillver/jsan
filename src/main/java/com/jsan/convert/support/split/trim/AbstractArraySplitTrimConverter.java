package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;
import com.jsan.convert.support.split.SplitFuncUtils;

public abstract class AbstractArraySplitTrimConverter extends AbstractArrayConverter {

	/**
	 * 如果是字符串，则按逗号分割成字符串数组（字符串去除首尾空白）。
	 * 
	 * @param clazz
	 * @param source
	 * @param type
	 * @return
	 */
	@Override
	protected <T> T getArrayConvert(Class<T> clazz, Object source, Type type) {

		source = SplitFuncUtils.handleSourceBySplitToArray(source, true);

		return super.getArrayConvert(clazz, source, type);
	}

}
