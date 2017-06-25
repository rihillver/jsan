package com.jsan.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import junit.framework.TestCase;

public class HttpUtilsTest extends TestCase {

	public void testFoo() throws UnsupportedEncodingException {

		long start = System.nanoTime();

		String url = "http://www.abc.com/app/query.do?key=" + URLEncoder.encode("可乐", "utf-8");
		System.out.println(HttpUtils.getString(url));
		long end = System.nanoTime() - start;

		System.out.println(end);

	}

	public void testBar() {

		String url = "https://www.baidu.com/";
		System.out.println(HttpUtils.getFile(url, "d:/baidu.html"));
	}

	public void testBaz() {

		String httpsUrl = "https://www.baidu.com/";
		System.out.println(HttpUtils.getString(httpsUrl));
	}

	public void testQux() {

		String imgUrl = "https://www.baidu.com/img/bd_logo.png";
		System.out.println(HttpUtils.getFile(imgUrl, "d:/baidu.png"));
	}

	public void testQuux() {

		String imgUrl = "https://www.baidu.com/img/bd_logo.png";
		byte[] bs = HttpUtils.getBytes(imgUrl);
		for (byte b : bs) {
			System.out.print(b + "-");
		}
	}

}
