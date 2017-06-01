package com.jsan.convert;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import com.jsan.convert.cache.BeanConvertServiceCache;
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

		for (Map.Entry<?, ?> entry : map.entrySet()) {
			convertBeanElement(Mold.COMMON, bean, beanClass, service, entry.getKey().toString(), entry.getValue());
		}

		return bean;
	}

	public static <T> void convertBeanElement(Mold mold, T bean, Class<T> beanClass, ConvertService service, String key,
			Object value) {

		Method method = BeanInformationCache.getWriteMethod(beanClass, key);
		if (method != null) {
			Class<?> type = method.getParameterTypes()[0];
			Type genericType = method.getGenericParameterTypes()[0];

			// 返回的 ConvertService 是不会为 null 的
			ConvertService tempService = BeanConvertServiceCache.getConvertService(mold, beanClass, method, service);

			Converter converter = tempService.lookupConverter(type);
			Object object = converter.convert(value, genericType);

			try {
				method.invoke(bean, object);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static <T> T createBeanInstance(Class<T> beanClass) {

		try {
			return BeanProxyUtils.newInstance(beanClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
