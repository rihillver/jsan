package com.jsan.convert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public abstract class AbstractMapConverter extends AbstractRecursiveableConverter {

	@SuppressWarnings("unchecked")
	protected <T> T getMapConvert(Class<T> clazz, Object source, Type type) {

		if (source == null) {
			return null;
		}

		Map<Object, Object> object = null;

		Type keyType;
		Class<?> keyClass;

		Type valueType;
		Class<?> valueClass;

		if (type instanceof ParameterizedType) {

			ParameterizedType parameterizedType = (ParameterizedType) type;

			keyType = parameterizedType.getActualTypeArguments()[0];

			GenericTypeExtractor extractor = new GenericTypeExtractor(keyType);
			keyType = extractor.getActualType();
			keyClass = extractor.getActualClass();

			valueType = parameterizedType.getActualTypeArguments()[1];

			extractor = new GenericTypeExtractor(valueType);
			valueType = extractor.getActualType();
			valueClass = extractor.getActualClass();

		} else {
			keyType = Object.class;
			keyClass = Object.class;

			valueType = Object.class;
			valueClass = Object.class;
		}

		Converter keyConverter = lookupConverter(keyClass);
		Converter valueConverter = lookupConverter(valueClass);

		if (Map.class.isAssignableFrom(source.getClass())) {

			try {
				object = (Map<Object, Object>) clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Map<?, ?> sourceMap = (Map<?, ?>) source;

			for (Map.Entry<?, ?> entry : sourceMap.entrySet()) {
				object.put(keyConverter.convert(entry.getKey(), keyType),
						valueConverter.convert(entry.getValue(), valueType));
			}

		}

		return clazz.cast(object);
	}

}
