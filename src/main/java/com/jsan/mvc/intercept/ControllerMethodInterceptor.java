package com.jsan.mvc.intercept;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 控制器方法拦截器处理中心。
 * <ul>
 * <li>如果在 before 的时候设置了返回值，则不会调用原方法，而是直接返回所设置的值。</li>
 * <li>如果在 afterReturning 的时候设置了返回值，则返回这里设置的值。</li>
 * </ul>
 *
 */

public class ControllerMethodInterceptor implements MethodInterceptor {

	public static final ControllerMethodInterceptor INSTANCE = new ControllerMethodInterceptor();

	@Override
	public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {

		Invocation invocation = new Invocation();
		invocation.setTarget(target);
		invocation.setMethod(method);
		invocation.setArgs(args);
		invocation.setMethodProxy(proxy);

		InterceptService interceptService = InterceptServiceCache.get(method);

		List<Interceptor> list = interceptService.getInterceptorList();

		Object result;

		before(invocation, list);

		try {

			if (invocation.isHasReturnValue()) {
				result = invocation.getReturnValue();
			} else {
				result = proxy.invokeSuper(target, args);

				afterReturning(invocation, result, list);

				if (invocation.isHasReturnValue()) {
					result = invocation.getReturnValue();
				}
			}

		} catch (Exception e) {

			afterThrowing(invocation, e, list);

			throw new RuntimeException(e);

		} finally {

			after(invocation, list);
		}

		return result;
	}

	private void before(Invocation inv, List<Interceptor> list) {

		for (Interceptor interceptor : list) {
			interceptor.before(inv);
		}
	}

	private void after(Invocation inv, List<Interceptor> list) {

		for (Interceptor interceptor : list) {
			interceptor.after(inv);
		}
	}

	private void afterReturning(Invocation inv, Object result, List<Interceptor> list) {

		for (Interceptor interceptor : list) {
			interceptor.afterReturning(inv, result);
		}
	}

	private void afterThrowing(Invocation inv, Exception e, List<Interceptor> list) {

		for (Interceptor interceptor : list) {
			interceptor.afterThrowing(inv, e);
		}
	}

}
