package com.jsan.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * 简易的 Http 请求工具类。
 * <ul>
 * <li>主要通过 HttpURLConnection 简易实现（仅支持 GET 请求）。</li>
 * <li>更专业的可参 Apache HttpComponents。</li>
 * </ul>
 * 
 */

public class HttpUtils {

	private static final int CONNECT_TIMEOUT = 10000; // 建立连接的超时时间为10秒

	private static final int READ_TIMEOUT = 30000; // 传递数据的超时时间为30秒

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";

	/**
	 * 返回 URL 请求的字符串形式（自动分析字符编码）。
	 * 
	 * @param url
	 * @return
	 */
	public static String getString(URL url) {

		return getString(url, null);
	}

	/**
	 * 返回 URL 请求的字符串形式（指定字符编码）。
	 * 
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String getString(URL url, String charset) {

		String str = null;
		HttpURLConnection conn = null;
		InputStream in = null;

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setRequestProperty("User-Agent", USER_AGENT); // 设置User-Agent，避免部分网站禁止网络爬虫抓取网页内容
			conn.connect();

			if (charset == null) {
				String contentType = conn.getContentType();
				if (contentType != null) {
					String temp = contentType.replaceAll(".*charset\\s*=\\s*([\\w-]+).*", "$1");
					if (!temp.equals(contentType) && temp.length() > 0) {
						charset = temp;
					}
				}
			}

			if (conn.getResponseCode() == 200) {

				in = conn.getInputStream();

				byte[] result = streamToBytes(in);

				if (result != null) {
					if (charset != null) {
						str = new String(result, charset);
					} else {
						str = new String(result);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return str;
	}

	/**
	 * 返回 URL 请求的字符串形式（自动分析字符编码）。
	 * 
	 * @param urlStr
	 * @return
	 */
	public static String getString(String urlStr) {

		return getString(urlStr, null);
	}

	/**
	 * 返回 URL 请求的字符串形式（指定字符编码）。
	 * 
	 * @param urlStr
	 * @param charset
	 * @return
	 */
	public static String getString(String urlStr, String charset) {

		return getString(getURL(urlStr), charset);
	}

	/**
	 * 返回 URL 请求的字节数组形式。
	 * 
	 * @param url
	 * @return
	 */
	public static byte[] getBytes(URL url) {

		byte[] bytes = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			toStream(url, out);
			bytes = out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return bytes;
	}

	/**
	 * 返回 URL 请求的字节数组形式。
	 * 
	 * @param urlStr
	 * @return
	 */
	public static byte[] getBytes(String urlStr) {

		return getBytes(getURL(urlStr));
	}

	/**
	 * 返回 URL 请求的文件形式。
	 * 
	 * @param url
	 * @param filePath
	 * @return
	 */
	public static File getFile(URL url, String filePath) {

		File oldFile = null;

		File file = new File(filePath);

		if (file.exists()) {
			oldFile = new File(filePath + System.currentTimeMillis() + new Random().nextInt());
			file.renameTo(oldFile);
		} else {
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
		}

		boolean error = false;

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			toStream(url, bos);
			return file;
		} catch (Exception e) {
			error = true;
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					// 异常的情况下如果该文件原先已存在的则保留原文件，不存在的则删除已生成的文件
					if (error) {
						if (file.delete() && oldFile != null) {
							oldFile.renameTo(file);
						}
					} else if (oldFile != null) {
						oldFile.delete();
					}
				}
			}
		}

		return null;
	}

	/**
	 * 返回 URL 请求的文件形式。
	 * 
	 * @param urlStr
	 * @param filePath
	 * @return
	 */
	public static File getFile(String urlStr, String filePath) {

		return getFile(getURL(urlStr), filePath);
	}

	/**
	 * URL 请求转输出流。
	 * 
	 * @param url
	 * @param out
	 * @throws IOException
	 */
	public static void toStream(URL url, OutputStream out) throws Exception {

		HttpURLConnection conn = null;
		InputStream in = null;

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setRequestProperty("User-Agent", USER_AGENT); // 设置User-Agent，避免部分网站禁止网络爬虫抓取网页内容
			conn.connect();

			if (conn.getResponseCode() == 200) {
				in = conn.getInputStream();
				convertStream(in, out);
			}

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * URL 请求转输出流。
	 * 
	 * @param urlStr
	 * @param out
	 * @throws IOException
	 */
	public static void toStream(String urlStr, OutputStream out) throws Exception {

		toStream(getURL(urlStr), out);
	}

	/**
	 * 输入流转为输出流。
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private static void convertStream(InputStream in, OutputStream out) throws IOException {

		byte[] buffer = new byte[1024 * 4];
		int len = 0;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}

	}

	private static byte[] streamToBytes(InputStream in) throws IOException {

		if (in == null) {
			return null;
		}

		ByteArrayOutputStream out = null;

		try {

			// 当从网络URL中获取输入流时不能使用 in.available()
			// result = new byte[in.available()];
			// in.read(result);

			out = new ByteArrayOutputStream();
			convertStream(in, out);
			return out.toByteArray();

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static URL getURL(String urlStr) {

		try {
			return new URL(urlStr);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
