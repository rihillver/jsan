package com.jsan.util;

/**
 * 常用转换工具类。
 *
 */

public class ParseUtils {

	/**
	 * 将首字母转小写。
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
	 * 将首字母转大写。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseFirstCharToUpperCase(String str) {

		if (str != null && str.length() > 0) {
			char firstChar = str.charAt(0);
			if (firstChar >= 'a' && firstChar <= 'z') {
				char[] charArray = str.toCharArray();
				charArray[0] -= ('a' - 'A');
				return new String(charArray);
			}
		}

		return str;
	}

	/**
	 * 将 byte[] 制转换成 16 进制字符串。
	 * 
	 * @param bytes
	 * @return
	 */
	public static String parseByteToHexString(byte[] bytes) {

		if (bytes == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex);
		}

		return sb.toString();
	}

	/**
	 * 将 16 进制字符串转换为 byte[] 。
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] parseHexStringToByte(String hexString) {

		if (hexString == null) {
			return null;
		}

		byte[] result = new byte[hexString.length() / 2];

		for (int i = 0; i < hexString.length() / 2; i++) {
			int high = Integer.parseInt(hexString.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexString.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}

		return result;
	}

	/**
	 * 将字符串转换成 Unicode 形式的字符串。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseStringToUnicode(String str) {

		return parseStringToUnicode(str, false);
	}

	/**
	 * 将字符串转换成 Unicode 形式的字符串，并指定是否不包括 ASCII 编码的转换（false：包括、true：不包括）。
	 * 
	 * @param str
	 * @param excludeASCII
	 * @return
	 */
	public static String parseStringToUnicode(String str, boolean excludeASCII) {

		if (str != null) {

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (excludeASCII && c < 128) {
					sb.append(c);
				} else {
					sb.append(String.format("\\u%04x", (int) c));
				}
			}

			str = sb.toString();
		}

		return str;
	}

	/**
	 * 将 Unicode 形式的字符串转换成字符串。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseUnicodeToString(String str) {

		if (str != null && str.contains("\\u")) {

			StringBuilder sb = new StringBuilder();

			String[] hexs = str.split("\\\\u");

			for (int i = 1; i < hexs.length; i++) {

				if (hexs[i].length() > 4) {
					int c = Integer.parseInt(hexs[i].substring(0, 4), 16);
					sb.append((char) c);
					sb.append(hexs[i].substring(4));
				} else {
					int c = Integer.parseInt(hexs[i], 16);
					sb.append((char) c);
				}
			}

			str = sb.toString();
		}

		return str;
	}

	/**
	 * 将驼峰形式的字符串转换成带下划线形式的字符串。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseCamelCaseToSnakeCase(String str) {

		return parseCamelCaseToGivenCase(str, '_');
	}

	/**
	 * 将驼峰形式的字符串转换成带中横杠形式的字符串。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseCamelCaseToKebabCase(String str) {

		return parseCamelCaseToGivenCase(str, '-');
	}

	/**
	 * 将含下划线的字符串转换成驼峰形式的字符串。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseSnakeCaseToCamelCase(String str) {

		return parseGivenCaseToCamelCase(str, '_', null);
	}

	/**
	 * 将含下划线的字符串转换成驼峰形式的字符串（小驼峰）。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseSnakeCaseToLowerCamelCase(String str) {

		return parseGivenCaseToCamelCase(str, '_', false);
	}

	/**
	 * 将含下划线的字符串转换成驼峰形式的字符串（大驼峰）。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseSnakeCaseToUpperCamelCase(String str) {

		return parseGivenCaseToCamelCase(str, '_', true);
	}

	/**
	 * 将含中横杠的字符串转换成驼峰形式的字符串。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseKebabCaseToCamelCase(String str) {

		return parseGivenCaseToCamelCase(str, '-', null);
	}

	/**
	 * 将含中横杠的字符串转换成驼峰形式的字符串（小驼峰）。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseKebabCaseToLowerCamelCase(String str) {

		return parseGivenCaseToCamelCase(str, '-', false);
	}

	/**
	 * 将含中横杠的字符串转换成驼峰形式的字符串（大驼峰）。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseKebabCaseToUpperCamelCase(String str) {

		return parseGivenCaseToCamelCase(str, '-', true);
	}

	/**
	 * 将含指定字符（下划线、中横杠等）的字符串转换成驼峰形式的字符串。
	 * 
	 * @param str
	 * @param firstCharacterUpperCase
	 *            大驼峰为true，小驼峰为false
	 * @param c
	 * @return
	 */
	public static String parseGivenCaseToCamelCase(String str, char c, Boolean firstCharacterUpperCase) {

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

	/**
	 * 将驼峰形式的字符串转换成带指定字符（下划线、中横杠等）形式的字符串。
	 * 
	 * @param str
	 * @param c
	 * @return
	 */
	public static String parseCamelCaseToGivenCase(String str, char c) {

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

	/**
	 * 将单引号替换成双单引号，避免使用 str.replace("'", "''") 这种正则表达式的方式。
	 * 
	 * @param str
	 * @return
	 */
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

	/**
	 * 将单引号去除，避免使用 str.replace("'", "") 这种正则表达式的方式。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseAposToNone(String str) {

		if (str == null) {
			return null;
		}

		if (str.indexOf('\'') == -1) {
			return str;
		}

		char[] fromArray = str.toCharArray();
		char[] toArray = new char[fromArray.length];

		int j = 0;
		for (int i = 0; i < fromArray.length; i++) {
			if (fromArray[i] == '\'') {
				continue;
			}
			toArray[j++] = fromArray[i];
		}

		return new String(toArray, 0, j);
	}

}
