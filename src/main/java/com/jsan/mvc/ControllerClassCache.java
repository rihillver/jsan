package com.jsan.mvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器类缓存。
 *
 */

public class ControllerClassCache {

	private static final Map<String, Class<?>> controllerClassMap = new HashMap<String, Class<?>>(); // 全局性的，多个Filter共用此Map

	/**
	 * 初始化控制器的类缓存，即将扫描指定前缀名的包下面的所有类，并将其缓存到 HashMap 中，这里为了便于模糊匹配，将其全限定类名均转换为小写作为 Map 的 Key。
	 * 
	 * @param packagePrefix
	 */
	public static void init(String packagePrefix) {

		Set<Class<?>> set = MvcFuncUtils.getClasses(packagePrefix);

		for (Class<?> clazz : set) {
			controllerClassMap.put(clazz.getName().toLowerCase(), clazz);
		}
	}

	/**
	 * 这里的全限定类名必须转换为小写，因为上面 init() 方法初始化的时候已经将所有的扫描到的类文件名转换为小写形式。
	 * 
	 * @param qualifiedClassName
	 * @return
	 */
	public static Class<?> get(String qualifiedClassName) {

		return controllerClassMap.get(qualifiedClassName.toLowerCase()); // 转换小写，因为Key已转换为小写
	}

}
