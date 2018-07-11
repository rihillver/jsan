package com.jsan.util;

import java.util.Set;

import org.junit.Test;

import com.jsan.dao.Sqlx;

public class ClassUtilsTest {

	@Test
	public void testFoo() {

		Set<Class<?>> set = ClassUtils.getClasses("org.slf4j", false);

		for (Class<?> clss : set) {
			System.out.println(clss.getName());
		}
	}

	@Test
	public void testBar() {

		Set<Class<?>> set = ClassUtils.getChildClasses("com.jsan.dao", Sqlx.class, true);

		for (Class<?> clss : set) {
			System.out.println(clss.getName());
		}
	}

	@Test
	public void testBaz() {

		Set<Class<?>> set = ClassUtils.getClassesStartsWith("com.jsan.dao", "Bean");

		for (Class<?> clss : set) {
			System.out.println(clss.getName());
		}
	}

	@Test
	public void testQux() {

		Set<Class<?>> set = ClassUtils.getClassesContains("com.jsan.convert", "Array", false);

		for (Class<?> clss : set) {
			System.out.println(clss.getName());
		}
	}
}
