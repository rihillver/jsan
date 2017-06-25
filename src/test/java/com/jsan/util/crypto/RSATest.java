package com.jsan.util.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.jsan.util.ParseUtils;

import junit.framework.TestCase;

public class RSATest extends TestCase {

	public void testBar() {

		long start = System.nanoTime();

		RSA rsa = new RSA();

		System.out.println("公钥：" + rsa.getPublicKey());
		System.out.println("私钥模：" + rsa.getPrivateKey().getModulus());
		System.out.println("私钥指数：" + rsa.getPrivateKey().getPrivateExponent());
		System.out.println("私钥模长度（bit）：" + rsa.getPrivateKey().getModulus().bitLength());

		String str = "我们都有一个中国梦，Dream...";

		System.out.println("明文：" + str);
		System.out.println("明文长度：" + str.getBytes().length);

		byte[] content = rsa.encryptByPublicKey(str.getBytes());

		System.out.println("密文：" + new String(content));
		System.out.println("密文(十六进制)：" + ParseUtils.parseByteToHexString(content));
		System.out.println("密文(Base64)：" + Base64Utils.encode(content));

		content = rsa.decryptByPrivateKey(content);

		System.out.println("解密：" + new String(content));

		System.out.println("耗时：" + (System.nanoTime() - start));
	}
	
	public void testBaz() {

		long start = System.nanoTime();

		RSA rsa = new RSA(new BouncyCastleProvider());
		 
		// RSA rsa = new RSA(new BouncyCastleProvider(), "RSA/ECB/PKCS1Padding");
		 
		// RSA rsa = new RSA(new BouncyCastleProvider(), "RSA/ECB/NoPadding");
		 
		// 这种补位方式方式下明文位长度 <= 模长(bit)-11 ，即 <= 73.625 字节((600-11)/8)
		// RSA rsa = new RSA(new BouncyCastleProvider(), "RSA/ECB/NoPadding", 600); 

		// 这种补位方式方式下明文字节长度 <= 模长(byte)-11 ，即 <= 64 字节(600/8-11)
		// RSA rsa = new RSA(new BouncyCastleProvider(), "RSA/ECB/PKCS1Padding", 600); 
		
		System.out.println("公钥：" + rsa.getPublicKey());
		System.out.println("私钥模：" + rsa.getPrivateKey().getModulus());
		System.out.println("私钥指数：" + rsa.getPrivateKey().getPrivateExponent());
		System.out.println("私钥模长度（bit）：" + rsa.getPrivateKey().getModulus().bitLength());

		String str = "我们都有一个中国梦，Dream...";

		System.out.println("明文：" + str);
		System.out.println("明文长度：" + str.getBytes().length);

		byte[] content = rsa.encryptByPublicKey(str.getBytes());

		System.out.println("密文：" + new String(content));
		System.out.println("密文(十六进制)：" + ParseUtils.parseByteToHexString(content));
		System.out.println("密文(Base64)：" + Base64Utils.encode(content));

		content = rsa.decryptByPrivateKey(content);

		System.out.println("解密：" + new String(content));

		System.out.println("耗时：" + (System.nanoTime() - start));
	}
}
