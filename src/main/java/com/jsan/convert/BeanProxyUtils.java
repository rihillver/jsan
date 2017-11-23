package com.jsan.convert;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.jsan.convert.cache.BeanInformationCache;
import com.jsan.convert.cache.BeanInformationCache.MethodHandler;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

/**
 * 基于 Cglib 的动态代理工具类。
 * <p>
 * 特别注意：<br>
 * 需要通过 Cglib 构建代理对象的原生类如果含有 callback 和 callbacks 字段对应的 Getter 和 Setter
 * 方法，则在构建时可能发生异常，原因是通过 Cglib 构建出来的代理类内部含有专有方法
 * setCallback(int,net.sf.cglib.proxy.Callback)、getCallback(int)、setCallbacks(net.sf.cglib.proxy.Callback[])、getCallbacks()
 * 而导致方法冲突。另外还有三个重载的专有方法也需要留意潜在的冲突
 * newInstance(net.sf.cglib.proxy.Callback[])、newInstance(java.lang.Class[],java.lang.Object[],net.sf.cglib.proxy.Callback[])、newInstance(net.sf.cglib.proxy.Callback)。
 *
 */

public class BeanProxyUtils {

	public static class DaoBeanExcludeFieldContainer {

		Class<?> originalClass;
		Set<String> fieldSet;

		public DaoBeanExcludeFieldContainer(Class<?> originalClass, Set<String> fieldSet) {
			this.originalClass = originalClass;
			this.fieldSet = fieldSet;
		}

		public Class<?> getOriginalClass() {
			return originalClass;
		}

		public void setOriginalClass(Class<?> originalClass) {
			this.originalClass = originalClass;
		}

		public Set<String> getFieldSet() {
			return fieldSet;
		}

		public void setFieldSet(Set<String> fieldSet) {
			this.fieldSet = fieldSet;
		}

	}

	private static final Map<Integer, DaoBeanExcludeFieldContainer> daoBeanExcludeFieldContainerMap = new WeakHashMap<Integer, DaoBeanExcludeFieldContainer>();

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

	/**
	 * 通过传入类的方式构建一个新的代理对象（新的代理对象将会记录 Setter 的状态，主要用于 Dao 操作的 POJO）。
	 * 
	 * @param beanClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDaoBean(Class<T> beanClass) {

		T bean = getObject(beanClass, daoBeanWriteMethodInterceptor);

		Set<String> fieldSet = (Set<String>) ((LinkedHashSet<String>) BeanInformationCache.getFieldSet(beanClass))
				.clone();
		DaoBeanExcludeFieldContainer container = new DaoBeanExcludeFieldContainer(beanClass, fieldSet);
		setDaoBeanExcludeFieldContainer(bean, container);

		return bean;
	}

	/**
	 * 通过传入对象的方式构建一个新的代理对象，并且新的代理对象复制了原对象的状态（新的代理对象将会记录 Setter 的状态，主要用于 Dao 操作的
	 * POJO）。
	 * 
	 * @param beanObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDaoBean(T beanObject) {

		Class<T> beanClass = (Class<T>) beanObject.getClass();
		T bean = getObject(beanClass, daoBeanWriteMethodInterceptor);

		copyBean(bean, beanClass, beanObject); // 复制bean状态

		Set<String> fieldSet = (Set<String>) ((LinkedHashSet<String>) BeanInformationCache.getFieldSet(beanClass))
				.clone();
		DaoBeanExcludeFieldContainer container = new DaoBeanExcludeFieldContainer(beanClass, fieldSet);
		setDaoBeanExcludeFieldContainer(bean, container);

		return bean;
	}

	/**
	 * 将原始对象的状态值复制到代理对象中去（通过原始对象的 Getter 获取到的值，再通过代理对象的 Setter 设置进去）。
	 * 
	 * @param bean
	 * @param beanClass
	 * @param beanObject
	 * @throws Exception
	 */
	private static <T> void copyBean(final T bean, Class<T> beanClass, final T beanObject) {

		final Map<String, Method> beanObjectReadMethodMap = BeanInformationCache.getReadMethodMap(beanClass);

		BeanInformationCache.handleWriteMethodMap(bean.getClass(), new MethodHandler() {

			@Override
			public void handle(String key, Method method) {
				Method beanObjectReadMethod = beanObjectReadMethodMap.get(key);
				if (beanObjectReadMethod != null) {
					try {
						Object beanObjectReadMethodReturnValue = beanObjectReadMethod.invoke(beanObject);
						method.invoke(bean, beanObjectReadMethodReturnValue);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
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

	/**
	 * 返回 daoBean 排除字段的 Set 结果集，请勿直接对这里返回的 Set 结果集进行增删操作。
	 * 
	 * @param obj
	 * @return
	 */
	public static Set<String> getDaoBeanExcludeFieldSet(Object obj) {

		DaoBeanExcludeFieldContainer container = getDaoBeanExcludeFieldContainer(obj);
		if (container != null) {
			return container.getFieldSet();
		}
		return null;
	}

	/**
	 * 返回 daoBean 排除字段的 Set 结果集的克隆副本。
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> getDaoBeanExcludeFieldSetClone(Object obj) {

		Set<String> fieldSet = getDaoBeanExcludeFieldSet(obj);
		if (fieldSet != null) {
			return (Set<String>) ((LinkedHashSet<String>) fieldSet).clone();
		}
		return null;
	}

	/**
	 * 返回代理对象的原始类型（原型类型/父类型）。
	 * 
	 * @param obj
	 * @return
	 */
	public static Class<?> getDaoBeanOriginalClass(Object obj) {

		DaoBeanExcludeFieldContainer container = getDaoBeanExcludeFieldContainer(obj);
		if (container != null) {
			return container.getOriginalClass();
		}
		return null;
	}

	public static DaoBeanExcludeFieldContainer getDaoBeanExcludeFieldContainer(Object obj) {

		int key = System.identityHashCode(obj);
		return daoBeanExcludeFieldContainerMap.get(key);
	}

	private static void setDaoBeanExcludeFieldContainer(Object obj, DaoBeanExcludeFieldContainer container) {

		int key = System.identityHashCode(obj);
		daoBeanExcludeFieldContainerMap.put(key, container);
	}

	private static void removeIncludeField(Object obj, String methodName) {

		Set<String> fieldSet = getDaoBeanExcludeFieldSet(obj);
		if (fieldSet != null) {
			String key = ConvertFuncUtils.parseFirstCharToLowerCase(methodName.substring(3));
			fieldSet.remove(key);
		}
	}

}
