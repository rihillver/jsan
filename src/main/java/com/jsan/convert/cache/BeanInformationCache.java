package com.jsan.convert.cache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.jsan.convert.ConvertFuncUtils;

/**
 * BeanInformation 缓存。
 * <p>
 * readMethod 和 writeMethod 支持有继承关系的 Bean，包含父类的 Getter 和 Setter 方法，<br>
 * fieldSet 则不含父类的字段。
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

		final Map<String, Method> map = new HashMap<String, Method>();

		handleReadMethodMap(beanClass, new MethodHandler() {

			@Override
			public void handle(String key, Method method) {
				map.put(key, method);
			}
		});

		return map;
	}

	public static void handleReadMethodMap(Class<?> beanClass, MethodHandler methodHandler) {

		Method[] methods = beanClass.getMethods(); // 只获取公共的方法，包括父类的公共方法
		for (Method method : methods) {
			method.setAccessible(true);

			if (Modifier.isStatic(method.getModifiers())) { // 不能是静态方法
				continue;
			}

			if (method.getParameterTypes().length != 0) { // 不能有形参
				continue;
			}

			if (method.getReturnType() == Void.TYPE) { // 返回类型不能是void
				continue;
			}

			String methodName = method.getName();

			if (methodName.equals("getClass")) { // 将getCalss()方法排除
				continue;
			}

			String key = null;
			if (methodName.startsWith("get")) {
				if (methodName.length() > 3) {
					char c = methodName.charAt(3);
					if (Character.isUpperCase(c) || (c == '_') || (c == '$')) { // 第四个字母必须为大写，或下划线，或美元符
						key = methodName.substring(3); // 将方法名的前面get去掉
					}
				}
			} else if (methodName.startsWith("is")) {
				if (methodName.length() > 2 && method.getReturnType() == Boolean.TYPE) {
					char c = methodName.charAt(2);
					if (Character.isUpperCase(c) || (c == '_') || (c == '$')) { // 第三个字母必须为大写，或下划线，或美元符
						key = methodName.substring(2); // 将方法名的前面is去掉
					}
				}
			}

			if (key != null) {
				key = ConvertFuncUtils.parseFirstCharToLowerCase(key); // 将第一个字母转为小写

				methodHandler.handle(key, method);
			}
		}
	}

	private static Map<String, Method> createWriteMethodMap(Class<?> beanClass) {

		final Map<String, Method> map = new HashMap<String, Method>();

		handleWriteMethodMap(beanClass, new MethodHandler() {

			@Override
			public void handle(String key, Method method) {
				map.put(key, method);
			}
		});

		return map;
	}

	public static void handleWriteMethodMap(Class<?> beanClass, MethodHandler methodHandler) {

		Method[] methods = beanClass.getMethods(); // 只获取公共的方法，包括父类的公共方法
		for (Method method : methods) {
			method.setAccessible(true);

			if (Modifier.isStatic(method.getModifiers())) { // 不能是静态方法
				continue;
			}

			if (method.getParameterTypes().length != 1) { // 只能是一个形参
				continue;
			}

			if (method.getReturnType() != Void.TYPE) { // 返回类型只能是void
				continue;
			}

			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				if (methodName.length() > 3) {
					char c = methodName.charAt(3);
					if (Character.isUpperCase(c) || (c == '_') || (c == '$')) { // 第四个字母必须为大写，或下划线，或美元符
						String key = methodName.substring(3); // 将方法名的前面set去掉
						key = ConvertFuncUtils.parseFirstCharToLowerCase(key); // 将第一个字符转为小写

						methodHandler.handle(key, method);
					}
				}
			}
		}
	}

	public static interface MethodHandler {

		void handle(String key, Method method);
	}

}
