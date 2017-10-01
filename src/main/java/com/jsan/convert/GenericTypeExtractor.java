package com.jsan.convert;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * 泛型类型提取器。
 *
 */

public class GenericTypeExtractor {

	private Type actualType;
	private Class<?> actualClass;

	public GenericTypeExtractor(Type type) {

		actualType = type;

		if (actualType instanceof ParameterizedType) { // 参数化类型

			ParameterizedType actualParameterizedType = (ParameterizedType) actualType;
			actualClass = (Class<?>) actualParameterizedType.getRawType();

		} else if (actualType instanceof WildcardType) { // 通配符类型表达式

			WildcardType wildcardType = (WildcardType) actualType;
			Type[] lowerBoundTypes = wildcardType.getLowerBounds();

			if (lowerBoundTypes.length > 0) {

				Type lowerBoundType = lowerBoundTypes[0]; // 下边界
				if (lowerBoundType instanceof ParameterizedType) {

					ParameterizedType lowerBoundParameterizedType = (ParameterizedType) lowerBoundType;
					actualClass = (Class<?>) lowerBoundParameterizedType.getRawType();
					actualType = actualClass;

				} else {

					actualClass = (Class<?>) lowerBoundType;
					actualType = actualClass;

				}
			} else {

				Type upperBoundType = wildcardType.getUpperBounds()[0]; // 上边界

				if (upperBoundType instanceof ParameterizedType) {

					ParameterizedType upperBoundParameterizedType = (ParameterizedType) upperBoundType;
					actualClass = (Class<?>) upperBoundParameterizedType.getRawType();
					actualType = actualClass;

				} else {

					actualClass = (Class<?>) upperBoundType;
					actualType = actualClass;

				}
			}
		} else if (actualType instanceof GenericArrayType) { // 参数化数组类型

			GenericArrayType genericArrayType = (GenericArrayType) type;
			Type arrayType = genericArrayType.getGenericComponentType();

			GenericTypeExtractor extractor = new GenericTypeExtractor(arrayType); // 泛型数组，极其罕见的
			Class<?> arrayClass = extractor.getActualClass();

			actualClass = Array.newInstance(arrayClass, 0).getClass(); // 获取泛型数组类型的类比较麻烦

		} else {
			actualClass = (Class<?>) actualType;
		}

	}

	public Type getActualType() {

		return actualType;
	}

	public Class<?> getActualClass() {

		return actualClass;
	}

}