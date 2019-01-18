package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		Abc[] list = {
				new Abc("aa", 11),
				new Abc("bb", 22),
		new Abc("cc", 33),
		new Abc("dd", 44)
		};


		Map<String, Object> map = new HashMap<>();

		map.put("name", "jiangshan");
		map.put("age", 30);
		map.put("list", list);

		Abc abc = BeanConvertUtils.getBean(Abc.class, map);

		System.out.println(abc);
		System.out.println(abc.getList().length);

	}

	private static class Abc {

		String name;
		int age;
		Abc[] list;

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

		public Abc[] getList() {
			return list;
		}

		public void setList(Abc[] list) {
			this.list = list;
		}

		@Override
		public String toString() {
			return "Abc [name=" + name + ", age=" + age + ", list=" + Arrays.toString(list) + "]";
		}

		

	}

}
