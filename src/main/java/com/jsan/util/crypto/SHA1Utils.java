package com.jsan.util.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * SHA1 散列算法工具类。
 * <p>
 * 更专业的可参 Bouncy Castle。
 *
 */

public class SHA1Utils {

	private static final String ALGORITHM = "SHA-1";

	/**
	 * 返回 SHA1 散列码。
	 * 
	 * @param bytes
	 * @return
	 */
	public static String encrypt(byte[] bytes) {

		try {
			MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
			digest.update(bytes);
			byte[] buff = digest.digest();
			return parseByteToHexString(buff);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 返回 SHA1 散列码。
	 * 
	 * @param str
	 * @return
	 */
	public static String encrypt(String str) {

		return encrypt(str.getBytes());
	}

	/**
	 * 返回 SHA1 散列码。
	 * 
	 * @param filePath
	 * @return
	 */
	public static String encryptByFile(String filePath) {

		File file = new File(filePath);
		return encryptByFile(file);
	}

	/**
	 * 返回 SHA1 散列码。
	 * 
	 * @param file
	 * @return
	 */
	public static String encryptByFile(File file) {

		InputStream in = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
			in = new FileInputStream(file);
			byte[] buffer = new byte[1024 * 4];
			while (in.read(buffer) != -1) {
				digest.update(buffer);
			}
			byte[] buff = digest.digest();
			return parseByteToHexString(buff);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将 byte[] 转换成 16 进制字符串。
	 * 
	 * @param bytes
	 * @return
	 */
	public static String parseByteToHexString(byte[] bytes) {

		if (bytes == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			sb.append(Character.forDigit((bytes[i] & 240) >> 4, 16));
			sb.append(Character.forDigit(bytes[i] & 15, 16));
		}

		return sb.toString();
	}

}
