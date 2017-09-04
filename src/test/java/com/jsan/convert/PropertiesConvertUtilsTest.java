package com.jsan.convert;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jsan.convert.annotation.ConvertServiceRegister;
import com.jsan.convert.annotation.DateTimePattern;

public class PropertiesConvertUtilsTest {

	@Test
	public void foo() throws FileNotFoundException {

//		PropBean propBean = PropertiesConvertUtils.getObject("/com/jsan/convert", PropBean.class);
//		PropBean propBean = PropertiesConvertUtils.getObject(PropBean.class);

		PropBean propBean = PropertiesConvertUtils.getObject("/propbeantest.properties", PropBean.class);

		System.out.println(propBean);

		if (propBean.getList1() != null) {
			for (Object object : propBean.getList1()) {
				System.out.println(object.getClass().getSimpleName() + " - " + object);
			}
		}
		
		// ==================================================
		
//		Map<Object, Object> map = PropertiesConvertUtils.loadProperties("/propbeantest.properties");
//
//		for (Map.Entry<Object, Object> entry : map.entrySet()) {
//			System.out.println(entry.getKey() + " - " + entry.getValue().getClass() + " - " + entry.getValue());
//		}
		
		// ==================================================		

//		Properties pro = new Properties();
//		pro.setProperty("one", "111");
//		pro.setProperty("two", "222");
//
//		PropertiesConvertUtils.storeProperties(pro, "/propbeanbar.properties", true); // 输出目录在 jsan/target/test-classes
		
		// ==================================================

//		PropBean obj = new PropBean();
//
//		obj.setId(123);
//		obj.setSex(true);
//		obj.setName("this is a test. 这是一个测试。");
//		obj.setMoney(123.123456f);
//
//		List<Date> list = new ArrayList<Date>();
//		list.add(new Date());
//		list.add(new Date(23434));
//		list.add(new Date(57678678));
//
//		obj.setList(list);
//
//		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
//		map.put("one", 1);
//		map.put("two", 2);
//		map.put("three", 4);
//
//		obj.setMap(map);
//
//		PropertiesConvertUtils.setObject(obj); // 输出目录在 jsan/target/test-classes
//
//		System.out.println(map);

	}

	@Test
	public void bar() {

		User user = PropertiesConvertUtils.getObjectEnhanced("/propbeantest.properties", User.class, "user.");
		System.out.println(user);
//		user.setSex(true);
//		PropertiesConvertUtils.setObjectEnhanced(user, "/propbeantest.properties", "user."); // 输出目录在 jsan/target/test-classes
//
		User user0 = PropertiesConvertUtils.getObjectEnhanced("/propbeantest.properties", User.class, "person.user.");
		System.out.println(user0);

	}

	public static class User {

		Integer id;
		String name;
		Integer age;
		Boolean sex;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public Boolean getSex() {
			return sex;
		}

		public void setSex(Boolean sex) {
			this.sex = sex;
		}

		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + ", age=" + age + ", sex=" + sex + "]";
		}

	}
}

class PropBean {

	int id;
	Boolean sex;
	float money;
	String name;
	Double[] doubles;

	List<Date> list;
	List<Object> list1;
	Map<String, Integer> map;
	Map<String, Character> map1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double[] getDoubles() {
		return doubles;
	}

	public void setDoubles(Double[] doubles) {
		this.doubles = doubles;
	}

	public List<Date> getList() {
		return list;
	}

	@DateTimePattern("yyyy/MM/dd")
	public void setList(List<Date> list) {
		this.list = list;
	}

	public Map<String, Integer> getMap() {
		return map;
	}

	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}

	public List<Object> getList1() {
		return list1;
	}

	@ConvertServiceRegister(JsonConvertService.class)
	public void setList1(List<Object> list1) {
		this.list1 = list1;
	}

	public Map<String, Character> getMap1() {
		return map1;
	}

	@ConvertServiceRegister(JsonConvertService.class)
	public void setMap1(Map<String, Character> map1) {
		this.map1 = map1;
	}

	@Override
	public String toString() {
		return "PropBean [id=" + id + ", sex=" + sex + ", money=" + money + ", name=" + name + ", doubles="
				+ Arrays.toString(doubles) + ", list=" + list + ", list1=" + list1 + ", map=" + map + ", map1=" + map1
				+ "]";
	}

}
