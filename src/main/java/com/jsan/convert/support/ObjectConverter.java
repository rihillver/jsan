package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.jsan.convert.AbstractRecursiveableConverter;
import com.jsan.convert.BeanConvertUtils;

public class ObjectConverter extends AbstractRecursiveableConverter {

	@Override
	public Object convert(Object source, Type type) {

		// 如果source是Map的情况，则尝试将Map转换成Bean
		if (Map.class.isAssignableFrom(source.getClass())) {
			return BeanConvertUtils.getBean((Class<?>) type, (Map<?, ?>) source, getConvertService());
		}

		return source;
	}

	public static void main(String[] args) {

		//Cao[] list = { new Cao("aa", 11), new Cao("bb", 22), new Cao("cc", 33), new Cao("dd", 44) };
		List<Cao> list = new LinkedList<>();
		list.add(new Cao("aa", 11));
		list.add(new Cao("bb", 22));
		list.add(new Cao("cc", 333));
		
		Map<String, Cao> map = new LinkedHashMap<>();
		map.put("one", new Cao("one...", 10));
		map.put("two", new Cao("two...", 20));
		map.put("three", new Cao("three...", 30));
		map.put("four", new Cao("four...", 40));
		
//		Map<String, Object> map = new HashMap<>();
//
//		map.put("name", "jiangshan");
//		map.put("age", 30);
//		map.put("list", list);

		//Abc abc = BeanConvertUtils.getBean(Abc.class, map);

		Abc abc = new Abc("江山", 30);
		abc.setList(list);
		abc.setCao(new Cao("哈哈", 88));
		abc.setMap(map);
		
		System.out.println(abc);
		
		List<Map<String, Object>> lst = (List<Map<String, Object>>) BeanConvertUtils.getMap(abc, true, false).get("list");
		
		System.out.println(lst.getClass());
		System.out.println(BeanConvertUtils.getMap(abc, true, false));
		System.out.println(lst.get(0));
		System.out.println(lst.size());
		
		Map<Object, Object> m = (Map<Object, Object>) BeanConvertUtils.getMap(abc, true, false).get("map");
		System.out.println(m.get("one").getClass());
		System.out.println(m.get("one"));
		System.out.println(m.size());

	}

	private static class Abc {

		String name;
		int age;
		Cao cao;
		List<Cao> list;
		Map<String, Cao> map;

		public Abc() {

		}

		public Abc(String name, int age) {
			super();
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public Cao getCao() {
			return cao;
		}

		public void setCao(Cao cao) {
			this.cao = cao;
		}

		public List<Cao> getList() {
			return list;
		}

		public void setList(List<Cao> list) {
			this.list = list;
		}

		public Map<String, Cao> getMap() {
			return map;
		}

		public void setMap(Map<String, Cao> map) {
			this.map = map;
		}

		@Override
		public String toString() {
			return "Abc [name=" + name + ", age=" + age + ", cao=" + cao + ", list=" + list + ", map=" + map + "]";
		}



	

	}

	private static class Cao {

		public Cao() {

		}

		public Cao(String name, int age) {

			this.name = name;
			this.age = age;
		}

		String name;
		int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return "Cao [name=" + name + ", age=" + age + "]";
		}

	}

}
