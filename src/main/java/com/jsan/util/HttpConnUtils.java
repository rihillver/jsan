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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
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

public class HttpConnUtils {

	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String HEAD = "HEAD";
	public static final String OPTIONS = "OPTIONS";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";
	public static final String TRACE = "TRACE";

	private static final int CONNECT_TIMEOUT = 10000; // 建立连接的超时时间为10秒
	private static final int READ_TIMEOUT = 30000; // 传递数据的超时时间为30秒
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";

	public static class RequestInfo {

		private URL url = null; // URL实例
		private String urlStr = null; // url地址
		private String requestMethod = GET; // 请求方法必须大写
		// private boolean doInput = true;
		// private boolean doOutput = false;
		private boolean useCaches = false; // 是否使用缓存
		private boolean instanceFollowRedirects = false; // HttpURLConnection实例是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）
		private long ifModifiedSince = 0; // 如果指定了时间，则只有在该时间之后又进行了修改时，才获取该对象。
		private int connectTimeout = CONNECT_TIMEOUT;
		private int readTimeout = READ_TIMEOUT;

		private Map<String, Object> requestParamMap = null;
		private Map<String, String> requestCookieMap = null;
		private Map<String, String> requestPropertyMap = null;

		private OutputStream outputStream = null; // 不为null时表示自行处理输出流

		public RequestInfo() {

		}

		public RequestInfo(String urlStr) {

			this(urlStr, GET);
		}

		public RequestInfo(String urlStr, String requestMethod) {

			this(urlStr, requestMethod, null);
		}

		public RequestInfo(String urlStr, String requestMethod, Map<String, Object> requestParamMap) {

			this.urlStr = urlStr;
			this.requestMethod = requestMethod;
			this.requestParamMap = requestParamMap;
		}

		// -------------------

		public void setRequestProperty(String key, String value) {
			if (requestPropertyMap == null) {
				requestPropertyMap = new LinkedHashMap<>();
			}

			requestPropertyMap.put(key, value);
		}

		public void setRequestParam(String key, Object value) {
			if (requestParamMap == null) {
				requestParamMap = new LinkedHashMap<>();
			}
			requestParamMap.put(key, value);
		}

		public void setRequestCookie(String key, String value) {
			if (requestCookieMap == null) {
				requestCookieMap = new LinkedHashMap<>();
			}

			requestCookieMap.put(key, value);
		}

		// -------------------

		public String getUrlStr() {
			return urlStr;
		}

		public void setUrlStr(String urlStr) {
			this.urlStr = urlStr;
		}

		public URL getUrl() {
			return url;
		}

		public void setUrl(URL url) {
			this.url = url;
		}

		public String getRequestMethod() {
			return requestMethod;
		}

		public void setRequestMethod(String requestMethod) {
			this.requestMethod = requestMethod;
		}

		public boolean isInstanceFollowRedirects() {
			return instanceFollowRedirects;
		}

		public void setInstanceFollowRedirects(boolean instanceFollowRedirects) {
			this.instanceFollowRedirects = instanceFollowRedirects;
		}

		public boolean isUseCaches() {
			return useCaches;
		}

		public void setUseCaches(boolean useCaches) {
			this.useCaches = useCaches;
		}

		public long getIfModifiedSince() {
			return ifModifiedSince;
		}

		public void setIfModifiedSince(long ifModifiedSince) {
			this.ifModifiedSince = ifModifiedSince;
		}

		public int getConnectTimeout() {
			return connectTimeout;
		}

		public void setConnectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
		}

		public int getReadTimeout() {
			return readTimeout;
		}

		public void setReadTimeout(int readTimeout) {
			this.readTimeout = readTimeout;
		}

		public Map<String, String> getRequestPropertyMap() {
			return requestPropertyMap;
		}

		public void setRequestPropertyMap(Map<String, String> requestPropertyMap) {
			this.requestPropertyMap = requestPropertyMap;
		}

		public Map<String, Object> getRequestParamMap() {
			return requestParamMap;
		}

		public void setRequestParamMap(Map<String, Object> requestParamMap) {
			this.requestParamMap = requestParamMap;
		}

