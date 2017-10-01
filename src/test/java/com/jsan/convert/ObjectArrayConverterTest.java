package com.jsan.convert;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Test;

import com.jsan.convert.support.ObjectArrayConverter;

public class ObjectArrayConverterTest {

	@SuppressWarnings("unchecked")
	public List<Integer>[] foo(List<Integer>[] list) { // 范型数组

		ArrayList<Integer>[] lists = (ArrayList<Integer>[]) new ArrayList[2];
		System.out.println(lists);
		return lists;
	}

	public void bar(List<? extends Number>[] list) {

	}

	public void baz(List<? super Number>[] list) {

	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBaz() throws NoSuchMethodException, SecurityException {

		Method method = ObjectArrayConverterTest.class.getDeclaredMethod("baz", List[].class);
		Type type = method.getGenericParameterTypes()[0];

		ConvertService convertService = new GeneralConvertService();
		Converter converter = convertService.lookupConverter(List[].class);

		String[] strs = { "1.11", "2.22", "3.33", "4.44", "5.55" };

		List<? super Number>[] list = (List<? super Number>[]) converter.convert(strs, type);

		System.out.println(list.getClass());
		for (List<? super Number> object : list) {
			System.out.println(object.get(0) + " - " + object.get(0).getClass());
		}

	}

	@Test
	@SuppressWarnings("unchecked")
	public void testFoo() throws NoSuchMethodException, SecurityException {

		Method method = ObjectArrayConverterTest.class.getDeclaredMethod("foo", List[].class);

		Type type = method.getGenericParameterTypes()[0];

		String[][] strings = { { "34", "55" }, { "a-1", "0", "123", "456", "789" }, { "123", "456", "789" } };

		String[] strs = { "111", "222", "333" };
		String[] strs1 = { "444", "555", "666" };
		String[] strs2 = { "777", "888", "999" };

		// double[] ds = { 12.1221, 11, 485.65, 88.9165216, 12.000036 };

		Collection<String[]> collection = new LinkedHashSet<String[]>();
		collection.add(strs);
		collection.add(strs1);
		collection.add(strs2);
		collection.add(strs1);

		ConvertService convertService = new GeneralConvertService();
		convertService.registerConverter(new NewTestArrayConverter());

		Converter converter = convertService.lookupConverter(Integer[].class);

		// List<Integer>[] lists = (List<Integer>[])
		// converter.convert("this,is,a,test,ok", type);
		// List<Integer>[] lists = (List<Integer>[])
		// converter.convert(collection, type);

		List<Integer>[] lists = (List<Integer>[]) converter.convert(strings, type); // 二维数组也行

		System.out.println();

		for (List<Integer> integer : lists) {
			// System.out.println(integer);
			System.out.println("----------");
			for (Integer i : integer) {
				System.out.println(i);
			}
		}

	}
}

class NewTestArrayConverter extends ObjectArrayConverter {

	public Object[] convert(Object source, Type type) {

		if (source instanceof String) {
			String string = source.toString();
			source = string.split(",");
		}
		return super.convert(source, type);
	}

}
