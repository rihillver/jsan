package com.jsan.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;

import junit.framework.TestCase;

@Ignore
public class HttpUtilsTest extends TestCase {

	public void testFoo() throws UnsupportedEncodingException {

		long start = System.nanoTime();

		String url = "http://www.abc.com/app/query.do?key=" + URLEncoder.encode("可乐", "utf-8");
		System.out.println(HttpUtils.getString(url));
		long end = System.nanoTime() - start;

		System.out.println(end);

	}

	public void testFoz() {

		String url = "http://www.abc.com/app/query.do";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", "可乐");

		System.out.println(HttpUtils.getString(url, HttpUtils.POST, map));

	}

	/**
	 * 获取重定向地址，Response Code 为 3XX。
	 * 
	 */
	public void testFox() throws IOException {

		String urlStr = "http://www.baidu.com/abcdefghijklmn";

		HttpURLConnection conn = null;

		URL url = new URL(urlStr);

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(false); // 必须设置false，否则将重定向到Location的地址
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");

			conn.connect();

			if (conn.getResponseCode() >= 300 && conn.getResponseCode() <= 399) {
				String location = conn.getHeaderField("Location");
				System.out.println("重定向地址：" + location);
			}

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

	}

	public void testBar() {

		String url = "https://www.baidu.com/";
		System.out.println(HttpUtils.getFile(url, HttpUtils.GET, null, "d:/baidu.html"));
	}

	public void testBaz() {

		String httpsUrl = "https://www.baidu.com/";
		System.out.println(HttpUtils.getString(httpsUrl));
	}

	public void testQux() {

		String imgUrl = "https://www.baidu.com/img/bd_logo.png";
		System.out.println(HttpUtils.getFile(imgUrl, "d:/baidu.png"));
	}

	public void testQuz() {

		String imgUrl = "https://www.baidu.com/img/bd_logo.png";
		byte[] bs = HttpUtils.getBytes(imgUrl);
		for (byte b : bs) {
			System.out.print(b + "-");
		}
	}

}
