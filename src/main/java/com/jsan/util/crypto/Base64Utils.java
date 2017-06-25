package com.jsan.util.crypto;

import java.io.IOException;

/**
 * Base64 工具类（基于 net.iharder.Base64）。
 * <p>
 * 
 * 另可参：<br>
 * （javax.xml.bind.DatatypeConverter.class） <br>
 * Java 8 （java.util.Base64.class） <br>
 * Apache Commons Codec （Base64.class） <br>
 * Google Guava （BaseEncoding.class） <br>
 * MiGBase64 （Base64.class） <br>
 * Bouncy Castle （Base64.class）
 *
 */

public class Base64Utils {

	/**
	 * Base64 编码（指定选项）。
	 * 
	 * @param source
	 * @param options
	 * @return
	 */
	public static String encode(byte[] source, int options) {

		try {
			return Base64.encodeBytes(source, options);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Base64 编码。
	 * 
	 * @param source
	 * @return
	 */
	public static String encode(byte[] source) {

		return encode(source, Base64.NO_OPTIONS);
	}

	/**
	 * Base64 编码。
	 * 
	 * @param source
	 * @return
	 */
	public static String encode(String source) {

		return encode(source.getBytes(), Base64.NO_OPTIONS);
	}

	/**
	 * Base64 编码（URL 模式，将 "+" 替换成 "-" 、 "/" 替换成 "_"、去除 "="）。
	 * 
	 * @param source
	 * @return
	 */
	public static String encodeUrlSafe(byte[] source) {

		return removeEqualsSign(encode(source, Base64.URL_SAFE));
	}

	/**
	 * Base64 编码（URL 模式，将 "+" 替换成 "-" 、 "/" 替换成 "_"、去除 "="）。
	 * 
	 * @param source
	 * @return
	 */
	public static String encodeUrlSafe(String source) {

		return removeEqualsSign(encode(source.getBytes(), Base64.URL_SAFE));
	}

	/**
	 * Base64 解码（指定选项）。
	 * 
	 * @param source
	 * @param options
	 * @return
	 */
	public static byte[] decode(String source, int options) {

		try {
			return Base64.decode(source, options);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Base64 解码。
	 * 
	 * @param source
	 * @return
	 */
	public static byte[] decodeToByte(String source) {

		return decode(source, Base64.NO_OPTIONS);
	}

	/**
	 * Base64 解码。
	 * 
	 * @param source
	 * @return
	 */
	public static String decodeToString(String source) {

		return new String(decode(source, Base64.NO_OPTIONS));
	}

	/**
	 * Base64 解码（URL 模式，将 "+" 替换成 "-" 、 "/" 替换成 "_"、去除 "="）。
	 * 
	 * @param source
	 * @return
	 */
	public static byte[] decodeUrlSafeToByte(String source) {

		return decode(paddingEqualsSign(source), Base64.URL_SAFE);
	}

	/**
	 * Base64 解码（URL 模式，将 "+" 替换成 "-" 、 "/" 替换成 "_"、去除 "="）。
	 * 
	 * @param source
	 * @return
	 */
	public static String decodeUrlSafeToString(String source) {

		return new String(decode(paddingEqualsSign(source), Base64.URL_SAFE));
	}

	/**
	 * 根据情况填补尾部的等号。
	 * <p>
	 * 如果长度不是 4 的倍数的情况则加上填补符号（=）。
	 * 
	 * @param source
	 * @return
	 */
	public static String paddingEqualsSign(String source) {

		int len = source.length() % 4;

		if (len == 2) {
			source += "==";
		} else if (len == 3) {
			source += "=";
		}

		return source;
	}

	/**
	 * 根据情况移除尾部的等号。
	 * 
	 * @param source
	 * @return
	 */
	public static String removeEqualsSign(String source) {

		int index = source.indexOf('=');
		if (index > -1) {
			return source.substring(0, index);
		}

		return source;
	}
}
