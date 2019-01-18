package com.jsan.convert;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jsan.convert.cache.BeanConvertServiceCache;
import com.jsan.convert.cache.BeanConvertServiceContainer;
import com.jsan.convert.cache.BeanInformationCache;

/**
 * Bean 相关转换的工具类。
 *
 */

public class BeanConvertUtils {

	private static final ConvertService defaultConvertService = new GeneralConvertService();

	/**
	 * 设置 Bean 对象中的字段值（该字段必须有对应的 setter 方法）。
	 * 
	 * @param bean
	 * @param key
	 * @param value
	 */
	public static <T> void setProperty(T bean, String key, Object value) {

		setProperty(bean, key, value, defaultConvertService);
	}

	/**
	 * 设置 Bean 对象中的字段值（该字段必须有对应的 setter 方法）。
	 * 
	 * @param bean
	 * @param key
	 * @param value
	 * @param service
	 */
	@SuppressWarnings("unchecked")
	public static <T> void setProperty(T bean, String key, Object value, ConvertService service) {

		Class<T> beanClass = (Class<T>) bean.getClass();
		Method method = BeanInformationCache.getWriteMethod(beanClass, key);
		BeanConvertServiceContainer container = BeanConvertServiceCache.getConvertServiceContainer(Mold.COMMON, beanClass, service);

		try {
			convertBeanElement(bean, beanClass, service, container, method, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取 Bean 对象中的字段值（该字段必须有对应的 getter 方法）。
	 * 
	 * @param <T>
	 * @param bean
	 * @param key
	 * @return
	 */
	public static <T> Object getProperty(T bean, String key) {

		Method method = BeanInformationCache.getReadMethod(bean.getClass(), key);

		try {
			return method.invoke(bean);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取 Bean 对象中的字段值（该字段必须有对应的 getter 方法）。
	 * 
	 * @param <T>
	 * @param bean
	 * @param key
	 * @param fieldClass
	 * @return
	 */
	public static <C, T> C getProperty(T bean, String key, Class<C> fieldClass) {
		
		Object obj = getProperty(bean, key);
		return fieldClass.cast(obj);
	}

	public static <T> T getBean(Class<T> beanClass, Map<?, ?> map) {

		return getBean(beanClass, map, false);
	}

	public static <T> T getBean(Class<T> beanClass, Map<?, ?> map, boolean keyToCamelCase) {

		return getBean(beanClass, map, defaultConvertService, keyToCamelCase);
	}

	public static <T> T getBean(Class<T> beanClass, Map<?, ?> map, ConvertService service) {

		return getBean(beanClass, map, service, false);
	}

	public static <T> T getBean(Class<T> beanClass, Map<?, ?> map, ConvertService service, boolean keyToCamelCase) {

		T bean = createBeanInstance(beanClass);

		Map<String, Method> writeMethodMap = BeanInformationCache.getWriteMethodMap(beanClass);
		BeanConvertServiceContainer container = BeanConvertServiceCache.getConvertServiceContainer(Mold.COMMON, beanClass, service);

		try {
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				String key = entry.getKey().toString();
				if (keyToCamelCase) {
					key = ConvertFuncUtils.parseSnakeCaseToCamelCase(key); // 转换为驼峰命名规范
				}
				Method method = writeMethodMap.get(key);
				if (method != null) {
					convertBeanElement(bean, beanClass, service, container, method, entry.getValue());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return bean;
	}

	/**
	 * 基于 bean 的字段名，仅含自身的所有字段，不含父类的任何字段。
	 * 
	 * @param bean
	 * @return
	 */
	public static <T> Map<String, Object> getMapBaseOnField(T bean) {

		return getMapBaseOnField(bean, false);
	}

	public static <T> Map<String, Object> getMapBaseOnField(T bean, boolean deepConvert) {
		
		return getMapBaseOnField(bean, deepConvert, false);
	}

	/**
	 * 基于 bean 的字段名，仅含自身的所有字段，不含父类的任何字段（将驼峰形式的字段名转换为下划线形式）。
	 * 
	 * @param bean
	 * @param keyToSnakeCase
	 * @return
	 */
	public static <T> Map<String, Object> getMapBaseOnField(T bean, boolean deepConvert, boolean keyToSnakeCase) {

		return convertBeanToMap(bean, true, deepConvert, keyToSnakeCase);
	}

	/**
	 * 基于 bean 的 Getter 方法，通过 Getter 方法取字段名（对应的字段不一定真实存在），含父类的公共方法，不含自身的私有方法。
	 * 
	 * @param bean
	 * @return
	 */
	public static <T> Map<String, Object> getMap(T bean) {

		return getMap(bean, false);
	}

	public static <T> Map<String, Object> getMap(T bean, boolean deepConvert) {
		
		return getMap(bean, deepConvert, false);
	}

	/**
	 * 基于 bean 的 Getter 方法，通过 Getter 方法取字段名（对应的字段不一定真实存在），含父类的公共方法，不含自身的私有方法（将驼峰形式的字段名转换为下划线形式）。
	 * 
	 * @param bean
	 * @param keyToSnakeCase
	 * @return
	 */
	public static <T> Map<String, Object> getMap(T bean, boolean deepConvert, boolean keyToSnakeCase) {

		return convertBeanToMap(bean, false, deepConvert, keyToSnakeCase);
	}

	/**
	 * 将 Bean 转换成 Map。
	 * 
	 * @param bean
	 * @param baseOnField
	 *            true：基于自身字段相对于的Getter方法，false：基于所有Getter方法
	 * @param deepConvert
	 *            true：深度逐层转换，false：仅第一层转换
	 * @param keyToSnakeCase
	 *            true：将key转换为下划线形式，false：默认key不做任何转换
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> convertBeanToMap(T bean, boolean baseOnField, boolean deepConvert, boolean keyToSnakeCase) {

		Class<T> beanClass = (Class<T>) bean.getClass();
		return convertBeanToMap(beanClass, bean, baseOnField, deepConvert, keyToSnakeCase);
	}

	/**
	 * 将 Bean 转换成 Map。
	 * <p>
	 * （对Array、Collection、Map、T）
	 * 
	 * @param beanClass
	 * @param bean
	 * @param baseOnField
	 *            true：基于自身字段相对于的Getter方法，false：基于所有Getter方法
	 * @param deepConvert
	 *            true：深度逐层转换，false：仅第一层转换
	 * @param keyToSnakeCase
	 *            true：将key转换为下划线形式，false：默认key不做任何转换
	 * @return
	 */
	public static <T> Map<String, Object> convertBeanToMap(Class<T> beanClass, T bean, boolean baseOnField, boolean deepConvert, boolean keyToSnakeCase) {

		Map<String, Object> map = new LinkedHashMap<String, Object>();

		Map<String, Method> readMethodMap = baseOnField ? BeanInformationCache.getReadMethodMapBaseOnField(beanClass) : BeanInformationCache.getReadMethodMap(beanClass);

		try {
			for (Map.Entry<String, Method> entry : readMethodMap.entrySet()) {
				String key = entry.getKey();
				if (keyToSnakeCase) {
					key = ConvertFuncUtils.parseCamelCaseToSnakeCase(key); // 转换为下划线命名规范
				}
				Method method = entry.getValue();
				Object returnValue = method.invoke(bean);
				
				// 深度转换的情况需要继续处理
				if (deepConvert) {
					Class<?> returnValueClass = returnValue.getClass();
					
					returnValue = recursiveConvert(returnValueClass, returnValue, baseOnField, deepConvert, keyToSnakeCase); // 逐层递归转换
					
				}
				
				map.put(key, returnValue);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return map;
	}

	/**
	 * 逐层递归转换。
	 * 
	 * @param <T>
	 * 
	 */
	@SuppressWarnings("unchecked")
	private static <T> Object recursiveConvert(Class<?> valueClass, Object value, boolean baseOnField, boolean deepConvert, boolean keyToSnakeCase) {

		if (valueClass.isArray()) { // 数组时
			value = arrayDeepConvert(value, baseOnField, deepConvert, keyToSnakeCase);
		} else if (Collection.class.isAssignableFrom(valueClass)) { // Collection时
			value = collectionDeepConvert(value, baseOnField, deepConvert, keyToSnakeCase);
		} else if (Map.class.isAssignableFrom(valueClass)) { // Map时
			value = mapDeepConvert(value, baseOnField, deepConvert, keyToSnakeCase);
		} else { // bean时
			String valueClassName = valueClass.getName();
			if (!(valueClassName.startsWith("java.") || valueClassName.startsWith("javax."))) { // 非java核心类库的情况均视作为普通的bean
				value = convertBeanToMap((Class<T>) valueClass, (T) value, baseOnField, deepConvert, keyToSnakeCase);
			}
		}

		return value;
	}

	private static <T> Object arrayDeepConvert(Object array, boolean baseOnField, boolean deepConvert, boolean keyToSnakeCase) {

		Class<?> componentClass = Object.class;

		int length = Array.getLength(array);
		Object object = Array.newInstance(componentClass, length);

		if (length > 0) {
			for (int i = 0; i < length; i++) {

				Object value = Array.get(array, i);
				Class<?> valueClass = value.getClass();

				value = recursiveConvert(valueClass, value, baseOnField, deepConvert, keyToSnakeCase); // 逐层递归转换

				Array.set(object, i, value);
			}
		}

		return object;
	}

	@SuppressWarnings("unchecked")
	private static <T> Collection<Object> collectionDeepConvert(Object collection, boolean baseOnField, boolean deepConvert, boolean keyToSnakeCase) {

		Collection<Object> object = null;

		try {
			object = (Collection<Object>) collection.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Collection<?> sourceCollection = (Collection<?>) collection;
		for (Object obj : sourceCollection) {

			Object value = obj;
			Class<?> valueClass = value.getClass();

			value = recursiveConvert(valueClass, value, baseOnField, deepConvert, keyToSnakeCase); // 逐层递归转换

			object.add(value);
		}

		return object;
	}

	@SuppressWarnings("unchecked")
	private static <T> Map<Object, Object> mapDeepConvert(Object map, boolean baseOnField, boolean deepConvert, boolean keyToSnakeCase) {

		Map<Object, Object> object = null;

		try {
			object = (Map<Object, Object>) map.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Map<?, ?> sourceMap = (Map<?, ?>) map;

		for (Map.Entry<?, ?> entry : sourceMap.entrySet()) {

			Object k = entry.getKey();
			Class<?> kClass = k.getClass();
			k = recursiveConvert(kClass, k, baseOnField, deepConvert, keyToSnakeCase); // 逐层递归转换

			Object v = entry.getValue();
			Class<?> vClass = v.getClass();
			v = recursiveConvert(vClass, v, baseOnField, deepConvert, keyToSnakeCase); // 逐层递归转换

			object.put(k, v);
		}

		return object;
	}

	/**
	 * 该方法虽简便，但大批量进行转换时对缓存的查找次数稍微多那么一点点。
	 * 
	 * @param mold
	 * @param bean
	 * @param beanClass
	 * @param service
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static <T> void convertBeanElement(Mold mold, T bean, Class<T> beanClass, ConvertService service, String key, Object value) throws Exception {

		Method method = BeanInformationCache.getWriteMethod(beanClass, key);
		if (method != null) {
			BeanConvertServiceContainer container = BeanConvertServiceCache.getConvertServiceContainer(mold, beanClass, service);
			convertBeanElement(bean, beanClass, service, container, method, value);
		}
	}

	/**
	 * 建议使用该方法进行转换，以减少缓存的查找次数。
	 * 
	 * @param bean
	 * @param beanClass
	 * @param service
	 * @param container
	 * @param method
	 * @param value
	 * @throws Exception
	 */
	public static <T> void convertBeanElement(T bean, Class<T> beanClass, ConvertService service, BeanConvertServiceContainer container, Method method, Object value) throws Exception {

		Class<?> type = method.getParameterTypes()[0];
		Type genericType = method.getGenericParameterTypes()[0];

		// 返回的 ConvertService 是不会为 null 的
		ConvertService tempService = BeanConvertServiceCache.getConvertService(beanClass, method, service, container);

		Converter converter = tempService.lookupConverter(type);
		Object object = converter.convert(value, genericType);

		method.invoke(bean, object);
	}

	private static <T> T createBeanInstance(Class<T> beanClass) {

		try {
			return BeanProxyUtils.newInstance(beanClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
