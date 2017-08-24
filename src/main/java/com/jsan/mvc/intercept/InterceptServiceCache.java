package com.jsan.mvc.intercept;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 独立的拦截服务缓存。
 * <p>
 * 主要用于在控制器方法拦截器 ControllerMethodInterceptor 中调用。
 *
 */

public class InterceptServiceCache {

	private static final Map<Method, InterceptService> interceptServiceMap = new HashMap<Method, InterceptService>();

	public static InterceptService get(Method method) {

		return interceptServiceMap.get(method);
	}

	public static void put(Method method, InterceptService interceptService) {

		synchronized (interceptServiceMap) {
			interceptServiceMap.put(method, interceptService);
		}
	}

}
