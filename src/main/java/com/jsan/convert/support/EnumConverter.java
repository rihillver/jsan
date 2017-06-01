package com.jsan.convert.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.jsan.convert.AbstractRecursiveableConverter;

public class EnumConverter extends AbstractRecursiveableConverter {

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Enum<?> convert(Object source, Type type) {

		source = getArrayOrCollectionFirstObject(source);

		if (source == null) {
			return null;
		}

		// 允许不同枚举类型的转换，因此不能如下简单粗暴的转换
		// if (source instanceof Enum) {
		// return (Enum<?>) source;
		// }

		Enum<?> e = null;

		Class enumType;

		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			enumType = (Class) parameterizedType.getActualTypeArguments()[0];
		} else {
			enumType = (Class) type;
		}

		if (source instanceof String || source instanceof Enum) {
			try {
				e = Enum.valueOf(enumType, source.toString());
			} catch (Exception ex) {
				// logging...
				// ex.printStackTrace();
			}
		}

		return e;
	}

}
