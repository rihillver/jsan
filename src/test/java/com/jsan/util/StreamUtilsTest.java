package com.jsan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import junit.framework.TestCase;

public class StreamUtilsTest extends TestCase {

	public void testBar() throws IOException {

		byte[] bs = StreamUtils.readStreamToByte(new FileInputStream("d:/user"));

		for (byte b : bs) {
			System.out.print((char) b);
		}
	}

	public void testFoo() throws IOException {

		User user = new User();
		user.setName("shan");

		StreamUtils.writeObject(new File("d:/user"), user);

		System.out.println(StreamUtils.readObject("d:/user"));
	}

	public static class User implements Serializable {

		private static final long serialVersionUID = 2853164349886170295L;

		int id;
		String name;

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

		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + "]";
		}

	}
}
