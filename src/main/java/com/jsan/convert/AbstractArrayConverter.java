package com.jsan.convert;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractArrayConverter extends AbstractRecursiveableConverter {

	@SuppressWarnings("unchecked")
	protected <T> T getArrayConvert(Class<T> clazz, Object source, Type type) {

		if (source == null) {
			return null;
		}

		T t;

		Type componentType; // 数组元素 Type 类型
		if (type instanceof GenericArrayType) {// 判断是否为参数化数组类型
			GenericArrayType genericArrayType = (GenericArrayType) type;
			componentType = genericArrayType.getGenericComponentType();
		} else {
			componentType = ((Class<?>) type).getComponentType();
		}

		Class<?> componentClass; // 数组元素 Class 类型
		if (componentType instanceof ParameterizedType) { // 判断是否为参数化类型
			ParameterizedType parameterizedType = (ParameterizedType) componentType;
			componentClass = (Class<?>) parameterizedType.getRawType();
		} else {
			componentClass = (Class<?>) componentType;
		}

		Converter converter = lookupConverter(componentClass);

		if (source.getClass().isArray()) {
			int length = Array.getLength(source);
			t = (T) Array.newInstance(componentClass, length);
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					Array.set(t, i, converter.convert(Array.get(source, i), componentType));
				}
			}
		} else if (Collection.class.isAssignableFrom(source.getClass())) {
			Collection<?> sources = (Collection<?>) source;
			int length = sources.size();
			t = (T) Array.newInstance(componentClass, length);
			if (length > 0) {
				Iterator<?> iterator = sources.iterator();
				int i = 0;
				while (iterator.hasNext()) {
					Array.set(t, i++, converter.convert(iterator.next(), componentType));
				}
			}
		} else {
			t = (T) Array.newInstance(componentClass, 1);
			Array.set(t, 0, converter.convert(source, componentType));
		}

		return t;
	}

}
