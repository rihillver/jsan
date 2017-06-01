package com.jsan.convert.cache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.jsan.convert.ConvertFuncUtils;

/**
 * BeanInformation 缓存。
 * <p>
 * 支持有继承关系的 Bean
 *
 */

public class BeanInformationCache {

	private static final Map<Class<?>, BeanInformationContainer> beanInformationContainerMap = new HashMap<Class<?>, BeanInformationContainer>();

	public static BeanInformationContainer getBeanInformationContainer(Class<?> beanClass) {

		BeanInformationContainer container = beanInformationContainerMap.get(beanClass);

		if (container == null) {
			synchronized (beanInformationContainerMap) {
				container = beanInformationContainerMap.get(beanClass);
				if (container == null) {
					container = createBeanInformationContainer(beanClass);
					beanInformationContainerMap.put(beanClass, container);
				}
			}
		}

		return container;
	}

	private static BeanInformationContainer createBeanInformationContainer(Class<?> beanClass) {

		BeanInformationContainer container = new BeanInformationContainer();

		container.setFieldSet(createFieldSet(beanClass));
		container.setReadMethodMap(createReadMethodMap(beanClass));
		container.setWriteMethodMap(createWriteMethodMap(beanClass));

		return container;
	}

	public static Set<String> getFieldSet(Class<?> beanClass) {

		return getBeanInformationContainer(beanClass).getFieldSet();
	}

	/**
	 * 这里的 key 通常是去掉 "get"/"is" 外首字母小写的方法名（例如：getNewName() -> newName、isSex() ->
	 * sex）。
	 * 
	 * @param beanClass
	 * @param key
	 * @return
	 */
	public static Method getReadMethod(Class<?> beanClass, String key) {

		return getReadMethodMap(beanClass).get(key);
	}

	/**
	 * 这里的 key 通常是去掉 "set" 外首字母小写的方法名（例如：setNewName() -> newName）。
	 * 
	 * @param beanClass
	 * @param key
	 * @return
	 */
	public static Method getWriteMethod(Class<?> beanClass, String key) {

		return getWriteMethodMap(beanClass).get(key);
	}

	public static Map<String, Method> getReadMethodMap(Class<?> beanClass) {

		return getBeanInformationContainer(beanClass).getReadMethodMap();
	}

	public static Map<String, Method> getWriteMethodMap(Class<?> beanClass) {

		return getBeanInformationContainer(beanClass).getWriteMethodMap();
	}

	private static Set<String> createFieldSet(Class<?> beanClass) {

		Set<String> set = new LinkedHashSet<String>();

		for (Field field : beanClass.getDeclaredFields()) { // 仅当前类内部的所有字段，不包括继承的字段
			set.add(field.getName());
		}

		return set;
	}

	private static Map<String, Method> createReadMethodMap(Class<?> beanClass) {

		Map<String, Method> map = new HashMap<String, Method>();

		Method[] methods = beanClass.getMethods(); // 只获取公共的方法，包括父类的公共方法
		for (Method method : methods) {
			method.setAccessible(true);
			if (method.getParameterTypes().length == 0) { // 不能有形参
				String methodName = method.getName();

				if (methodName.equals("getClass")) { // 将 getCalss() 方法排除
					continue;
				}

				String key = null;
				if (methodName.startsWith("get")) {
					if (methodName.length() > 3 && Character.isUpperCase(methodName.charAt(3))) { // 第四个字母必须为大写
						key = methodName.substring(3);
					}
				} else if (methodName.startsWith("is")) {
					if (methodName.length() > 2 && Character.isUpperCase(methodName.charAt(2))) { // 第三个字母必须为大写
						key = methodName.substring(2);
					}
				}

				if (key != null) {
					// 将方法名的前面 get 或 is 去掉，然后将第一个字母转为小写
					map.put(ConvertFuncUtils.parseFirstCharToLowerCase(key), method);
				}
			}
		}

		return map;
	}

	private static Map<String, Method> createWriteMethodMap(Class<?> beanClass) {

		Map<String, Method> map = new HashMap<String, Method>();

		Method[] methods = beanClass.getMethods(); // 只获取公共的方法，包括父类的公共方法
		for (Method method : methods) {
			method.setAccessible(true);
			String methodName = method.getName();
			// 以 set 开头并且形参只能是一个
			if (method.getParameterTypes().length == 1 && methodName.startsWith("set")) {
				// 第四个字母必须为大写
				if (methodName.length() > 3 && Character.isUpperCase(methodName.charAt(3))) {
					String key = methodName.substring(3);
					// 将方法名的前面 set 去掉，然后将第一个字母转为小写
					map.put(ConvertFuncUtils.parseFirstCharToLowerCase(key), method);
				}
			}
		}

		return map;
	}

}
