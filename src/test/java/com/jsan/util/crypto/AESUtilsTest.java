package com.jsan.util.crypto;

import junit.framework.TestCase;

public class AESUtilsTest extends TestCase {

	public void testFoo() {

		String content = "我们都是好朋友";
		String password = "abcdefg123456";

		long start = System.nanoTime();

		String ciphertext = AESUtils.encrypt(content, password);

		System.out.println("---加密---");
		System.out.println("密文=" + ciphertext);
		System.out.println("耗时=" + (System.nanoTime() - start));

		start = System.nanoTime();

		String text = AESUtils.decrypt(ciphertext, password);

		System.out.println("---解密---");
		System.out.println("原文=" + text);
		System.out.println("耗时=" + (System.nanoTime() - start));

	}

}
