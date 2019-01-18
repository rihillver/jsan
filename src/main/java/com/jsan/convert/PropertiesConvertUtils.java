package com.jsan.convert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger logger = LoggerFactory.getLogger(PropertiesConvertUtils.class);

	private static final String PROPERTIES_SUFFIX = ".properties";
	private static final ConvertService defaultConvertService = new SplitTrimConvertService();

	public static <T> T getObject(Class<T> clazz) {

		return getObjectEnhanced(null, null, clazz, defaultConvertService, null);
	}

	/**
	 * 在 classes 根目录下根据类名寻找对应的 properties 文件，并转换成对象（默认使用 SplitTrimConvertService
	 * 进行转换）。
	 * 
	 * @param clazz
	 * @param keyPrefix
	 * @return
	 */
	public static <T> T getObjectEnhanced(Class<T> clazz, String keyPrefix) {

		return getObjectEnhanced(null, null, clazz, defaultConvertService, keyPrefix);
	}

	public static <T> T getObject(Class<T> clazz, ConvertService service) {

		return getObjectEnhanced(null, null, clazz, service, null);
	}

	/**
	 * 在 classes 根目录下根据类名寻找对应的 properties 文件，并转换成对象（指定 ConvertService 进行转换）。
	 * 
	 * @param clazz
	 * @param service
	 * @param keyPrefix
	 * @return
	 */
	public static <T> T getObjectEnhanced(Class<T> clazz, ConvertService service, String keyPrefix) {

		return getObjectEnhanced(null, null, clazz, service, keyPrefix);
	}

	public static <T> T getObject(String path, Class<T> clazz) {

		return getObjectEnhanced(path, clazz, defaultConvertService, null);
	}

	/**
	 * 将指定路径下的 properties 文件转换成对象（默认使用 SplitTrimConvertService 进行转换）。<br>
	 *
	 * @param path
	 * @param clazz
	 * @param keyPrefix
	 * @return
	 */
	public static <T> T getObjectEnhanced(String path, Class<T> clazz, String keyPrefix) {

		return getObjectEnhanced(path, clazz, defaultConvertService, keyPrefix);
	}

	public static <T> T getObject(String dirPath, String fileName, Class<T> clazz) {

		return getObjectEnhanced(dirPath, fileName, clazz, defaultConvertService, null);
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
	 * @param keyPrefix
	 * @return
	 */
	public static <T> T getObjectEnhanced(String dirPath, String fileName, Class<T> clazz, String keyPrefix) {

		return getObjectEnhanced(dirPath, fileName, clazz, defaultConvertService, keyPrefix);
	}

	public static <T> T getObject(String path, Class<T> clazz, ConvertService service) {

		return getObjectEnhanced(path, clazz, service, null);
	}

	/**
	 * 将指定路径下的 properties 文件转换成对象（指定 ConvertService 进行转换）。
	 * 
	 * @param path
	 * @param clazz
	 * @param service
	 * @param keyPrefix
	 * @return
	 */
	public static <T> T getObjectEnhanced(String path, Class<T> clazz, ConvertService service, String keyPrefix) {

		Properties properties = getProperties(path);
		if (properties == null) {
			properties = new Properties();
		}

		if (keyPrefix != null && keyPrefix.length() > 0) {
			properties = handleProperties(properties, keyPrefix);
		}

		return BeanConvertUtils.getBean(clazz, properties, service);
	}

	public static <T> T getObject(String dirPath, String fileName, Class<T> clazz, ConvertService service) {

		return getObjectEnhanced(dirPath, fileName, clazz, service, null);
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
	 * @param keyPrefix
	 * @return
	 */
	public static <T> T getObjectEnhanced(String dirPath, String fileName, Class<T> clazz, ConvertService service,
			String keyPrefix) {

		Properties properties = null;

		if (fileName == null) { // 文件名未指定的情况下
			fileName = clazz.getSimpleName();
			properties = getProperties(dirPath, fileName + PROPERTIES_SUFFIX); // 文件名按原始的类名尝试
			if (properties == null) {
				properties = getProperties(dirPath,
						Character.toLowerCase(fileName.charAt(0)) + fileName.substring(1) + PROPERTIES_SUFFIX); // 文件名按第一个字母小写的类名尝试
			}
			if (properties == null) {
				properties = getProperties(dirPath, fileName.toLowerCase() + PROPERTIES_SUFFIX); // 文件名按小写的类名尝试
			}
			if (properties == null) {
				properties = getProperties(dirPath, fileName.toUpperCase() + PROPERTIES_SUFFIX); // 文件名按大写的类名尝试
			}
		} else {
			properties = getProperties(dirPath, fileName);
		}

		if (properties == null) {
			properties = new Properties();
		}

		if (keyPrefix != null && keyPrefix.length() > 0) {
			properties = handleProperties(properties, keyPrefix);
		}

		return BeanConvertUtils.getBean(clazz, properties, service);
	}

	private static Properties handleProperties(Properties properties, String keyPrefix) {

		Properties prop = new Properties();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String key = entry.getKey().toString();
			int len = keyPrefix.length();
			if (key.startsWith(keyPrefix)) {
				prop.setProperty(key.substring(len), entry.getValue().toString());
			}
		}

		return prop;
	}

	public static void setObject(Object obj) {

		setObjectEnhanced(obj, null, null, null);
	}

	public static void setObjectEnhanced(Object obj, String keyPrefix) {

		setObjectEnhanced(obj, null, null, keyPrefix);
	}

	public static void setObject(Object obj, String path) {

		setObjectEnhanced(obj, path, null);
	}

	/**
	 * 先判断是否为 Map， 再判断是否为 Bean 对象（支持继承的 Bean）。
	 * 
	 * @param obj
	 * @param path
	 * @param keyPrefix
	 */
	public static void setObjectEnhanced(Object obj, String path, String keyPrefix) {

		Properties properties;
		properties = getProperties(path);
		if (properties == null) {
			properties = new Properties();
		}

		if (keyPrefix == null) {
			keyPrefix = "";
		}

		if (Map.class.isAssignableFrom(obj.getClass())) { // Map 的情况下
			for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (key != null) {
					handlePropertiesItem(properties, keyPrefix + entry.getKey(), value);
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
				handlePropertiesItem(properties, keyPrefix + entry.getKey(), value);
			}
		}

		storeProperties(properties, path);
	}

	private static void handlePropertiesItem(Properties properties, String key, Object value) {

		if (value != null) {
			properties.setProperty(key, value.toString());
		} else {
			properties.remove(key); // 若值为null时则移除对应的key
		}
	}

	public static void setObject(Object obj, String dirPath, String fileName) {

		setObjectEnhanced(obj, dirPath, fileName, null);
	}

	/**
	 * 文件名未指定的情况下去其类名的小写。
	 * 
	 * @param obj
	 * @param dirPath
	 * @param fileName
	 * @param keyPrefix
	 */
	public static void setObjectEnhanced(Object obj, String dirPath, String fileName, String keyPrefix) {

		dirPath = getQualifiedDirPath(dirPath);

		if (fileName == null) { // 文件名未指定的情况下，默认为类名小写.properties ，在 classes 目录下
			fileName = obj.getClass().getSimpleName().toLowerCase() + PROPERTIES_SUFFIX;
		}

		setObjectEnhanced(obj, dirPath + fileName, keyPrefix);
	}

	/**
	 * 读取指定路径的 properties 文件并转换成 Properties 对象（仅相对于 classes 目录下）。
	 * <p>
	 * 抛出 IOException 异常。
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Properties loadProperties(String path) throws IOException {

		InputStream inputStream = ConvertFuncUtils.class.getResourceAsStream(path);

		if (inputStream == null) {
			throw new IOException("failed to open the file: " + path);
		}

		Properties properties = new Properties();

		try {
			properties.load(inputStream);
		} catch (Exception e) {
			throw new IOException("failed to read the file: " + path);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return properties;
	}

	/**
	 * 读取指定路径目录和文件名的 properties 文件并转换成 Properties 对象（仅相对于 classes 目录下）。
	 * <p>
	 * 抛出 IOException 异常。
	 * 
	 * @param dirPath
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Properties loadProperties(String dirPath, String fileName) throws IOException {

		dirPath = getQualifiedDirPath(dirPath);

		return loadProperties(dirPath + fileName);
	}

	/**
	 * 读取指定路径的 properties 文件并转换成 Properties 对象（仅相对于 classes 目录下）。
	 * <p>
	 * IOException 异常时返回 null。
	 * 
	 * @param path
	 * @return
	 */
	public static Properties getProperties(String path) {

		try {
			return loadProperties(path);
		} catch (IOException e) {
			logger.warn("failed to open the file: {}", path);
			return null;
		}
	}

	/**
	 * 读取指定路径目录和文件名的 properties 文件并转换成 Properties 对象（仅相对于 classes
	 * 目录下）。
	 * <p>
	 * IOException 异常时返回 null。
	 * 
	 * @param dirPath
	 * @param fileName
	 * @return
	 */
	public static Properties getProperties(String dirPath, String fileName) {

		try {
			return loadProperties(dirPath, fileName);
		} catch (IOException e) {
			logger.warn("failed to open the file: {}", getQualifiedDirPath(dirPath) + fileName);
			return null;
		}
	}

	public static Map<String, Object> getMap(String path) {

		return getMapEnhanced(path, null);
	}

	public static Map<String, Object> getMap(String dirPath, String fileName) {

		return getMapEnhanced(dirPath, fileName, null);
	}

	public static Map<String, Object> getMapEnhanced(String path, String keyPrefix) {

		Properties properties = getProperties(path);
		if (properties == null) {
			properties = new Properties();
		}

		Map<String, Object> map = new HashMap<String, Object>();

		if (keyPrefix != null && keyPrefix.length() > 0) {
			int len = keyPrefix.length();
			for (Map.Entry<Object, Object> entry : properties.entrySet()) {
				String key = entry.getKey().toString();
				if (key.startsWith(keyPrefix)) {
					map.put(key.substring(len), entry.getValue());
				}
			}
		} else {
			for (Map.Entry<Object, Object> entry : properties.entrySet()) {
				map.put(entry.getKey().toString(), entry.getValue());
			}
		}

		return map;
	}

	public static Map<String, Object> getMapEnhanced(String dirPath, String fileName, String keyPrefix) {

		dirPath = getQualifiedDirPath(dirPath);

		return getMapEnhanced(dirPath + fileName, keyPrefix);
	}

	public static void setMap(Map<?, ?> map, String path) {

		setMapEnhanced(map, path, null);
	}

	public static void setMap(Map<?, ?> map, String dirPath, String fileName) {

		setMapEnhanced(map, dirPath, fileName, null);
	}

	public static void setMapEnhanced(Map<?, ?> map, String path, String keyPrefix) {

		setObjectEnhanced(map, path, keyPrefix);
	}

	/**
	 * 如果要将一个键从properties文件中移除，将其值设置为null即可移除（使用remove()方法移除键将无法从properties文件中移除）。
	 * 
	 * @param map
	 * @param dirPath
	 * @param fileName
	 * @param keyPrefix
	 */
	public static void setMapEnhanced(Map<?, ?> map, String dirPath, String fileName, String keyPrefix) {

		dirPath = getQualifiedDirPath(dirPath);

		setMapEnhanced(map, dirPath + fileName, keyPrefix);
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

		URL url = PropertiesConvertUtils.class.getResource("/");

		try {

			File file = new File(url.getPath(), path);

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
