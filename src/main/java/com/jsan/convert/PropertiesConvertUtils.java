package com.jsan.convert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.jsan.convert.cache.BeanInformationCache;

/**
 * 读取资源文件的工具类。
 * 
 * <ul>
 * <li>并不强制要求必须是后缀为 properties 类型的文件，只要按照 properties 文件规范的文件均可正常操作。</li>
 * <li>路径都只相对于 classes 目录下进行，该工具类不能对其他目录下的 properties 文件进行操作，一般情况下 properties
 * 文件也不会放在非 classes 目录下。</li>
 * </ul>
 *
 */

public class PropertiesConvertUtils {

	private static final String PROPERTIES_SUFFIX = ".properties";
	private static final ConvertService defaultConvertService = new SplitTrimConvertService();

	/**
	 * 在 classes 根目录下根据类名寻找对应的 properties 文件，并转换成对象（默认使用 SplitTrimConvertService
	 * 进行转换）。
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getObject(Class<T> clazz) {

		return getObject(clazz, defaultConvertService);
	}

	/**
	 * 在 classes 根目录下根据类名寻找对应的 properties 文件，并转换成对象（指定 ConvertService 进行转换）。
	 * 
	 * @param clazz
	 * @param service
	 * @return
	 */
	public static <T> T getObject(Class<T> clazz, ConvertService service) {

		return getObject(null, null, clazz, service);
	}

	/**
	 * 将指定路径下的 properties 文件转换成对象（默认使用 SplitTrimConvertService 进行转换）。<br>
	 *
	 * @param path
	 * @param clazz
	 * @return
	 */
	public static <T> T getObject(String path, Class<T> clazz) {

		return getObject(path, clazz, defaultConvertService);
	}

	/**
	 * 将指定目录和文件名的 properties 文件转换成对象（默认使用 SplitTrimConvertService 进行转换）。
	 * <p>
	 * 当指定的文件名为 null 的时候，properties
	 * 文件名则按照类名依次寻找（依次按照原始类名、第一个字母小写的类名、全小写的类名、全大写的类名）
	 * 
	 * @param dirPath
	 * @param fileName
	 * @param clazz
	 * @return
	 */
	public static <T> T getObject(String dirPath, String fileName, Class<T> clazz) {

		return getObject(dirPath, fileName, clazz, defaultConvertService);
	}

	/**
	 * 将指定路径下的 properties 文件转换成对象（指定 ConvertService 进行转换）。
	 * 
	 * @param path
	 * @param clazz
	 * @param service
	 * @return
	 */
	public static <T> T getObject(String path, Class<T> clazz, ConvertService service) {

		Properties properties = loadProperties(path);

		return BeanConvertUtils.getObject(clazz, properties, service);
	}

	/**
	 * 将指定目录和文件名的 properties 文件转换成对象（指定 ConvertService 进行转换）。
	 * <p>
	 * 当指定的文件名为 null 的时候，properties
	 * 文件名则按照类名依次寻找（依次按照原始类名、第一个字母小写的类名、全小写的类名、全大写的类名）
	 * 
	 * @param dirPath
	 * @param fileName
	 * @param clazz
	 * @param service
	 * @return
	 */
	public static <T> T getObject(String dirPath, String fileName, Class<T> clazz, ConvertService service) {

		Properties properties = null;

		if (fileName == null) { // 文件名未指定的情况下
			fileName = clazz.getSimpleName();
			try {
				properties = loadProperties(dirPath, fileName + PROPERTIES_SUFFIX); // 文件名按原始的类名尝试
			} catch (Exception e1) {
				try {
					properties = loadProperties(dirPath,
							Character.toLowerCase(fileName.charAt(0)) + fileName.substring(1) + PROPERTIES_SUFFIX); // 文件名按第一个字母小写的类名尝试
				} catch (Exception e2) {
					try {
						properties = loadProperties(dirPath, fileName.toLowerCase() + PROPERTIES_SUFFIX); // 文件名按小写的类名尝试
					} catch (Exception e3) {
						properties = loadProperties(dirPath, fileName.toUpperCase() + PROPERTIES_SUFFIX); // 文件名按大写的类名尝试
					}
				}
			}
		} else {
			properties = loadProperties(dirPath, fileName);
		}

		return BeanConvertUtils.getObject(clazz, properties, service);
	}

	public static void setObject(Object obj) {

		setObject(obj, null, null);
	}

	/**
	 * 先判断是否为 Map， 再判断是否为 Bean 对象（支持继承的 Bean）。
	 * 
	 * @param obj
	 * @param path
	 */
	public static void setObject(Object obj, String path) {

		Properties properties = new Properties();

		if (Map.class.isAssignableFrom(obj.getClass())) { // Map 的情况下
			for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (key != null && value != null) {
					properties.setProperty(key.toString(), value.toString());
				}
			}
		} else { // Bean 的情况下
			Map<String, Method> map = BeanInformationCache.getReadMethodMap(obj.getClass());
			for (Map.Entry<String, Method> entry : map.entrySet()) {
				Object value = null;
				try {
					value = entry.getValue().invoke(obj);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (value != null) {
					properties.setProperty(entry.getKey(), value.toString());
				}
			}
		}

		storeProperties(properties, path);
	}

	/**
	 * 文件名未指定的情况下去其类名的小写。
	 * 
	 * @param obj
	 * @param dirPath
	 * @param fileName
	 */
	public static void setObject(Object obj, String dirPath, String fileName) {

		dirPath = getQualifiedDirPath(dirPath);

		if (fileName == null) { // 文件名未指定的情况下，默认为类名小写.properties ，在 classes 目录下
			fileName = obj.getClass().getSimpleName().toLowerCase() + PROPERTIES_SUFFIX;
		}

		setObject(obj, dirPath + fileName);
	}

	/**
	 * 读取指定路径的 properties 文件并转换成 Properties 对象（仅相对于 classes 目录下）。
	 * 
	 * @param path
	 * @return
	 */
	public static Properties loadProperties(String path) {

		try {
			return ConvertFuncUtils.getProperties(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 读取指定路径目录和文件名的 properties 文件并转换成 Properties 对象（仅相对于 classes 目录下）。
	 * 
	 * @param dirPath
	 * @param fileName
	 * @return
	 */
	public static Properties loadProperties(String dirPath, String fileName) {

		dirPath = getQualifiedDirPath(dirPath);

		return loadProperties(dirPath + fileName);
	}

	private static String getQualifiedDirPath(String dirPath) {

		if (dirPath == null) {
			dirPath = "/";
		}

		if (!dirPath.startsWith("/")) {
			dirPath = "/" + dirPath;
		}

		if (!dirPath.endsWith("/")) {
			dirPath = dirPath + "/";
		}

		return dirPath;
	}

	public static void storeProperties(Properties properties, String path) {

		storeProperties(properties, path, false);
	}

	public static void storeProperties(Properties properties, String path, boolean append) {

		URL url = Object.class.getResource("/");

		try {

			String canonicalPath = new File(url.getPath()).getCanonicalPath();

			File file = new File(canonicalPath + path);

			File parentFile = file.getParentFile();

			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}

			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(file, append);

			properties.store(fos, null);

			fos.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void storeProperties(Properties properties, String dirPath, String fileName) {

		storeProperties(properties, dirPath, fileName, false);
	}

	public static void storeProperties(Properties properties, String dirPath, String fileName, boolean append) {

		dirPath = getQualifiedDirPath(dirPath);

		storeProperties(properties, dirPath + fileName, append);
	}

}
