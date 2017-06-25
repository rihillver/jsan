package com.jsan.util.crypto;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;

import junit.framework.TestCase;

public class RSAUtilsTest extends TestCase {

	public void testBar() {

		long start = System.nanoTime();

		String text = "Bouncy Castle 是一种用于 Java 平台的开放源码的轻量级密码术包。";

		System.out.println("明文：" + text);
		System.out.println("明文大小：" + text.getBytes().length);

		KeyPair keyPair = RSAUtils.generateKeyPair();

		byte[] content = text.getBytes();

		byte[] encryptContent = RSAUtils.encryptByPublicKey(keyPair.getPublic(), content);

		System.out.println("加密耗时：" + (System.nanoTime() - start));

		start = System.nanoTime();

		// System.out.println("加密：" + new String(encryptContent));
		System.out.println("加密：" + RSAUtils.parseByteToHexString(encryptContent));

		System.out.println("密文大小：" + encryptContent.length);

		// 通过私钥的模和指数生成私钥
		BigInteger modulus = RSAUtils.getPrivateKey(keyPair).getModulus();
		BigInteger exponent = RSAUtils.getPrivateKey(keyPair).getPrivateExponent();

		RSAPrivateKey privateKey = RSAUtils.getPrivateKey(modulus, exponent);

		byte[] result = RSAUtils.decryptByPrivateKey(privateKey, encryptContent);

		// byte[] result = decryptByPrivateKey(keyPair.getPrivate(), encryptContent);

		System.out.println("解密：" + new String(result));

		System.out.println("解密耗时：" + (System.nanoTime() - start));

		byte[] pk = RSAUtils.getPublicKey(keyPair).getEncoded();

		System.out.println("公钥(Base64)：" + Base64Utils.encode(pk));
		System.out.println("公钥(十六进制)：" + RSAUtils.parseByteToHexString(pk));

		System.out.println("公钥模：" + RSAUtils.getPublicKey(keyPair).getModulus().toString(16));
		System.out.println("公钥指数：" + RSAUtils.getPublicKey(keyPair).getPublicExponent().toString(16));

	}

}
