package com.jsan.convert;

import java.util.Properties;

import com.jsan.convert.annotation.ConverterRegister;
import com.jsan.convert.annotation.DateTimePattern;
import com.jsan.convert.annotation.NumberPattern;

public class ConvertFuncUtils {

	public static void registerConverterAndFormatter(ConvertService service, ConverterRegister register,
			DateTimePattern dateTimePattern, NumberPattern numberPattern) {

		registerConverter(service, register);
		registerFormatterByDateTime(service, dateTimePattern);
		registerFormatterByNumber(service, numberPattern);
	}

	public static void registerConverter(ConvertService service, ConverterRegister register) {

		if (register != null) {
			Class<? extends Converter>[] converterClasses = register.value();
			for (Class<? extends Converter> converterClass : converterClasses) {
				service.registerConverter(converterClass);
			}
		}
	}

	public static void registerFormatterByDateTime(ConvertService service, DateTimePattern dateTimePattern) {

		if (dateTimePattern != null) {
			String pattern = dateTimePattern.value();
			for (Class<? extends Formatter> formatterClass : service.getDateTimeDeclareFormatterClassSet()) {
				executeRegisterFormatter(service, formatterClass, pattern);
			}
		}
	}

	public static void registerFormatterByNumber(ConvertService service, NumberPattern numberPattern) {

		if (numberPattern != null) {
			String pattern = numberPattern.value();
			for (Class<? extends Formatter> formatterClass : service.getNumberDeclareFormatterClassSet()) {
				executeRegisterFormatter(service, formatterClass, pattern);
			}
		}
	}

	private static void executeRegisterFormatter(ConvertService service, Class<? extends Formatter> formatterClass,
			String pattern) {

		Formatter formatter = createFormatterInstance(formatterClass);
		formatter.setPattern(pattern);
		service.registerFormatter(formatter);
	}

	private static Formatter createFormatterInstance(Class<? extends Formatter> formatterClass) {

		try {
			return BeanProxyUtils.newInstance(formatterClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// =============================================================

	public static Class<?> getClassByProperties(Properties properties, String key) {

		Class<?> clazz = null;
		String className = getStringByProperties(properties, key);

		if (className != null && !className.isEmpty()) {
			try {
				clazz = Class.forName(className);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return clazz;
	}

	public static Object getInstanceByProperties(Properties properties, String key) {

		Object object = null;

		Class<?> clazz = getClassByProperties(properties, key);
		if (clazz != null) {
			try {
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

	public static String getStringByProperties(Properties properties, String key) {

		if (properties != null) {
			return properties.getProperty(key);
		} else {
			return null;
		}

	}

	/**
	 * 首字母小写。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseFirstCharToLowerCase(String str) {

		if (str != null && str.length() > 0) {
			char firstChar = str.charAt(0);
			if (firstChar >= 'A' && firstChar <= 'Z') {
				char[] charArray = str.toCharArray();
				charArray[0] += ('a' - 'A');
				return new String(charArray);
			}
		}

		return str;
	}

	/**
	 * 下划线。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseCamelCaseToSnakeCase(String str) {

		return parseCamelCaseToGivenCase(str, '_');
	}

	/**
	 * 小驼峰。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseSnakeCaseToLowerCamelCase(String str) {

		return parseGivenCaseToCamelCase(str, '_', false);
	}

	/**
	 * 驼峰。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseSnakeCaseToCamelCase(String str) {

		return parseGivenCaseToCamelCase(str, '_', null);
	}

	private static String parseGivenCaseToCamelCase(String str, char c, Boolean firstCharacterUpperCase) {

		if (str == null || str.isEmpty()) {
			return str;
		}

		if (str.indexOf(c) == -1 && firstCharacterUpperCase == null) {
			return str;
		}

		char[] fromArray = str.toCharArray();
		int len = fromArray.length;
		char[] toArray = new char[len];

		int j = 0;
		boolean flag = false;
		for (int i = 0; i < len; i++) {
			if (fromArray[i] == c) {
				flag = true;
				continue;
			}
			if (flag) {
				if (i < len) {
					toArray[j++] = Character.toUpperCase(fromArray[i]);
				}
				flag = false;
			} else {
				toArray[j++] = fromArray[i];
			}
		}

		if (firstCharacterUpperCase != null) {
			if (firstCharacterUpperCase) {
				toArray[0] = Character.toUpperCase(toArray[0]);
			} else {
				toArray[0] = Character.toLowerCase(toArray[0]);
			}
		}

		return new String(toArray, 0, j);
	}

	private static String parseCamelCaseToGivenCase(String str, char c) {

		if (str == null) {
			return null;
		}

		char[] fromArray = str.toCharArray();
		char[] toArray = new char[fromArray.length * 2];

		int j = 0;
		for (int i = 0; i < fromArray.length; i++) {

			if (Character.isUpperCase(fromArray[i])) {
				if (i > 0) {
					toArray[j++] = c;
				}
				toArray[j++] = Character.toLowerCase(fromArray[i]);
			} else {
				toArray[j++] = fromArray[i];
			}
		}

		return new String(toArray, 0, j);
	}

	public static String parseAposToDouble(String str) {

		if (str == null) {
			return null;
		}

		if (str.indexOf('\'') == -1) {
			return str;
		}

		char[] fromArray = str.toCharArray();
		char[] toArray = new char[fromArray.length * 2];

		int j = 0;
		for (int i = 0; i < fromArray.length; i++) {
			if (fromArray[i] == '\'') {
				toArray[j++] = '\'';
			}
			toArray[j++] = fromArray[i];
		}

		return new String(toArray, 0, j);
	}

}
