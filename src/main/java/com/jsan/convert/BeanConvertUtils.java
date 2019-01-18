package com.jsan.convert;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
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

		return getBean(beanClass, map, true);
	}

	public static <T> T getBean(Class<T> beanClass, Map<?, ?> map, boolean keyToCamelCase) {

		return getBean(beanClass, map, defaultConvertService, keyToCamelCase);
	}

	public static <T> T getBean(Class<T> beanClass, Map<?, ?> map, ConvertService service) {

		return getBean(beanClass, map, service, true);
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

	/**
	 * 基于 bean 的字段名，仅含自身的所有字段，不含父类的任何字段（将驼峰形式的字段名转换为下划线形式）。
	 * 
	 * @param bean
	 * @param keyToSnakeCase
	 * @return
	 */
	public static <T> Map<String, Object> getMapBaseOnField(T bean, boolean keyToSnakeCase) {

		return convertBeanToMap(bean, true, keyToSnakeCase);
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

	/**
	 * 基于 bean 的 Getter 方法，通过 Getter 方法取字段名（对应的字段不一定真实存在），含父类的公共方法，不含自身的私有方法（将驼峰形式的字段名转换为下划线形式）。
	 * 
	 * @param bean
	 * @param keyToSnakeCase
	 * @return
	 */
	public static <T> Map<String, Object> getMap(T bean, boolean keyToSnakeCase) {

		return convertBeanToMap(bean, false, keyToSnakeCase);
	}

	/**
	 * 将 Bean 转换成 Map。
	 * 
	 * @param bean
	 * @param baseOnField
	 *            true：基于自身字段相对于的Getter方法，false：基于所有Getter方法
	 * @param keyToSnakeCase
	 *            true：将key转换为下划线形式，false：默认key不做任何转换
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> convertBeanToMap(T bean, boolean baseOnField, boolean keyToSnakeCase) {

		Class<T> beanClass = (Class<T>) bean.getClass();
		return convertBeanToMap(beanClass, bean, baseOnField, keyToSnakeCase);
	}

	/**
	 * 将 Bean 转换成 Map。
	 * 
	 * @param beanClass
	 * @param bean
	 * @param baseOnField
	 *            true：基于自身字段相对于的Getter方法，false：基于所有Getter方法
	 * @param keyToSnakeCase
	 *            true：将key转换为下划线形式，false：默认key不做任何转换
	 * @return
	 */
	public static <T> Map<String, Object> convertBeanToMap(Class<T> beanClass, T bean, boolean baseOnField, boolean keyToSnakeCase) {

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
				map.put(key, returnValue);
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
