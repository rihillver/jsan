package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.Map;

import com.jsan.convert.AbstractRecursiveableConverter;
import com.jsan.convert.BeanConvertUtils;

public class ObjectConverter extends AbstractRecursiveableConverter {

	@Override
	public Object convert(Object source, Type type) {

		// 如果source是Map的情况，则尝试将Map转换成Bean
		if (Map.class.isAssignableFrom(source.getClass())) {
			return BeanConvertUtils.getBean((Class<?>) type, (Map<?, ?>) source, getConvertService());
		}

		return source;
	}

}
