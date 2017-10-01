package com.jsan.convert;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class MapConverterTest {

	public void foo(Map<Character, Map<Integer, List<Double>>> map) {

	}

	public void bar(Map<Double, List<? extends Number>> map) {

	}

	public void baz(Map<Double, List<String>[]> map) {

	}

	public void qux(Map<Double, List<? extends Number>[]> map) {

	}

	@SuppressWarnings("unchecked")
	@Test
	public void test1() throws NoSuchMethodException, SecurityException {

		Method method = MapConverterTest.class.getDeclaredMethod("baz", Map.class);

		Type type = method.getGenericParameterTypes()[0];

		ConvertService convertService = new GeneralConvertService();

		Converter converter = convertService.lookupConverter(Map.class);

		List<Integer> list0 = new ArrayList<Integer>();
		list0.add(111);
		list0.add(222);
		list0.add(333);
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(2);
		list1.add(3);

		List<Integer>[] lists = new ArrayList[2];
		lists[0] = list0;
		lists[1] = list1;

		Map<String, List<Integer>[]> m = new LinkedHashMap<String, List<Integer>[]>();

		m.put("111.11", lists);
		m.put("222.22", lists);
		m.put("333.33", lists);

		Map<Double, List<String>[]> map = (Map<Double, List<String>[]>) converter.convert(m, type);

		System.out.println(map);
		for (List<String>[] listss : map.values()) {
			for (List<String> l : listss) {
				System.out.println(l);
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Test
	public void test2() throws NoSuchMethodException, SecurityException {

		Method method = MapConverterTest.class.getDeclaredMethod("bar", Map.class);

		Type type = method.getGenericParameterTypes()[0];

		ConvertService convertService = new GeneralConvertService();

		Converter converter = convertService.lookupConverter(Map.class);

		Map<Integer, String[]> map = new HashMap<Integer, String[]>();
		map.put(11, new String[] { "11.11", "1.01", "1.001" });
		map.put(22, new String[] { "22.22", "2.02", "2.002" });
		map.put(33, new String[] { "33.33", "3.03", "3.003" });

		Map<Double, List<? extends Number>> map2 = (Map<Double, List<? extends Number>>) converter.convert(map, type);

		for (Map.Entry<Double, List<? extends Number>> entry : map2.entrySet()) {
			System.out.println(entry.getKey() + " ---> " + entry.getValue());
			for (Object object : entry.getValue()) {
				System.out.println(object.getClass() + " = " + object);
			}
		}

	}

	@Test
	@SuppressWarnings("unchecked")
	public void test3() throws NoSuchMethodException, SecurityException {

		Method method = MapConverterTest.class.getDeclaredMethod("foo", Map.class);

		Type type = method.getGenericParameterTypes()[0];

		ConvertService convertService = new GeneralConvertService();

		Converter converter = convertService.lookupConverter(Map.class);

		List<String> list0 = new ArrayList<String>();
		list0.add("111.11");
		list0.add("222.22");
		list0.add("333.33");

		List<String> list1 = new ArrayList<String>();
		list1.add("444.44");
		list1.add("555.55");
		list1.add("666.66");

		List<String> list2 = new ArrayList<String>();
		list2.add("777.77");
		list2.add("888.88");
		list2.add("999.99");

		// Map<Double, Character> m = new HashMap<Double, Character>();
		// m.put(123.01, 'a');
		// m.put(234.01, 'b');
		// m.put(345.01, 'c');
		// m.put(456.01, 'd');

		Map<Double, List<String>> m0 = new LinkedHashMap<Double, List<String>>();
		m0.put(123.01, list0);
		m0.put(234.01, list1);
		m0.put(345.01, list2);
		Map<Double, List<String>> m1 = new LinkedHashMap<Double, List<String>>();
		m1.put(111.01, list0);
		m1.put(222.01, list1);
		m1.put(333.01, list2);

		Map<String, Map<Double, List<String>>> mm = new LinkedHashMap<String, Map<Double, List<String>>>();
		mm.put("3", m0);
		mm.put("b", m1);

		Map<Character, Map<Integer, List<Double>>> map = (Map<Character, Map<Integer, List<Double>>>) converter
				.convert(mm, type);

		System.out.println(map);
		System.out.println();

		for (Map.Entry<Character, Map<Integer, List<Double>>> e : map.entrySet()) {
			System.out.println("---->" + e.getKey());
			for (Map.Entry<Integer, List<Double>> entry : e.getValue().entrySet()) {
				Integer key = entry.getKey();
				List<Double> value = entry.getValue();
				System.out.println(key + " - " + value);
			}
		}

	}
}
