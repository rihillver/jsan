package com.jsan.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

/**
 * 简易的 Http 请求工具类。
 * <ul>
 * <li>通过 HttpURLConnection 简易实现。</li>
 * <li>更专业的可参 Apache HttpComponents。</li>
 * </ul>
 * 
 */

public class HttpUtils {

	public static final String GET = "GET";

	public static final String POST = "POST";

	private static final int CONNECT_TIMEOUT = 10000; // 建立连接的超时时间为10秒

	private static final int READ_TIMEOUT = 30000; // 传递数据的超时时间为30秒

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";

	/**
	 * 返回 URL 请求的字符串形式（GET 方式，自动分析字符编码）。
	 * 
	 * @param urlStr
	 * @return
	 */
	public static String getString(String urlStr) {

		return getString(urlStr, null, null);
	}

	/**
	 * 返回 URL 请求的字符串形式（自动分析字符编码）。
	 * 
	 * @param urlStr
	 * @param method
	 * @param params
	 * @return
	 */
	public static String getString(String urlStr, String method, Map<String, Object> params) {

		return getString(urlStr, method, params, null);
	}

	/**
	 * 返回 URL 请求的字符串形式（GET 方式，指定字符编码）。
	 * 
	 * @param urlStr
	 * @param charset
	 * @return
	 */
	public static String getString(String urlStr, String charset) {

		return getString(urlStr, null, null, charset);
	}

	/**
	 * 返回 URL 请求的字符串形式（指定字符编码）。
	 * 
	 * @param urlStr
	 * @param method
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String getString(String urlStr, String method, Map<String, Object> params, String charset) {

		CharsetRecorder recorder = charset == null ? new CharsetRecorder() : null;

		byte[] result = getBytes(urlStr, method, params, recorder);

		if (charset == null) {
			charset = recorder.getCharset();
		}

		if (result != null) {
			if (charset != null) {
				try {
					return new String(result, charset);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			} else {
				return new String(result);
			}
		}

		return null;

	}

	/**
	 * 返回 URL 请求的字节数组形式（GET 方式）。
	 * 
	 * @param urlStr
	 * @return
	 */
	public static byte[] getBytes(String urlStr) {

		return getBytes(urlStr, null, null);
	}

	/**
	 * 返回 URL 请求的字节数组形式。
	 * 
	 * @param urlStr
	 * @param method
	 * @param params
	 * @return
	 */
	public static byte[] getBytes(String urlStr, String method, Map<String, Object> params) {

		return getBytes(urlStr, method, params, null);
	}

	private static byte[] getBytes(String urlStr, String method, Map<String, Object> params, CharsetRecorder recorder) {

		byte[] bytes = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			toStream(urlStr, out, method, params, recorder);
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
	 * 返回 URL 请求的文件形式（GET 方式）。
	 * 
	 * @param urlStr
	 * @param filePath
	 * @return
	 */
	public static File getFile(String urlStr, String filePath) {

		return getFile(urlStr, null, null, filePath);
	}

	/**
	 * 返回 URL 请求的文件形式。
	 * 
	 * @param urlStr
	 * @param method
	 * @param params
	 * @param filePath
	 * @return
	 */
	public static File getFile(String urlStr, String method, Map<String, Object> params, String filePath) {

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
			toStream(urlStr, bos, method, params);
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

	private static URL getURL(String urlStr) {

		try {
			return new URL(urlStr);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * URL 请求转输出流。
	 * 
	 * @param urlStr
	 * @param out
	 * @param method
	 * @param params
	 * @throws Exception
	 */
	public static void toStream(String urlStr, OutputStream out, String method, Map<String, Object> params)
			throws Exception {

		toStream(urlStr, out, method, params, null);
	}

	private static void toStream(String urlStr, OutputStream out, String method, Map<String, Object> params,
			CharsetRecorder recorder) throws Exception {

		HttpURLConnection conn = null;
		InputStream in = null;

		if (method != null) {
			method = method.toUpperCase();
		}

		if (method == null || GET.equals(method)) {
			urlStr = convertParamToUrlString(urlStr, params);
		}

		URL url = getURL(urlStr);

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false); // 禁止缓存
			conn.setInstanceFollowRedirects(false); // 禁止重定向
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setRequestProperty("User-Agent", USER_AGENT); // 设置User-Agent，避免部分网站禁止非常规的User-Agent请求

			if (method != null) {
				conn.setRequestMethod(method);
				if (POST.equals(method)) {
					conn.setDoOutput(true);
				}
			}

			conn.connect();

			if (params != null && POST.equals(method)) {
				PrintWriter writer = new PrintWriter(conn.getOutputStream());
				writer.write(convertParamToString(params));
				writer.flush();
			}

			if (conn.getResponseCode() == 200) {

				if (recorder != null) { // 提取字符编码
					String contentType = conn.getContentType();
					if (contentType != null) {
						String temp = contentType.replaceAll(".*charset\\s*=\\s*([\\w-]+).*", "$1");
						if (!temp.equals(contentType) && temp.length() > 0) {
							recorder.setCharset(temp);
						}
					}
				}

				in = conn.getInputStream();
				convertStream(in, out);
			} else {
				throw new RuntimeException("response code is " + conn.getResponseCode());
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
	 * 将请求 url 和 Map 形式的请求参数转换成经过 URL 编码的 url 字符串（主要用于 GET 请求）。
	 * 
	 * @param urlStr
	 * @param params
	 * @return
	 */
	public static String convertParamToUrlString(String urlStr, Map<String, Object> params) {

		if (params != null) {
			if (urlStr.indexOf('?') == -1) {
				urlStr = urlStr + "?" + convertParamToString(params);
			} else {
				urlStr = urlStr + "&" + convertParamToString(params);
			}
		}

		return urlStr;
	}

	/**
	 * 将 Map 形式的请求参数转换成经过 URL 编码的字符串。
	 * 
	 * @param params
	 * @return
	 */
	public static String convertParamToString(Map<String, Object> params) {

		if (params == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (i++ > 0) {
				sb.append("&");
			}
			sb.append(entry.getKey());
			sb.append("=");
			Object value = entry.getValue();
			if (value == null) {
				sb.append("");
			} else {
				try {
					sb.append(URLEncoder.encode(value.toString(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return sb.toString();
	}

	private static class CharsetRecorder {

		String charset;

		public String getCharset() {
			return charset;
		}

		public void setCharset(String charset) {
			this.charset = charset;
		}

	}

}
