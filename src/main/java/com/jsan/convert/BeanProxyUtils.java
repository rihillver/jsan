package com.jsan.convert;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import com.jsan.convert.cache.BeanInformationCache;

/**
 * 基于 Cglib 的动态代理。
 *
 */

public class BeanProxyUtils {

	private static final Map<Object, Set<String>> daoBeanExcludeFieldSetMap = new WeakHashMap<Object, Set<String>>();

	private static final MethodInterceptor daoBeanWriteMethodInterceptor = new MethodInterceptor() {

		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

			String methodName = method.getName();
			if (methodName.startsWith("set") && methodName.length() > 3) {
				removeIncludeField(obj, methodName);
			}

			return proxy.invokeSuper(obj, args);
		}
	};

	/**
	 * 如果访问权限不足时则使用 Cglib 动态代理的方式创建对象。
	 * 
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 */
	public static <T> T newInstance(Class<T> clazz) throws InstantiationException {

		try {
			return clazz.newInstance();
		} catch (IllegalAccessException e) {
			return getObject(clazz);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getDaoBean(Class<T> beanClass) {

		T bean = getObject(beanClass, daoBeanWriteMethodInterceptor);
		setDaoBeanExcludeFieldSet(bean,
				(Set<String>) ((LinkedHashSet<String>) BeanInformationCache.getFieldSet(beanClass)).clone());

		return bean;
	}

	public static boolean isDaoBean(Object obj) {

		return getDaoBeanExcludeFieldSet(obj) != null;
	}

	/**
	 * 返回一个无任何拦截的纯粹代理对象。
	 * 
	 * @param beanClass
	 * @return
	 */
	public static <T> T getObject(Class<T> beanClass) {

		return getObject(beanClass, NoOp.INSTANCE);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObject(Class<T> beanClass, Callback callback) {

		return (T) Enhancer.create(beanClass, callback);
	}

	public static boolean isProxyClass(Class<?> clazz) {

		return Enhancer.isEnhanced(clazz);
	}

	public static boolean isProxyObject(Object obj) {

		return isProxyClass(obj.getClass());
	}

	public static Set<String> getDaoBeanExcludeFieldSet(Object obj) {

		return daoBeanExcludeFieldSetMap.get(obj);
	}

	private static void setDaoBeanExcludeFieldSet(Object obj, Set<String> set) {

		daoBeanExcludeFieldSetMap.put(obj, set);
	}

	private static void removeIncludeField(Object obj, String methodName) {

		Set<String> set = getDaoBeanExcludeFieldSet(obj);
		set.remove(ConvertFuncUtils.parseFirstCharToLowerCase(methodName.substring(3)));
	}

}
