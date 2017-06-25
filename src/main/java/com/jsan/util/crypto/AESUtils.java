package com.jsan.util.crypto;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 加密工具类。
 * <p>
 * 更专业的可参 Bouncy Castle。
 *
 */

public class AESUtils {

	private static final String ALGORITHM = "AES";

	/**
	 * 加密。
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] encrypt(byte[] content, byte[] password) {

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
			keyGenerator.init(128, new SecureRandom(password));
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] encoded = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(encoded, ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			return cipher.doFinal(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加密。
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] encrypt(byte[] content, String password) {

		return encrypt(content, password.getBytes());
	}

	/**
	 * 加密。
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static String encrypt(String content, String password) {

		try {
			byte[] contents = content.getBytes();
			byte[] results = encrypt(contents, password);
			return parseByteToHexString(results);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密。
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] decrypt(byte[] content, byte[] password) {

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
			keyGenerator.init(128, new SecureRandom(password));
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] encoded = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(encoded, ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			return cipher.doFinal(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密。
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) {

		return decrypt(content, password.getBytes());
	}

	/**
	 * 解密。
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static String decrypt(String content, String password) {

		byte[] contents = parseHexStringToByte(content);
		byte[] results = decrypt(contents, password);
		return new String(results);
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

}
