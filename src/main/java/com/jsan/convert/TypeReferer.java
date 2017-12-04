package com.jsan.convert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型类型提取工具类。
 *
 */

public class TypeReferer<T> {

	private final Type type;

	protected TypeReferer() {

		Type superClass = getClass().getGenericSuperclass();
		type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
	}

	public Type getType() {

		return type;
	}
}
