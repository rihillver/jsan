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

		return getObject(beanClass, map, defaultConvertService);
	}

	public static <T> T getObject(Class<T> beanClass, Map<?, ?> map, ConvertService service) {

		T bean = createBeanInstance(beanClass);

		Map<String, Method> writeMethodMap = BeanInformationCache.getWriteMethodMap(beanClass);
		BeanConvertServiceContainer container = BeanConvertServiceCache.getConvertServiceContainer(Mold.COMMON,
				beanClass, service);

		try {
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				Method method = writeMethodMap.get(entry.getKey().toString());
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

		return convertBeanToMap(bean, false);
	}

	public static <T> Map<String, Object> getMapBaseOnReadMethod(T bean) {

		return convertBeanToMap(bean, true);
	}

	private static <T> Map<String, Object> convertBeanToMap(T bean, boolean baseOnReadMethod) {

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
