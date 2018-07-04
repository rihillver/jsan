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
 * readMethod 和 writeMethod 支持有继承关系的 Bean，包含父类的 Getter 和 Setter 方法，fieldSet
 * 则不含父类的字段，同时fieldSet不含用static和transient修饰的字段。
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

		final Set<String> fieldSet = new LinkedHashSet<String>();

		for (Field field : beanClass.getDeclaredFields()) { // 仅当前类内部的所有字段，不包括继承的字段
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isTransient(mod)) { // 对于用static和transient修饰的字段忽略
				continue;
			}
			fieldSet.add(field.getName());
		}

		final Map<String, Method> readMethodMap = new HashMap<String, Method>();
		final Map<String, Method> readMethodMapBaseOnField = new HashMap<String, Method>();

		handleReadMethodMap(beanClass, new MethodHandler() {

			@Override
			public void handle(String key, Method method) {
				readMethodMap.put(key, method);
				if (fieldSet.contains(key)) {
					readMethodMapBaseOnField.put(key, method);
				}
			}
		});

		final Map<String, Method> writeMethodMap = new HashMap<String, Method>();
		final Map<String, Method> writeMethodMapBaseOnField = new HashMap<String, Method>();

		handleWriteMethodMap(beanClass, new MethodHandler() {

			@Override
			public void handle(String key, Method method) {
				writeMethodMap.put(key, method);
				if (fieldSet.contains(key)) {
					writeMethodMapBaseOnField.put(key, method);
				}
			}
		});

		container.setFieldSet(fieldSet);
		container.setReadMethodMap(readMethodMap);
		container.setReadMethodMapBaseOnField(readMethodMapBaseOnField);
		container.setWriteMethodMap(writeMethodMap);
		container.setWriteMethodMapBaseOnField(writeMethodMapBaseOnField);

		return container;
	}

	/**
	 * 返回 Bean 的所有字段名，仅含自身的所有字段，不含父类的任何字段。
	 * 
	 * @param beanClass
	 * @return
	 */
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

	/**
	 * 返回 Bean 的所有 Getter 方法，key 为字段名，含父类的公共方法，不含自身的私有方法。
	 * 
	 * @param beanClass
	 * @return
	 */
	public static Map<String, Method> getReadMethodMap(Class<?> beanClass) {

		return getBeanInformationContainer(beanClass).getReadMethodMap();
	}

	/**
	 * 返回 Bean 的所有与自身字段相对应的 Getter 方法，key 为字段名，不含父类的公共方法，不含自身的私有方法。
	 * 
	 * @param beanClass
	 * @return
	 */
	public static Map<String, Method> getReadMethodMapBaseOnField(Class<?> beanClass) {

		return getBeanInformationContainer(beanClass).getReadMethodMapBaseOnField();
	}

	/**
	 * 返回 Bean 的所有 Setter 方法，key 为字段名，含父类的公共方法，不含自身的私有方法。
	 * 
	 * @param beanClass
	 * @return
	 */
	public static Map<String, Method> getWriteMethodMap(Class<?> beanClass) {

		return getBeanInformationContainer(beanClass).getWriteMethodMap();
	}

	/**
	 * 返回 Bean 的所有与自身字段相对应的 Setter 方法，key 为字段名，不含父类的公共方法，不含自身的私有方法。
	 * 
	 * @param beanClass
	 * @return
	 */
	public static Map<String, Method> getWriteMethodMapBaseOnField(Class<?> beanClass) {

		return getBeanInformationContainer(beanClass).getWriteMethodMapBaseOnField();
	}

	/**
	 * 该方法针对所有公共的 Getter 方法，包括父类的公共的 Getter 方法。
	 * 
	 * @param beanClass
	 * @param methodHandler
	 */
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

				methodHandler.handle(key, method); // MethodHandler
			}
		}
	}

	/**
	 * 该方法针对所有公共的 Setter 方法，包括父类的公共的 Setter 方法。
	 * 
	 * @param beanClass
	 * @param methodHandler
	 */
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

						methodHandler.handle(key, method); // MethodHandler
					}
				}
			}
		}
	}

	public static interface MethodHandler {

		void handle(String key, Method method);
	}

}
