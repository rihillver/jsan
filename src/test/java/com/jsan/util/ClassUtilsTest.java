package com.jsan.util;

import java.util.Set;

import org.junit.Test;

public class ClassUtilsTest {

    @Test
    public void testFoo() {
	
	Set<Class<?>> set = ScanClassUtils.getClasses("org.slf4j", false);

	for (Class<?> clss : set) {
	    System.out.println(clss);
	}
    }
}
