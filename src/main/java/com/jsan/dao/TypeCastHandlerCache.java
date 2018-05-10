package com.jsan.dao;

import java.util.HashMap;
import java.util.Map;

import com.jsan.convert.BeanProxyUtils;

/**
 * TypeCastHandler 缓存。
 *
 */

public class TypeCastHandlerCache {

	private static final Map<Class<?>, TypeCastHandler> handlerMap = new HashMap<Class<?>, TypeCastHandler>();

	public static TypeCastHandler getHandler(Class<?> beanClass, Class<? extends TypeCastHandler> typeCastHandlerClass) {

		TypeCastHandler handler = handlerMap.get(beanClass);

		if (handler == null) {
			synchronized (handlerMap) {
				handler = handlerMap.get(beanClass);
				if (handler == null) {
					try {
						handler = BeanProxyUtils.newInstance(typeCastHandlerClass);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					handlerMap.put(beanClass, handler);
				}
			}
		}

		return handler;
	}
	
}
