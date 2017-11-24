package com.jsan.convert;

import java.lang.reflect.Method;
import java.util.Date;

import org.junit.Test;

public class BeanProxyUtilsTest {

	@Test
	public void testBar() throws Exception {

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
	public void TestBaz() throws Exception {

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

		user2.setId(1);
		user2.setName("shan");
		user2.setAge(30);

		System.out.println(user2);
		System.out.println(BeanProxyUtils.getDaoBeanExcludeFieldSet(user2));

	}

	/**
	 * 父类的 Method 可以调用子类的对象，子类的 Method
	 * 也可以调用父类的对象（前提是子类未覆盖父类的该方法），但子类若覆盖改写了该方法，再去调用父类的对象则会报错。
	 * 
	 */
	@Test
	public void testQux() throws Exception {

		User user = new User();
		My my = new My();

		Method userMethod = User.class.getMethod("setName", String.class);
		System.out.println(userMethod);

		Method myMethod = My.class.getMethod("setName", String.class);
		System.out.println(myMethod);

		userMethod.invoke(my, "I am My"); // 父类的Method可以调用子类的对象没问题
		System.out.println(my.getName());

		try {
			myMethod.invoke(user, "I am User"); // 报错，子类的Method
												// 调用父类的对象（因为子类覆盖改写了该方法）
			System.out.println(user.getName());
		} catch (Exception e) {
			System.err.println("error...");
		}

		Method userSetIdMethod = User.class.getMethod("setId", int.class);
		System.out.println(userMethod);

		Method mySetIdMethod = My.class.getMethod("setId", int.class);
		System.out.println(myMethod);

		userSetIdMethod.invoke(my, 999); // 父类的Method可以调用子类的对象没问题
		System.out.println(my.getId());

		mySetIdMethod.invoke(user, 123); // 子类的Method也可以调用父类的对象（前提是子类未覆盖父类的该方法）
		System.out.println(user.getId());
	}

	public static class My extends User {

		private String name;

		public String getName() {
			System.out.println("My.getName()");
			return name;
		}

		public void setName(String name) {
			System.out.println("My.setName():" + name);
			this.name = name;
		}
	}

	public static class User {

		private int id;
		private String name;
		private boolean sex;
		private Date birth;

		// int age;

		public int getAge() {
			return 20;
		}

		public void setAge(int age) {
			// this.age = age;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			System.out.println("User.getName()");
			return name;
		}

		public void setName(String name) {
			System.out.println("User.setName():" + name);
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
