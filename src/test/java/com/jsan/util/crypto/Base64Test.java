package com.jsan.util.crypto;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

public class Base64Test extends TestCase {

	String source = "我们都有一个家名字叫中国，test。。。";
	String str = "BASE64 编码是一种常用的字符编码，在很多地方都会用到。但base64不是安全领域下的加密解密算法。能起到安全作用的效果很差，而且很容易破解，他核心作用应该是传输数据的正确性，有些网关或系统只能使用ASCII字符。Base64就是用来将非ASCII字符的数据转换成ASCII字符的一种方法，而且base64特别适合在http，mime协议下快速传输数据。.";

	String str1 = null;
	String str2 = null;
	String str3 = null;
	String str4 = null;

	public void testFoo() throws UnsupportedEncodingException {

		str1 = Base64Utils.encode(source);
		System.out.println(str1);

		str2 = Base64Utils.encodeUrlSafe(source);
		System.out.println(str2);

		str3 = Base64Utils.encode(source.getBytes("gbk"));
		System.out.println(str3);

		str4 = Base64Utils.encodeUrlSafe(source.getBytes("gbk"));
		System.out.println(str4);

		System.out.println();

		bar();
	}

	public void bar() throws UnsupportedEncodingException {

		System.out.println(Base64Utils.decodeToString(str1));
		System.out.println(Base64Utils.decodeUrlSafeToString(str2));
		System.out.println(new String(Base64Utils.decodeToByte(str3), "gbk"));
		System.out.println(new String(Base64Utils.decodeUrlSafeToByte(str4), "gbk"));
	}

	public void testBaz() {

		String encodeStr1 = Base64Utils.encode(str.getBytes(), Base64.DO_BREAK_LINES); // 满76个字符换行

		String encodeStr2 = Base64Utils.encode(str.getBytes(), Base64.DO_BREAK_LINES | Base64.URL_SAFE); // 满76个字符换行，且URL模式

		System.out.println(encodeStr1);
		System.out.println();
		System.out.println(encodeStr2);
		System.out.println();
	}

	/**
	 * org.apache.commons.codec.binary.Base64 的 URL 模式默认是去除等号的（如果尾部有的等号的情况）。
	 * <p>
	 * net.iharder.Base64 、java 8 的 java.util.Base64 的 URL 模式默认是保留等号的。
	 * 
	 */
	public void testQux() {

		System.out.println();
		String encodeStr = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(str.getBytes());
		System.out.println(encodeStr);
		System.out.println(new String(org.apache.commons.codec.binary.Base64.decodeBase64(encodeStr)));
		System.out.println();

	}
}
