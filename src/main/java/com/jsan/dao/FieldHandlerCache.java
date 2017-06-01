package com.jsan.dao;

import java.util.HashMap;
import java.util.Map;

import com.jsan.convert.BeanProxyUtils;

/**
 * FieldHandler 缓存。
 *
 */

public class FieldHandlerCache {

	private static final Map<Class<?>, FieldHandler> fieldHandlerMap = new HashMap<Class<?>, FieldHandler>();

	public static FieldHandler getFieldHandler(Class<?> beanClass, Class<? extends FieldHandler> fieldHandlerClass) {

		FieldHandler fieldHandler = fieldHandlerMap.get(beanClass);

		if (fieldHandler == null) {
			synchronized (fieldHandlerMap) {
				fieldHandler = fieldHandlerMap.get(beanClass);
				if (fieldHandler == null) {
					try {
						fieldHandler = BeanProxyUtils.newInstance(fieldHandlerClass);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					fieldHandlerMap.put(beanClass, fieldHandler);
				}
			}
		}

		return fieldHandler;
	}

}
