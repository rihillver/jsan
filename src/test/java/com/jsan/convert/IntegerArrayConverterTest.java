package com.jsan.convert;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class IntegerArrayConverterTest {

	public void foo(List<List<List<Integer>>> list) {

	}

	@Test
	@SuppressWarnings("unchecked")
	public void bar() throws NoSuchMethodException, SecurityException {

		String[][][] strs = { { { "1.23", null, "2.34", "3.45", "234" }, { "111", "3333", "23445", null, "5.46" } },
				{ { "1.23", "2.34", "3.45", "234" }, { "111", "3333", "23445", null, "5.46" } }, { { "111", "222" } } };

		// Character[] c = {'y',null, 'b','c',null};

		ConvertService convertService = new GeneralConvertService();

		// convertService.register(new MyIntegerArrayConverter());

		Converter converter = convertService.lookupConverter(List.class);

		// System.out.println(converter);

		Method method = IntegerArrayConverterTest.class.getDeclaredMethod("foo", List.class);

		Type type = method.getGenericParameterTypes()[0];

		List<List<List<Integer>>> is = (List<List<List<Integer>>>) converter.convert(strs, type);

		System.out.println(is.getClass());

		Iterator<?> iterator = is.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());

		}

		for (List<List<Integer>> l1 : is) {
			System.out.println("----------");
			if (l1 != null) {
				for (List<Integer> l2 : l1) {
					System.out.println("==========");
					if (l2 != null) {
						for (Integer i : l2) {
							System.out.println(i);
						}
					}
				}
			}
		}

	}
}
