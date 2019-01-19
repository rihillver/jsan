package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jsan.mvc.json.JsonParserConfigurator;

/**
 * 控制器上映射方法形参上的 Request Payload 数据（通常为json字符串形式）转换的注解。
 * <p>
 * 当客户端请求的 Content-Type 为 text/plain、application/json 等情况时，请求表单参数则在 Request Payload 中，此时服务端需要通过 request.getReader() 或 request.getInputStream() 来获取数据， 通过该注解可以将这些表单数据转换到对应的形参上。
 * <p>
 * 注：<br>
 * 1、基于 Fastjson 进行 json 的转换。<br>
 * 2、形参类型为 String、JSONObject、JSONArray 时将直接以对应的对象返回，不会做更多的无畏转换。
 * 
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonConvert {

	Class<? extends JsonParserConfigurator> value() default JsonParserConfigurator.class;
	
	/**
	 * 声明是否使用动态代理的方式，仅对于 Bean 的转换类型才有效，对于数组、Collectio、Map 的转换类型无效。
	 * 
	 * @return
	 */
	boolean proxy() default false;
	
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
