package com.jsan.util;

import java.util.Set;

import org.junit.Test;

import com.jsan.convert.Converter;
import com.jsan.dao.AbstractSqlx;
import com.jsan.dao.AnsiSql;
import com.jsan.dao.Page;
import com.jsan.dao.Sqlx;

public class ClassUtilsTest {

	@Test
	public void testFoo() {

		//Set<Class<?>> set = ClassUtils.getClassSet("org.slf4j", false);
		//Set<Class<?>> set = ClassUtils.getChildClassSet("com.jsan.dao", Sqlx.class, false);
		//Set<Class<?>> set = ClassUtils.getChildClassSet("com.jsan.dao", AbstractSqlx.class, false);
		//Set<Class<?>> set = ClassUtils.getClassSetStartsWith("com.jsan.dao", "M");
		//Set<Class<?>> set = ClassUtils.getClassSetEndsWith("com.jsan", "ver");
		Set<Class<?>> set = ClassUtils.getClassesContains("com.jsan.convert", "Array", false);

		for (Class<?> clss : set) {
			System.out.println(clss);
		}
	}
}