		public OutputStream getOutputStream() {
			return outputStream;
		}

		public void setOutputStream(OutputStream outputStream) {
			this.outputStream = outputStream;
		}

		public Map<String, String> getRequestCookieMap() {
			return requestCookieMap;
		}

		public void setRequestCookieMap(Map<String, String> requestCookieMap) {
			this.requestCookieMap = requestCookieMap;
		}

		@Override
		public String toString() {
			return "RequestInfo [url=" + url + ", urlStr=" + urlStr + ", requestMethod=" + requestMethod + ", useCaches=" + useCaches + ", instanceFollowRedirects=" + instanceFollowRedirects + ", ifModifiedSince=" + ifModifiedSince + ", connectTimeout=" + connectTimeout + ", readTimeout=" + readTimeout + ", requestParamMap=" + requestParamMap + ", requestCookieMap=" + requestCookieMap + ", requestPropertyMap="
					+ requestPropertyMap + "]";
		}

	}

	public static class ResponseInfo {

		private Exception exception; // 如果发生了异常则不为null

		private int responseCode;
		private String responseMessage;
		private String contentType;
		private String contentEncoding;
		private int contentLength;
		private long date;
		private long expiration;
		private long lastModified;
		private Map<String, List<String>> headerFields;

		private byte[] bytes;

		// ------------------------

		public String getCharset() {

			if (contentType != null) {
				String charset = contentType.replaceAll(".*charset\\s*=\\s*([\\w-]+).*", "$1");
				if (!charset.equals(contentType) && charset.length() > 0) {
					return charset;
				}
			}

			return null;
		}

		public String getString() {

			return getString(getCharset());
		}

		public String getString(String charset) {

			if (bytes != null) {
				if (charset != null) {
					try {
						return new String(bytes, charset);
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				} else {
					return new String(bytes);
				}
			}

			return null;
		}

		public Map<String, String> getCookieMap() {

			List<String> list = getCookieList();
			if (list == null) {
				return null;
			}

			Map<String, String> map = new LinkedHashMap<>();
			for (String str : list) {
				int i = str.indexOf(';');
				if (i != -1) {
					str = str.substring(0, i);
					int j = str.indexOf('=');
					if (j != -1) {
						String key = str.substring(0, j);
						String value = str.substring(j + 1);
						map.put(key, value);
					}
				}
			}

			return map;
		}

		public List<String> getCookieList() {

			if (headerFields != null) {
				return headerFields.get("Set-Cookie");
			}

			return null;
		}

		public String getHeaderField(String name) {

			if (headerFields != null) {
				List<String> list = headerFields.get(name);
				if (list.size() > 0) {
					return list.get(0);
				}
			}

			return null;
		}

		@SuppressWarnings("deprecation")
		public long getHeaderFieldDate(String name, long Default) {
			String dateString = getHeaderField(name);
			try {
				if (dateString.indexOf("GMT") == -1) {
					dateString = dateString + " GMT";
				}
				return Date.parse(dateString);
			} catch (Exception e) {
			}
			return Default;
		}

		public int getHeaderFieldInt(String name, int Default) {
			String value = getHeaderField(name);
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
			}
			return Default;
		}

		public long getHeaderFieldLong(String name, long Default) {
			String value = getHeaderField(name);
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
			}
			return Default;
		}

		public long getContentLengthLong() {
			return getHeaderFieldLong("content-length", -1);
		}

		// ------------------------

		public int getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
		}

		public String getResponseMessage() {
			return responseMessage;
		}

		public void setResponseMessage(String responseMessage) {
			this.responseMessage = responseMessage;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getContentEncoding() {
			return contentEncoding;
		}

		public void setContentEncoding(String contentEncoding) {
			this.contentEncoding = contentEncoding;
		}

		public int getContentLength() {
			return contentLength;
		}

		public void setContentLength(int contentLength) {
			this.contentLength = contentLength;
		}

		public long getDate() {
			return date;
		}

		public void setDate(long date) {
			this.date = date;
		}

		public long getExpiration() {
			return expiration;
		}

		public void setExpiration(long expiration) {
			this.expiration = expiration;
		}

		public long getLastModified() {
			return lastModified;
		}

		public void setLastModified(long lastModified) {
			this.lastModified = lastModified;
		}

		public Map<String, List<String>> getHeaderFields() {
			return headerFields;
		}

		public void setHeaderFields(Map<String, List<String>> headerFields) {
			this.headerFields = headerFields;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}

		public Exception getException() {
			return exception;
		}

		public void setException(Exception exception) {
			this.exception = exception;
		}

		@Override
		public String toString() {
			return "ResponseInfo [exception=" + exception + ", responseCode=" + responseCode + ", responseMessage=" + responseMessage + ", contentType=" + contentType + ", contentEncoding=" + contentEncoding + ", contentLength=" + contentLength + ", date=" + date + ", expiration=" + expiration + ", lastModified=" + lastModified + ", headerFields=" + headerFields + "]";
		}

	}

	public static ResponseInfo getResponseInfo(RequestInfo requestInfo) {

		ResponseInfo responseInfo = new ResponseInfo();

		HttpURLConnection conn = null;
		InputStream in = null;

		String requestMethod = requestInfo.getRequestMethod();

		URL url = requestInfo.getUrl();
		if (url == null) {
			String urlStr = requestInfo.getUrlStr();
			if (GET.equals(requestMethod)) {
				urlStr = convertRequestParamMapToUrlString(urlStr, requestInfo.getRequestParamMap());
			}

			url = getURL(urlStr);
		}

		try {
			conn = (HttpURLConnection) url.openConnection();

			// conn.setDoOutput(requestInfo.isDoInput());
			// conn.setDoOutput(requestInfo.isDoOutput());
			conn.setRequestMethod(requestInfo.getRequestMethod());
			conn.setConnectTimeout(requestInfo.getConnectTimeout());
			conn.setReadTimeout(requestInfo.getReadTimeout());
			conn.setUseCaches(requestInfo.isUseCaches());
			conn.setIfModifiedSince(requestInfo.getIfModifiedSince());
			conn.setInstanceFollowRedirects(requestInfo.isInstanceFollowRedirects());
			conn.setRequestProperty("User-Agent", USER_AGENT); // 设置User-Agent，避免部分网站禁止非常规的User-Agent请求

			// 处理请求时发送的cookie
			Map<String, String> requestCookieMap = requestInfo.getRequestCookieMap();
			if (requestCookieMap != null) {
				conn.setRequestProperty("Cookie", convertRequestCookieMapToString(requestCookieMap));
			}

			Map<String, String> requestPropertyMap = requestInfo.getRequestPropertyMap();
			if (requestPropertyMap != null) {
				for (Map.Entry<String, String> entry : requestPropertyMap.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			Map<String, Object> requestParamMap = requestInfo.getRequestParamMap();
			if (requestParamMap != null && POST.equals(requestMethod)) {
				conn.setDoOutput(true);
			}

			conn.connect();

			if (requestParamMap != null && POST.equals(requestMethod)) {
				PrintWriter writer = new PrintWriter(conn.getOutputStream());
				writer.write(convertRequestParamMapToString(requestParamMap));
				writer.close();
			}

			int responseCode = conn.getResponseCode();
			responseInfo.setResponseCode(responseCode);
			if (responseCode == 200) {

				responseInfo.setContentEncoding(conn.getContentEncoding());
				responseInfo.setContentLength(conn.getContentLength());
				responseInfo.setContentType(conn.getContentType());
				responseInfo.setDate(conn.getDate());
				responseInfo.setExpiration(conn.getExpiration());
				responseInfo.setLastModified(conn.getLastModified());
				responseInfo.setHeaderFields(conn.getHeaderFields());

				in = conn.getInputStream();

				if (requestInfo.getOutputStream() != null) {
					convertStream(in, requestInfo.getOutputStream());
				} else {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					convertStream(in, out);
					responseInfo.setBytes(out.toByteArray());
				}
			}

		} catch (Exception e) {
			responseInfo.setException(e);
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

		return responseInfo;
	}

	/******************************************************/

	/**
	 * 返回 URL 请求的字符串形式（GET 方式，自动分析字符编码）。
	 * 
	 * @param urlStr
	 * @return
	 */
	public static String getString(String urlStr) {

		return getString(urlStr, null);
	}

	/**
	 * 返回 URL 请求的字符串形式（GET 方式，指定字符编码）。
	 * 
	 * @param urlStr
	 * @param charset
	 * @return
	 */
	public static String getString(String urlStr, String charset) {

		return getString(urlStr, GET, null, charset);
	}

	/**
	 * 返回 URL 请求的字符串形式（自动分析字符编码）。
	 * 
	 * @param urlStr
	 * @param requestMethod
	 * @param requestParamMap
	 * @return
	 */
	public static String getString(String urlStr, String requestMethod, Map<String, Object> requestParamMap) {

		return getString(urlStr, requestMethod, requestParamMap, null);
	}

	/**
	 * 返回 URL 请求的字符串形式（指定字符编码）。
	 * 
	 * @param urlStr
	 * @param requestMethod
	 * @param requestParamMap
	 * @param charset
	 * @return
	 */
	public static String getString(String urlStr, String requestMethod, Map<String, Object> requestParamMap, String charset) {

		ResponseInfo responseInfo = getResponseInfo(new RequestInfo(urlStr, requestMethod, requestParamMap));

		return charset == null ? responseInfo.getString() : responseInfo.getString(charset);

	}

	/**
	 * 返回 URL 请求的字节数组形式（GET 方式）。
	 * 
	 * @param urlStr
	 * @return
	 */
	public static byte[] getBytes(String urlStr) {

		return getBytes(urlStr, GET, null);
	}

	/**
	 * 返回 URL 请求的字节数组形式。
	 * 
	 * @param urlStr
	 * @param requestMethod
	 * @param requestParamMap
	 * @return
	 */
	public static byte[] getBytes(String urlStr, String requestMethod, Map<String, Object> requestParamMap) {

		return getResponseInfo(new RequestInfo(urlStr, requestMethod, requestParamMap)).getBytes();
	}

	/**
	 * 返回 URL 请求的文件形式（GET 方式）。
	 * 
	 * @param urlStr
	 * @param filePath
	 * @return
	 */
	public static File getFile(String urlStr, String filePath) {

		return getFile(urlStr, GET, null, filePath);
	}

	/**
	 * 返回 URL 请求的文件形式。
	 * 
	 * @param urlStr
	 * @param requestMethod
	 * @param requestParamMap
	 * @param filePath
	 * @return
	 */
	public static File getFile(String urlStr, String requestMethod, Map<String, Object> requestParamMap, String filePath) {

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

			RequestInfo requestInfo = new RequestInfo(urlStr, requestMethod, requestParamMap);
			requestInfo.setOutputStream(bos);
			getResponseInfo(requestInfo);

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
	 * 将请求 url 和 Map 形式的请求参数转换成经过 URL 编码的 url 字符串（主要用于 GET 请求）。
	 * 
	 * @param urlStr
	 * @param paramMap
	 * @return
	 */
	private static String convertRequestParamMapToUrlString(String urlStr, Map<String, Object> paramMap) {

		if (paramMap != null) {
			if (urlStr.indexOf('?') == -1) {
				urlStr = urlStr + "?" + convertRequestParamMapToString(paramMap);
			} else {
				urlStr = urlStr + "&" + convertRequestParamMapToString(paramMap);
			}
		}

		return urlStr;
	}

	/**
	 * 将 Map 形式的请求参数转换成经过 URL 编码的字符串。
	 * 
	 * @param paramMap
	 * @return
	 */
	private static String convertRequestParamMapToString(Map<String, Object> paramMap) {

		if (paramMap == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
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

	private static String convertRequestCookieMapToString(Map<String, String> cookieMap) {

		if (cookieMap == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
			if (i++ > 0) {
				sb.append("; ");
			}
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
		}

		return sb.toString();
	}

}
