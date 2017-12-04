package com.jsan.convert;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public abstract class AbstractCollectionConverter extends AbstractRecursiveableConverter {

	/**
	 * Collection 的转换。
	 * 
	 * @param clazz
	 *            该参数不能是接口或抽象类，因为需要通过 newInstance() 实例化
	 * @param source
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getCollectionConvert(Class<T> clazz, Object source, Type type) {

		if (source == null) {
			return null;
		}

		Collection<Object> object = null;

		Class<?> actualClass;
		Type actualType;

		if (type instanceof ParameterizedType) {

			ParameterizedType parameterizedType = (ParameterizedType) type;
			actualType = parameterizedType.getActualTypeArguments()[0];

			TypeExtractor extractor = new TypeExtractor(actualType);
			actualType = extractor.getActualType();
			actualClass = extractor.getActualClass();

		} else {
			actualClass = Object.class;
			actualType = Object.class;
		}

		try {
			object = (Collection<Object>) clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Converter converter = lookupConverter(actualClass);

		if (source.getClass().isArray()) {
			int length = Array.getLength(source);
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					object.add(converter.convert(Array.get(source, i), actualType));
				}
			}
		} else if (Collection.class.isAssignableFrom(source.getClass())) {
			Collection<?> sources = (Collection<?>) source;
			for (Object obj : sources) {
				object.add(converter.convert(obj, actualType));
			}
		} else {
			object.add(converter.convert(source, actualType));
		}

		return clazz.cast(object);
	}

}
