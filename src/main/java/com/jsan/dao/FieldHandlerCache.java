package com.jsan.dao;

import java.util.HashMap;
import java.util.Map;

import com.jsan.convert.BeanProxyUtils;

/**
 * FieldHandler 缓存。
 *
 */

public class FieldHandlerCache {

	private static final Map<Class<?>, FieldValueHandler> valueHandlerMap = new HashMap<Class<?>, FieldValueHandler>();

	public static FieldValueHandler getValueHandler(Class<?> beanClass, Class<? extends FieldValueHandler> fieldValueHandlerClass) {

		FieldValueHandler handler = valueHandlerMap.get(beanClass);

		if (handler == null) {
			synchronized (valueHandlerMap) {
				handler = valueHandlerMap.get(beanClass);
				if (handler == null) {
					try {
						handler = BeanProxyUtils.newInstance(fieldValueHandlerClass);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					valueHandlerMap.put(beanClass, handler);
				}
			}
		}

		return handler;
	}
	
	private static final Map<Class<?>, FieldNameHandler> nameHandlerMap = new HashMap<Class<?>, FieldNameHandler>();
	
	public static FieldNameHandler getNameHandler(Class<?> beanClass, Class<? extends FieldNameHandler> fieldNameHandlerClass) {
		
		FieldNameHandler handler = nameHandlerMap.get(beanClass);
		
		if (handler == null) {
			synchronized (nameHandlerMap) {
				handler = nameHandlerMap.get(beanClass);
				if (handler == null) {
					try {
						handler = BeanProxyUtils.newInstance(fieldNameHandlerClass);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					nameHandlerMap.put(beanClass, handler);
				}
			}
		}
		
		return handler;
	}

}
