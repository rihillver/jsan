package com.jsan.util.crypto;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

//import java.security.Provider;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * RSA 加密工具类。
 * <p>
 * 更专业的可参 Bouncy Castle。
 *
 */

public class RSAUtils {

	// private static final Provider BOUNCY_CASTLE_PROVIDER = new BouncyCastleProvider();

	private static final String KEY_ALGORITHM = "RSA";

	private static final int KEY_SIZE = 1024;

	private static final int MAX_ENCRYPT_BLOCK = 117;

	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 生成 KeyPair 密钥对。
	 * 
	 * @return
	 */
	public static KeyPair generateKeyPair() {

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			// KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
			
			keyPairGenerator.initialize(KEY_SIZE);
			return keyPairGenerator.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过 KeyPair 返回 RSA 公钥。
	 * 
	 * @param keyPair
	 * @return
	 */
	public static RSAPublicKey getPublicKey(KeyPair keyPair) {

		return (RSAPublicKey) keyPair.getPublic();
	}

	/**
	 * 通过 KeyPair 返回 RSA 私钥。
	 * 
	 * @param keyPair
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey(KeyPair keyPair) {

		return (RSAPrivateKey) keyPair.getPrivate();
	}

	/**
	 * 通过公钥的模和指数返回 RSA 公钥。
	 * 
	 * @param modulus
	 * @param exponent
	 * @return
	 */
	public static RSAPublicKey getPublicKey(BigInteger modulus, BigInteger exponent) {

		try {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过私钥的模和指数返回 RSA 私钥。
	 * 
	 * @param modulus
	 * @param exponent
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey(BigInteger modulus, BigInteger exponent) {

		try {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 公钥加密。
	 * 
	 * @param publicKey
	 * @param content
	 * @return
	 */
	public static byte[] encryptByPublicKey(PublicKey publicKey, byte[] content) {

		try {
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			// Cipher cipher = Cipher.getInstance(KEY_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// content = cipher.doFinal(content);

			return getContentForHandleBlock(cipher, content, MAX_ENCRYPT_BLOCK);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 私钥加密。
	 * 
	 * @param privateKey
	 * @param content
	 * @return
	 */
	public static byte[] encryptByPrivateKey(PrivateKey privateKey, byte[] content) {

		try {
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			// Cipher cipher = Cipher.getInstance(KEY_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			// content = cipher.doFinal(content);

			return getContentForHandleBlock(cipher, content, MAX_ENCRYPT_BLOCK);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 公钥解密。
	 * 
	 * @param publicKey
	 * @param content
	 * @return
	 */
	public static byte[] decryptByPublicKey(PublicKey publicKey, byte[] content) {

		try {
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			// Cipher cipher = Cipher.getInstance(KEY_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
			// Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BOUNCY_CASTLE_PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			// content = cipher.doFinal(content);

			return getContentForHandleBlock(cipher, content, MAX_DECRYPT_BLOCK);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 私钥解密。
	 * 
	 * @param privateKey
	 * @param content
	 * @return
	 */
	public static byte[] decryptByPrivateKey(PrivateKey privateKey, byte[] content) {

		try {
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			// Cipher cipher = Cipher.getInstance(KEY_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
			// Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BOUNCY_CASTLE_PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// content = cipher.doFinal(content);

			return getContentForHandleBlock(cipher, content, MAX_DECRYPT_BLOCK);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 分段处理。
	 * 
	 * @param cipher
	 * @param content
	 * @param maxBlock
	 * @return
	 * @throws Exception
	 */
	private static byte[] getContentForHandleBlock(Cipher cipher, byte[] content, int maxBlock) throws Exception {

		int length = content.length;
		int offset = 0;
		byte[] buffer = null;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int i = 0;
		while (length - offset > 0) {
			if (length - offset > maxBlock) {
				buffer = cipher.doFinal(content, offset, maxBlock);
			} else {
				buffer = cipher.doFinal(content, offset, length - offset);
			}
			byteArrayOutputStream.write(buffer, 0, buffer.length);
			i++;
			offset = i * maxBlock;
		}
		content = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();

		return content;
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

}
