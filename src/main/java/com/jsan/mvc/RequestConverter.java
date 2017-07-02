package com.jsan.mvc;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Request 包装类，用于在表单提交方式为 GET 时处理中文乱码，对指定字符编码进行转换，以及去除请求参数的前后空白。
 *
 */

public class RequestConverter extends HttpServletRequestWrapper {

	private HttpServletRequest request;
	private String fromEncoding;
	private String toEncoding;
	private boolean trim;
	private boolean isParameterMapConverted;

	public RequestConverter(HttpServletRequest request, boolean trim) {

		this(request, null, null, false);
	}

	public RequestConverter(HttpServletRequest request, String fromEncoding, String toEncoding) {

		this(request, fromEncoding, toEncoding, false);
	}

	public RequestConverter(HttpServletRequest request, String fromEncoding, String toEncoding, boolean trim) {

		super(request);
		this.request = request;
		this.fromEncoding = fromEncoding;
		this.toEncoding = toEncoding;
		this.trim = trim;
	}

	@Override
	public String getParameter(String arg0) {

		String value = request.getParameter(arg0);
		value = getConvert(value);

		return value;
	}

	@Override
	public String[] getParameterValues(String arg0) {

		String[] values = request.getParameterValues(arg0);

		// if (values != null) {
		// for (int i = 0; i < values.length; i++) {
		// values[i] = getConvert(values[i]);
		// }
		// }

		for (int i = 0; i < values.length; i++) {
			values[i] = getConvert(values[i]);
		}

		return values;
	}

	@Override
	public Map<String, String[]> getParameterMap() {

		Map<String, String[]> map = request.getParameterMap();

		if (!isParameterMapConverted) {
			if (map != null) {
				for (String[] values : map.values()) {
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							values[i] = getConvert(values[i]);
						}
					}
				}
			}
			isParameterMapConverted = !isParameterMapConverted;
		}

		return map;
	}

	/**
	 * 转换字符编码。
	 * 
	 * @param str
	 * @return
	 */
	private String getConvert(String str) {

		if (str != null) {
			if (fromEncoding != null && toEncoding != null) {
				try {
					str = new String(str.getBytes(fromEncoding), toEncoding);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
			if (trim) {
				str = str.trim();
			}
		}

		return str;
	}

}
