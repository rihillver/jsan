package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jsan.mvc.json.JsonParserConfigurator;

/**
 * 控制器上映射方法形参上的表单字段 json 转换成（Bean）对象的注解。
 * 
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonConvert {

	Class<? extends JsonParserConfigurator> value() default JsonParserConfigurator.class;
	
	/**
	 * 待实现。。。。
	 * 是否处理客户端直接以json对象进行深度序列化后的形式提交请求参数的情况进行有效的特殊转换。
	 * <p>
	 * 注：这里指的不是json字符串，而是原生json对象，例如客户端通过jQuery以ajax的方式直接以json对象作为data提交时，其默认将对json对象进行深度序列化并转换成比较特殊的方式表示键值对，如items[]=[1,2,3]、obj[a]=xxx等，因为jQuery需要调用jQuery.param序列化参数，jQuery.param(obj, traditional)默认情况下traditional为false，即jquery会深度序列化参数对象，以适应如PHP和Ruby on Rails框架，但servelt无法处理，当然了，客户端可以通过设置traditional为true阻止深度序列化。
	 * 
	 * @return
	 */
	boolean deep() default false;
	
	/*
	public Map<String, Object> abc(Map<String, String[]> parameterMap) {

		Map<String, Object> map = new LinkedHashMap<>();

		boolean flag = true;

		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

			String key = entry.getKey();
			String[] value = entry.getValue();
			if (key.endsWith("[]")) {
				map.put(key.substring(0, key.length() - 2), Arrays.asList(value));
			} else {
				map.put(key, value[0]);
				if (key.indexOf('[') != -1 && key.indexOf(']') != -1) {
					flag = false;
				}
			}
		}

		if (flag) {
			return map;
		}

		return handle(map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> handle(Map<String, Object> map) {

		boolean flag = true;

		Map<String, Object> tempMap = new LinkedHashMap<>();

		for (Map.Entry<String, Object> entry : map.entrySet()) {

			String key = entry.getKey();
			Object value = entry.getValue();

			int leftIndex = key.lastIndexOf('[');
			int rightIndex = key.lastIndexOf(']');

			if (leftIndex != -1 && rightIndex != -1) {
				String name = key.substring(0, leftIndex);
				String itemName = key.substring(leftIndex + 1, rightIndex);

				Object tempObject = tempMap.get(name);
				if (tempObject == null) {
					Map<String, Object> itemMap = new LinkedHashMap<>();
					itemMap.put(itemName, value);

					tempMap.put(name, itemMap);
				} else {
					Map<String, Object> itemMap = (Map<String, Object>) tempObject;
					itemMap.put(itemName, value);

					tempMap.put(name, itemMap);
				}

				flag = false;
			} else {
				tempMap.put(key, value);
			}

		}

		if (flag) {
			return tempMap;
		}

		return handle(tempMap);
	}
	*/
	
	
	/*
	 public static void main(String[] args) {
		
		Map<String, Object> map1 = new HashMap<>();
		
		map1.put("name", "jiangshan");
		map1.put("age", 20);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("name", "jiangshan");
		map.put("age", 20);
		map.put("abc", map1);
		
		Abc abc = BeanConvertUtils.getObject(Abc.class, map);
		
		System.out.println(abc);
		
	}
	
	class Abc{
	
		String name;
		int age;
		Abc abc;
		
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
		public Abc getAbc() {
			return abc;
		}
		public void setAbc(Abc abc) {
			this.abc = abc;
		}
		@Override
		public String toString() {
			return "Abc [name=" + name + ", age=" + age + ", abc=" + abc + "]";
		}
		
		
	}
	 */
}
