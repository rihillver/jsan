package com.jsan.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DaoFuncUtils {

	public static Object getObjectByProperties(Properties properties, String key) {

		Object object = null;
		String className = null;

		if (properties != null) {
			className = properties.getProperty(key);
		}

		if (className != null && !className.isEmpty()) {
			try {
				Class<?> clazz = Class.forName(className);
				object = clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return object;
	}

	public static String[] getStringArrayByProperties(Properties properties, String key) {

		String[] strs = null;
		String str = null;

		if (properties != null) {
			str = properties.getProperty(key);
		}

		if (str != null && !str.isEmpty()) {
			strs = str.split(",");
			for (int i = 0; i < strs.length; i++) {
				strs[i] = strs[i].trim(); // 去除首尾空白
			}
		}

		return strs;
	}

	public static Properties getProperties(String path) throws IOException {

		InputStream inputStream = Object.class.getResourceAsStream(path);

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

	public static String parseFirstCharToLowerCase(String str) {

		if (str != null && str.length() > 0) {
			char firstChar = str.charAt(0);
			if (firstChar >= 'A' && firstChar <= 'Z') {
				char[] arr = str.toCharArray();
				arr[0] += ('a' - 'A');
				return new String(arr);
			}
		}

		return str;
	}

	/**
	 * 下划线
	 * 
	 * @param str
	 * @return
	 */
	public static String parseToSnakeCase(String str) {

		if (str == null) {
			return null;
		}

		int length = str.length();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c)) {
				if (i > 0) {
					sb.append('_');
				}
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 小驼峰
	 * 
	 * @param str
	 * @return
	 */
	public static String parseToLowerCamelCase(String str) {

		return parseToCamelCase(str, false);
	}

	/**
	 * 大驼峰
	 * 
	 * @param str
	 * @return
	 */
	public static String parseToUpperCamelCase(String str) {

		return parseToCamelCase(str, true);
	}

	/**
	 * 驼峰
	 * 
	 * @param str
	 * @return
	 */
	public static String parseToCamelCase(String str) {

		return parseToCamelCase(str, null);
	}

	public static String parseToCamelCase(String str, Boolean firstCharacterUpperCase) {

		if (str == null) {
			return null;
		}

		if (str.indexOf('_') > -1) { // 存在下划线的情况才进行

			StringBuilder sb = new StringBuilder();
			boolean nextUpperCase = false;
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (c == '_') {
					nextUpperCase = true;
					continue;
				}

				if (nextUpperCase) {
					sb.append(Character.toUpperCase(c));
					nextUpperCase = false;
				} else {
					sb.append(c);
				}
			}

			// 如果 firstCharacterUpperCase 为 null 则不对首字母做转换处理
			if (firstCharacterUpperCase != null) {
				if (firstCharacterUpperCase) {
					sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
				} else {
					sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
				}
			}

			str = sb.toString();

		} else {
			// 如果 firstCharacterUpperCase 为 null 或 str 为空则不对首字母做转换处理
			if (firstCharacterUpperCase != null && !str.isEmpty()) {
				if (firstCharacterUpperCase) {
					str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
				} else {
					str = Character.toLowerCase(str.charAt(0)) + str.substring(1);
				}
			}
		}

		return str;
	}

}
