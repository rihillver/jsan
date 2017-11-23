package com.jsan.convert;

import java.lang.reflect.Method;
import java.util.Date;

import org.junit.Test;

public class BeanProxyUtilsTest {

	@Test
	public void testBar() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Class<?> clazz = Class.forName("com.jsan.convert.BeanProxyUtilsTest$User");

		User user = (User) clazz.newInstance();

		user.setId(1);
		user.setName("jiang");
		user.setSex(true);

		System.out.println(user);
	}

	@Test
	public void testFoo() {

		User user = BeanProxyUtils.getObject(User.class);
		user.setId(123);
		user.setBirth(new Date());
		user.setName("shan");

		System.out.println(user);
	}

	@Test
	public void TestBaz() throws NoSuchMethodException, SecurityException {

		User user = new User();
		user.setId(123);
		user.setName("Jack");
		user.setBirth(new Date());

		System.out.println(user);

		User user2 = BeanProxyUtils.getDaoBean(user);

		long start = System.nanoTime();
		User user3 = BeanProxyUtils.getDaoBean(user);
		User user4 = BeanProxyUtils.getDaoBean(user);
		long end = System.nanoTime() - start;
		System.out.println(end);

		System.out.println(user2);
		System.out.println(user3);
		System.out.println(user4);

		user2.setName("shan");
		user2.setAge(30);

		System.out.println(user2);
		System.out.println(BeanProxyUtils.getDaoBeanExcludeFieldSet(user2));

		// printMethods(user2);

	}

	private void printMethods(Object obj) {

		for (Method method : obj.getClass().getMethods()) {
			System.out.println("----" + method);
		}
	}

	public static class User {

		int id;
		String name;
		boolean sex;
		Date birth;
		
		//int age;

		public int getAge() {
			return 20;
		}

		public void setAge(int age) {
			//this.age = age;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isSex() {
			return sex;
		}

		public void setSex(boolean sex) {
			this.sex = sex;
		}

		public Date getBirth() {
			return birth;
		}

		public void setBirth(Date birth) {
			this.birth = birth;
		}

		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + ", sex=" + sex + ", birth=" + birth + "]";
		}

	}

}
