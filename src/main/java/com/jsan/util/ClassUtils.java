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
 * Class类工具。
 * <p>
 * 注：<br>
 * 该工具类主要针对特定包下的类进行扫描和查找，常用的类工具方法可参见 Apache Commons 组件下的 org.apache.commons.lang3.ClassUtils。
 * 
 */

public class ClassUtils {

	/**
	 * 筛选接口。
	 *
	 */
	public static interface ClassFilter {

		boolean accept(Class<?> clss);
	}

	/**
	 * 获取类名以指定字符串开头的类。
	 * 
	 * @param packageName
	 * @param prefix
	 * @return
	 */
	public static Set<Class<?>> getClassesStartsWith(String packageName, String prefix) {

		return getClassesStartsWith(packageName, prefix, true);
	}

	/**
	 * 获取类名以指定字符串开头的类。
	 * 
	 * @param packageName
	 * @param prefix
	 * @param recursive
	 * @return
	 */
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

	/**
	 * 获取类名以指定字符串结尾的类。
	 * 
	 * @param packageName
	 * @param suffix
	 * @return
	 */
	public static Set<Class<?>> getClassesEndsWith(String packageName, String suffix) {

		return getClassesEndsWith(packageName, suffix, true);
	}

	/**
	 * 获取类名以指定字符串结尾的类。
	 * 
	 * @param packageName
	 * @param suffix
	 * @param recursive
	 * @return
	 */
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

	/**
	 * 获取类名包含指定字符串的类。
	 * 
	 * @param packageName
	 * @param str
	 * @return
	 */
	public static Set<Class<?>> getClassesContains(String packageName, String str) {

		return getClassesContains(packageName, str, true);
	}

	/**
	 * 获取类名包含指定字符串的类。
	 * 
	 * @param packageName
	 * @param str
	 * @param recursive
	 * @return
	 */
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

	/**
	 * 获取指定类的子类，或者指定接口的实现类（不含自身、不含接口、不含抽象类）。
	 * 
	 * @param packageName
	 * @param clazz
	 * @return
	 */
	public static Set<Class<?>> getChildClasses(String packageName, final Class<?> clazz) {

		return getChildClasses(packageName, clazz, true);
	}

	/**
	 * 获取指定类的子类，或者指定接口的实现类（不含自身、不含接口、不含抽象类）。
	 * 
	 * @param packageName
	 * @param clazz
	 * @param recursive
	 * @return
	 */
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

	/**
	 * 获取指定类的所有子类（不含自身、含接口、含抽象类）。
	 * 
	 * @param packageName
	 * @param clazz
	 * @return
	 */
	public static Set<Class<?>> getAllChildClasses(String packageName, final Class<?> clazz) {

		return getAllChildClasses(packageName, clazz, true);
	}

	/**
	 * 获取指定类的所有子类（不含自身、含接口、含抽象类）。
	 * 
	 * @param packageName
	 * @param clazz
	 * @param recursive
	 * @return
	 */
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

	/**
	 * 过滤筛选类。
	 * 
	 * @param packageName
	 * @param filter
	 * @return
	 */
	public static Set<Class<?>> findClasses(String packageName, ClassFilter filter) {

		return findClasses(packageName, filter, true);
	}

	/**
	 * 过滤筛选类。
	 * 
	 * @param packageName
	 * @param filter
	 * @param recursive
	 * @return
	 */
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

	/**
	 * 返回指定包下的所有类（含子包）。
	 * 
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClasses(String packageName) {

		return getClasses(packageName, true);
	}

	/**
	 * 返回指定包下的所有类（指定是否查找子包）。
	 * 
	 * @param packageName
	 * @param recursive
	 * @return
	 */
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
	
	/*********************************************************************/
	
	/**
	 * 从包package中获取所有的Class。
	 * 
	 * @param pack
	 * @return
	 
	public static Set<Class<?>> getClasses(String pack) {

		return getClasses(pack, true);
	}
	*/

	/**
	 * 从包package中获取所有的Class（指定是否查找子包）。
	 * 
	 * @param pack
	 * @param recursive
	 *            是否循环迭代
	 * @return
	 
	public static Set<Class<?>> getClasses(String pack, boolean recursive) {

		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// System.err.println("file类型的扫描");
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					// System.err.println("jar类型的扫描");
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								// if (idx != -1) {
								// 获取包名 把"/"替换成"."
								// packageName = name.substring(0, idx).replace('/', '.');
								// }
								// 如果可以迭代下去 并且是一个包
								if ((idx <= packageDirName.length()) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class") && !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										// String className = name.substring(packageName.length() + 1, name.length() - 6);
										String className = name.replace('/', '.');
										className = className.substring(0, className.length() - 6);

										try {
											// 添加到classes
											// classes.add(Class.forName(packageName + '.' + className));
											// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
											classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
										} catch (ClassNotFoundException e) {
											// log.error("添加用户自定义视图类错误找不到此类的.class文件");
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						// log.error("在扫描用户定义视图时从jar包获取文件出错");
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}
	*/

	/**
	 * 以文件的形式来获取包下的所有Class。
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 
	private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {

		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 添加到集合中去
					// classes.add(Class.forName(packageName + '.' + className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
	}
	*/

}
