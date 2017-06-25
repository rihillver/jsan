package com.jsan.util.upload;

import java.nio.ByteBuffer;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

/**
 * 根据由大小写英文、数字组成的随机序列码命名（基本不重复）。
 * <p>
 * 默认长度为 11，指定长度时应大于 11。
 *
 */

public class SequenceCodeNamingAdapter implements NamingAdapter {

	private int length;

	public SequenceCodeNamingAdapter() {

	}

	public SequenceCodeNamingAdapter(int length) {

		this.length = length;
	}

	@Override
	public String getName(String fieldName, String fileName, int number) {

		if (length > 11) {
			return getSequenceCode(length);
		} else {
			return getSequenceCode(11);
		}
	}

	// ============================================================

	private static String getSequenceCode(int length) {

		String str = parseCurrentTimeMillisToSequenceCode();

		while (str.indexOf('/') > -1 || str.indexOf('+') > -1) {
			str = parseCurrentTimeMillisToSequenceCode();
		}
		if (length > 11) {
			int len = length - 11;
			str = getWordChar(len) + str;
		}

		return str;
	}

	private static final String WORDCHAR_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private static String getString(String str, int minLength, int maxLength) {

		if (str != null) {

			char[] charArray = str.toCharArray();
			int charLength = charArray.length;

			Random random = new Random();
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < minLength; i++) {
				sb.append(charArray[random.nextInt(charLength)]);
			}

			int value = maxLength - minLength;

			if (value > 0) {
				value = random.nextInt(value + 1);
				for (int i = 0; i < value; i++) {
					sb.append(charArray[random.nextInt(charLength)]);
				}
			}

			return sb.toString();

		} else {
			return "";
		}
	}

	private static String getWordChar(int minLength, int maxLength) {

		return getString(WORDCHAR_STRING, minLength, maxLength);
	}

	private static String getWordChar(int length) {

		return getWordChar(length, length);
	}

	private static String parseCurrentTimeMillisToSequenceCode() {

		long l = System.currentTimeMillis();
		int i = getInt(1, 999999);
		l *= i;
		byte[] buffer = parseLongToByte(l);
		String str = parseBase64EncodeWithoutEqualsSign(buffer);

		return str;
	}

	private static int getInt(int minValue, int maxValue) {

		Random random = new Random();

		int value = maxValue - minValue;
		value = random.nextInt(value + 1);
		value += minValue;

		return value;
	}

	private static String parseBase64EncodeWithoutEqualsSign(byte[] bytes) {

		String str = null;

		if (bytes != null) {
			str = DatatypeConverter.printBase64Binary(bytes);
			int index = str.indexOf('=');
			if (index > -1) {
				str = str.substring(0, index);
			}
		}

		return str;
	}

	private static byte[] parseLongToByte(long l) {

		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(0, l);
		return buffer.array();
	}
}
