package com.jsan.convert;

import java.util.Date;

import junit.framework.TestCase;

public class BeanProxyUtilsTest extends TestCase {

	public void testBar() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Class<?> clazz = Class.forName("com.jsan.convert.BeanProxyUtilsTest$User");

		User user = (User) clazz.newInstance();

		user.setId(1);
		user.setName("jiang");
		user.setSex(true);

		System.out.println(user);
	}

	public void testFoo() {

		User user = BeanProxyUtils.getObject(User.class);
		user.setId(123);
		user.setBirth(new Date());
		user.setName("shan");

		System.out.println(user);
	}

	public static class User {

		int id;
		String name;
		boolean sex;
		Date birth;

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
