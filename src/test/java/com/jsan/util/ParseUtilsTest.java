package com.jsan.util;

import junit.framework.TestCase;

public class ParseUtilsTest extends TestCase {

	public void testFoo() {

		// 构建大于16位的unicode码点
		int codePiont = Integer.parseInt("20073", 16);
		System.out.println(codePiont);

		String s = new String(new int[] { codePiont }, 0, 1);
		System.out.println(s);

		// 转换成unicode字符串
		String unicodeStr = ParseUtils.parseStringToUnicode(s);
		System.out.println(unicodeStr);

		// 转换成字符串
		String str = ParseUtils.parseUnicodeToString(unicodeStr);
		System.out.println(str);
		System.out.println(Character.toString(str.charAt(0)));
		System.out.println(Integer.toHexString(str.charAt(0)));
		System.out.println(Character.toString(str.charAt(1)));
		System.out.println(Integer.toHexString(str.charAt(1)));

		// 转换成unicode码点
		System.out.println(str.codePointAt(0));
	}

}
