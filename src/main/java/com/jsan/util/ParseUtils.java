package com.jsan.util;

import java.math.BigInteger;

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
				char[] arr = str.toCharArray();
				arr[0] += ('a' - 'A');
				return new String(arr);
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
				char[] arr = str.toCharArray();
				arr[0] -= ('a' - 'A');
				return new String(arr);
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
	 * 将 16 进制字符串转换成大整数。
	 * 
	 * @param hexString
	 * @return
	 */
	public static BigInteger parseHexStringToBigInteger(String hexString) {

		try {
			return new BigInteger(hexString, 16);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将大整数转换成 16 进制字符串。
	 * 
	 * @param bigInteger
	 * @return
	 */
	public static String parseBigIntegerToHexString(BigInteger bigInteger) {

		if (bigInteger == null) {
			return null;
		}

		return bigInteger.toString(16);
	}

	/**
	 * 将 10 进制字符串转换成大整数。
	 * 
	 * @param hexString
	 * @return
	 */
	public static BigInteger parseStringToBigInteger(String str) {

		try {
			return new BigInteger(str);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将大整数转换成 10 进制字符串。
	 * 
	 * @param bigInteger
	 * @return
	 */
	public static String parseBigIntegerToString(BigInteger bigInteger) {

		if (bigInteger == null) {
			return null;
		}

		return bigInteger.toString();
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

		return parseCamelCaseTo(str, '_');
	}

	/**
	 * 将驼峰形式的字符串转换成带中横杠形式的字符串。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseCamelCaseToKebabCase(String str) {

		return parseCamelCaseTo(str, '-');
	}

	/**
	 * 将含下划线的字符串转换成驼峰形式的字符串。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseSnakeCaseToCamelCase(String str) {

		return parseToCamelCase(str, '_', null);
	}

	/**
	 * 将含下划线的字符串转换成驼峰形式的字符串（小驼峰）。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseSnakeCaseToLowerCamelCase(String str) {

		return parseToCamelCase(str, '_', false);
	}

	/**
	 * 将含下划线的字符串转换成驼峰形式的字符串（大驼峰）。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseSnakeCaseToUpperCamelCase(String str) {

		return parseToCamelCase(str, '_', true);
	}

	/**
	 * 将含中横杠的字符串转换成驼峰形式的字符串。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseKebabCaseToCamelCase(String str) {

		return parseToCamelCase(str, '-', null);
	}

	/**
	 * 将含中横杠的字符串转换成驼峰形式的字符串（小驼峰）。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseKebabCaseToLowerCamelCase(String str) {

		return parseToCamelCase(str, '-', false);
	}

	/**
	 * 将含中横杠的字符串转换成驼峰形式的字符串（大驼峰）。
	 * 
	 * @param str
	 * @return
	 */
	public static String parseKebabCaseToUpperCamelCase(String str) {

		return parseToCamelCase(str, '-', true);
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
	public static String parseToCamelCase(String str, char c, Boolean firstCharacterUpperCase) {

		if (str == null) {
			return null;
		}

		if (str.indexOf(c) > -1) { // 存在指定字符的情况才进行

			StringBuilder sb = new StringBuilder();
			boolean nextUpperCase = false;
			for (int i = 0; i < str.length(); i++) {
				char ch = str.charAt(i);
				if (ch == c) {
					nextUpperCase = true;
					continue;
				}

				if (nextUpperCase) {
					sb.append(Character.toUpperCase(ch));
					nextUpperCase = false;
				} else {
					sb.append(ch);
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

	/**
	 * 将驼峰形式的字符串转换成带指定字符（下划线、中横杠等）形式的字符串。
	 * 
	 * @param str
	 * @param c
	 * @return
	 */
	public static String parseCamelCaseTo(String str, char c) {

		if (str == null) {
			return null;
		}

		int length = str.length();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			char ch = str.charAt(i);
			if (Character.isUpperCase(ch)) {
				if (i > 0) {
					sb.append(c);
				}
				sb.append(Character.toLowerCase(ch));
			} else {
				sb.append(ch);
			}
		}

		return sb.toString();
	}

}
