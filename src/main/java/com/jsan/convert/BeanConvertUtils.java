package com.jsan.convert;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import com.jsan.convert.cache.BeanConvertServiceCache;
import com.jsan.convert.cache.BeanConvertServiceContainer;
import com.jsan.convert.cache.BeanInformationCache;

/**
 * 将 Map 转换成 Bean 的工具类。
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

	public static <T> void convertBeanElement(Mold mold, T bean, Class<T> beanClass, ConvertService service, String key,
			Object value) throws Exception {

		Method method = BeanInformationCache.getWriteMethod(beanClass, key);
		if (method != null) {
			BeanConvertServiceContainer container = BeanConvertServiceCache.getConvertServiceContainer(mold, beanClass,
					service);
			convertBeanElement(bean, beanClass, service, container, method, value);
		}
	}

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
