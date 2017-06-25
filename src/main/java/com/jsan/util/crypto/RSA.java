package com.jsan.util.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;

/**
 * RSA 加密类（明文长度不可超过 117 字节）。
 * <p>
 * <strong>JavaEE 默认算法是 RSA/None/PKCS1Padding，不同 JDK 默认的补位方式可能不同，例如 Android 默认是
 * RSA/None/NoPadding。</strong>
 * <p>
 * 更专业的可参 Bouncy Castle。
 *
 */

public class RSA {

	private static final String KEY_ALGORITHM = "RSA";

	private static final int KEY_SIZE = 1024;

	private Cipher cipher;

	private KeyPair keyPair;

	private RSAPublicKey publicKey;

	private RSAPrivateKey privateKey;

	/**
	 * JDK 默认服务提供者、JDK 默认算法、密钥长度 1024。
	 * 
	 */
	public RSA() {

		this(null);
	}

	/**
	 * 指定服务提供者（JDK 默认算法、密钥长度 1024）
	 * 
	 * @param provider
	 */
	public RSA(Provider provider) {

		this(provider, KEY_ALGORITHM);
	}

	/**
	 * 指定服务提供者、算法（密钥长度 1024）。
	 * 
	 * @param provider
	 * @param algorithm
	 */
	public RSA(Provider provider, String algorithm) {

		this(provider, algorithm, KEY_SIZE);
	}

	/**
	 * 指定服务提供者、算法、密钥长度。
	 * 
	 * @param provider
	 * @param algorithm
	 * @param size
	 */
	public RSA(Provider provider, String algorithm, int size) {

		try {
			KeyPairGenerator keyPairGenerator = null;
			if (provider == null) {
				keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
				cipher = Cipher.getInstance(algorithm);
			} else {
				keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
				cipher = Cipher.getInstance(algorithm, provider);
			}
			keyPairGenerator.initialize(size);
			keyPair = keyPairGenerator.generateKeyPair();
			publicKey = (RSAPublicKey) keyPair.getPublic();
			privateKey = (RSAPrivateKey) keyPair.getPrivate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 返回 KeyPair 密钥对。
	 * 
	 * @return
	 */
	public KeyPair getKeyPair() {

		return keyPair;
	}

	/**
	 * 返回公钥。
	 * 
	 * @return
	 */
	public RSAPublicKey getPublicKey() {

		return publicKey;
	}

	/**
	 * 返回私钥。
	 * 
	 * @return
	 */
	public RSAPrivateKey getPrivateKey() {

		return privateKey;
	}

	/**
	 * 公钥加密。
	 * 
	 * @param content
	 * @return
	 */
	public byte[] encryptByPublicKey(byte[] content) {

		try {
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 私钥加密。
	 * 
	 * @param content
	 * @return
	 */
	public byte[] encryptByPrivateKey(byte[] content) {

		try {
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return cipher.doFinal(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 公钥解密。
	 * 
	 * @param content
	 * @return
	 */
	public byte[] decryptByPublicKey(byte[] content) {

		try {
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return cipher.doFinal(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 私钥解密。
	 * 
	 * @param content
	 * @return
	 */
	public byte[] decryptByPrivateKey(byte[] content) {

		try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
