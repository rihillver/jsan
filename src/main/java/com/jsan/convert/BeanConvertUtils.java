package com.jsan.convert;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.jsan.convert.cache.BeanConvertServiceCache;
import com.jsan.convert.cache.BeanConvertServiceContainer;
import com.jsan.convert.cache.BeanInformationCache;

/**
 * Bean 与 Map 互转换的工具类。
 *
 */

public class BeanConvertUtils {

	private static final ConvertService defaultConvertService = new GeneralConvertService();

	public static <T> T getObject(Class<T> beanClass, Map<?, ?> map) {

		return getObject(beanClass, map, false);
	}

	public static <T> T getObject(Class<T> beanClass, Map<?, ?> map, boolean keyToCamelCase) {

		return getObject(beanClass, map, defaultConvertService, keyToCamelCase);
	}

	public static <T> T getObject(Class<T> beanClass, Map<?, ?> map, ConvertService service) {

		return getObject(beanClass, map, service, false);
	}

	public static <T> T getObject(Class<T> beanClass, Map<?, ?> map, ConvertService service, boolean keyToCamelCase) {

		T bean = createBeanInstance(beanClass);

		Map<String, Method> writeMethodMap = BeanInformationCache.getWriteMethodMap(beanClass);
		BeanConvertServiceContainer container = BeanConvertServiceCache.getConvertServiceContainer(Mold.COMMON,
				beanClass, service);

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

	public static <T> Map<String, Object> getMap(T bean) {

		return getMap(bean, false);
	}

	public static <T> Map<String, Object> getMap(T bean, boolean keyToSnakeCase) {

		return convertBeanToMap(bean, false, keyToSnakeCase);
	}

	public static <T> Map<String, Object> getMapBaseOnReadMethod(T bean) {

		return getMapBaseOnReadMethod(bean, false);
	}

	public static <T> Map<String, Object> getMapBaseOnReadMethod(T bean, boolean keyToSnakeCase) {

		return convertBeanToMap(bean, true, keyToSnakeCase);
	}

	private static <T> Map<String, Object> convertBeanToMap(T bean, boolean baseOnReadMethod, boolean keyToSnakeCase) {

		Class<?> beanClass = bean.getClass();
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		Set<String> fieldSet = baseOnReadMethod ? null : BeanInformationCache.getFieldSet(beanClass);
		Map<String, Method> readMethodMap = BeanInformationCache.getReadMethodMap(beanClass);

		try {
			for (Map.Entry<String, Method> entry : readMethodMap.entrySet()) {
				String key = entry.getKey();
				if (!baseOnReadMethod && !fieldSet.contains(key)) {
					continue;
				}
				if (keyToSnakeCase) {
					key = ConvertFuncUtils.parseCamelCaseToSnakeCase(key); // 转换为下划线命名规范
				}
				map.put(key, entry.getValue().invoke(bean));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return map;
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
	public static <T> void convertBeanElement(Mold mold, T bean, Class<T> beanClass, ConvertService service, String key,
			Object value) throws Exception {

		Method method = BeanInformationCache.getWriteMethod(beanClass, key);
		if (method != null) {
			BeanConvertServiceContainer container = BeanConvertServiceCache.getConvertServiceContainer(mold, beanClass,
					service);
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
	public static <T> void convertBeanElement(T bean, Class<T> beanClass, ConvertService service,
			BeanConvertServiceContainer container, Method method, Object value) throws Exception {

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
