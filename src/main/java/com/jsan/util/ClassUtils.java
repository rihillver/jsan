package com.jsan.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class类工具（待整理）。
 * <p>
 * 注：该工具类主要针对特定包下的类查找和类匹配，常用的类工具方法可参见 Apache Commons 组件下的 org.apache.commons.lang3.ClassUtils。
 * <p>
 * 1、扫描指定包下的所有类 2、筛选类（是否为接口的实现类，是否为父类的子类，以什么开头，以什么结尾等） 3、定义一个过滤接口来实现灵活的查找筛选
 * 
 * 
 * 
 * 几种浅拷贝
 * 
 * 1、遍历循环复制
 * 
 * List<Person> destList=new ArrayList<Person>(srcList.size()); for(Person p : srcList){ destList.add(p); } 2、使用List实现类的构造方法
 * 
 * List<Person> destList=new ArrayList<Person>(srcList); 3、使用list.addAll()方法
 * 
 * List<Person> destList=new ArrayList<Person>(); destList.addAll(srcList); 4、使用System.arraycopy()方法
 * 
 * Person[] srcPersons=srcList.toArray(new Person[0]); Person[] destPersons=new Person[srcPersons.length]; System.arraycopy(srcPersons, 0, destPersons, 0, srcPersons.length);
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class ClassUtils {

	public static interface ClassFilter {

		boolean accept(Class<?> clss);
	}

	public static Set<Class<?>> getClassesStartsWith(String packageName, String prefix) {

		return getClassesStartsWith(packageName, prefix, true);
	}

	public static Set<Class<?>> getClassesStartsWith(String packageName, final String prefix, boolean recursive) {

		return findClasses(packageName, new ClassFilter() {

			@Override
			public boolean accept(Class<?> clss) {
				if (clss.getSimpleName().startsWith(prefix)) {
					return true;
				}
				return false;
			}
		}, recursive);
	}

	public static Set<Class<?>> getClassesEndsWith(String packageName, String suffix) {

		return getClassesEndsWith(packageName, suffix, true);
	}

	public static Set<Class<?>> getClassesEndsWith(String packageName, final String suffix, boolean recursive) {

		return findClasses(packageName, new ClassFilter() {

			@Override
			public boolean accept(Class<?> clss) {
				if (clss.getSimpleName().endsWith(suffix)) {
					return true;
				}
				return false;
			}
		}, recursive);
	}

	public static Set<Class<?>> getClassesContains(String packageName, String str) {

		return getClassesContains(packageName, str, true);
	}

	public static Set<Class<?>> getClassesContains(String packageName, final String str, boolean recursive) {

		return findClasses(packageName, new ClassFilter() {

			@Override
			public boolean accept(Class<?> clss) {
				if (clss.getSimpleName().contains(str)) {
					return true;
				}
				return false;
			}
		}, recursive);
	}

	public static Set<Class<?>> getChildClasses(String packageName, final Class<?> clazz) {

		return getChildClasses(packageName, clazz, true);
	}

	public static Set<Class<?>> getChildClasses(String packageName, final Class<?> clazz, boolean recursive) {

		return findClasses(packageName, new ClassFilter() {

			@Override
			public boolean accept(Class<?> clss) {
				if (clazz == clss) {
					return false; // 排除自身
				}
				if (clss.isInterface()) {
					return false; // 排除接口
				}
				if (Modifier.isAbstract(clss.getModifiers())) {
					return false; // 排除抽象类
				}
				if (clazz.isAssignableFrom(clss)) {
					return true;
				}
				return false;
			}
		}, recursive);
	}

	public static Set<Class<?>> getAllChildClasses(String packageName, final Class<?> clazz) {

		return getAllChildClasses(packageName, clazz, true);
	}

	public static Set<Class<?>> getAllChildClasses(String packageName, final Class<?> clazz, boolean recursive) {

		return findClasses(packageName, new ClassFilter() {

			@Override
			public boolean accept(Class<?> clss) {
				if (clazz == clss) {
					return false; // 排除自身
				}
				if (clazz.isAssignableFrom(clss)) {
					return true;
				}
				return false;
			}
		}, recursive);
	}

	public static Set<Class<?>> findClasses(String packageName, ClassFilter filter) {

		return findClasses(packageName, filter, true);
	}

	public static Set<Class<?>> findClasses(String packageName, ClassFilter filter, boolean recursive) {

		Set<Class<?>> classSet = getClasses(packageName, recursive);
		Set<Class<?>> set = new LinkedHashSet<Class<?>>();

		for (Class<?> clss : classSet) {
			if (filter.accept(clss)) {
				set.add(clss);
			}
		}

		return set;
	}

	public static Set<Class<?>> getClasses(String packageName) {

		return getClasses(packageName, true);
	}

	public static Set<Class<?>> getClasses(String packageName, boolean recursive) {

		Set<Class<?>> classSet = new LinkedHashSet<>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		String packageDir = packageName.replace('.', '/');

		try {

			Enumeration<URL> resources = classLoader.getResources(packageDir);
			while (resources.hasMoreElements()) {

				URL url = resources.nextElement();
				String protocol = url.getProtocol();

				if ("file".equals(protocol)) {

					String path = URLDecoder.decode(url.getFile(), "UTF-8");
					File dirFile = new File(path);

					fillClasses(classLoader, packageName, dirFile, recursive, classSet);

				} else if ("jar".equals(protocol)) {

					JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
					Enumeration<JarEntry> entries = jarFile.entries();
					while (entries.hasMoreElements()) {

						JarEntry entry = entries.nextElement();
						String name = entry.getName();

						if (name.startsWith(packageDir)) {
							if (!recursive && name.lastIndexOf('/') > packageDir.length()) {
								continue;
							}

							if (name.endsWith(".class") && !entry.isDirectory()) {
								String className = name.substring(0, name.length() - 6).replace('/', '.');
								try {
									classSet.add(classLoader.loadClass(className));
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
							}
						}

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return classSet;
	}

	private static void fillClasses(ClassLoader classLoader, String packageName, File dirFile, boolean recursive, Set<Class<?>> classSet) throws IOException {

		if (dirFile.exists() && dirFile.isDirectory()) {

			File[] files = dirFile.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					String fileName = file.getName();
					if (fileName.endsWith(".class")) {
						String className = fileName.substring(0, fileName.length() - 6);
						try {
							classSet.add(classLoader.loadClass(packageName + "." + className));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				} else {
					if (recursive) {
						String dirPath = file.getCanonicalPath();
						String dirItem = dirPath.substring(dirPath.lastIndexOf(File.separatorChar) + 1);
						String packageStr = packageName + "." + dirItem;
						fillClasses(classLoader, packageStr, file, recursive, classSet);
					}
				}
			}
		}

	}

}
