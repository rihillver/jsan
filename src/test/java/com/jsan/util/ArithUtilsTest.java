package com.jsan.util;

import junit.framework.TestCase;

public class ArithUtilsTest extends TestCase {

	public void testFoo() {

		double a = 123.1454;
		double b = 2.36;

		System.out.println(a * b);
		System.out.println(ArithUtils.multiply(a, b));
		System.out.println(ArithUtils.divide(10.0, 3.0));

	}
}
