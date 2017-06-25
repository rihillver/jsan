package com.jsan.util;

import junit.framework.TestCase;

public class RandomUtilsTest extends TestCase {

	public void testFoo(){
		
		System.out.println(RandomUtils.getAllChinese(15));
		System.out.println(RandomUtils.getChinese(15));
		
		System.out.println(RandomUtils.getSequenceCode(20));
		System.out.println(RandomUtils.getSequenceCode(20));
		
		System.out.println(RandomUtils.getWordChar(12));
	}
}
