package com.jsan.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie 操作工具类。
 * <ul>
 * <li>不指定 maxAge 的时候默认为 -1 ，即浏览器关闭后 Cookie 随即失效。</li>
 * <li>path 为 null 的时候 Cookie 的路径默认在其请求的路径下。</li>
 * </ul>
 *
 */

public class CookieUtils {

	private static final String CHARACTER_ENCODING = "utf-8";

	/**
	 * 添加 Cookie（path 为根路径 "/"）。
	 * 
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void addCookie(HttpServletResponse response, String name, String value) {

		addCookie(response, name, value, null, "/", null);
	}

	/**
	 * 添加 Cookie（path 为根路径 "/"）。
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, Integer maxAge) {

		addCookie(response, name, value, maxAge, "/", null);
	}

	/**
	 * 添加 Cookie 。
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 * @param path
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, Integer maxAge, String path) {

		addCookie(response, name, value, maxAge, path, null);
	}

	/**
	 * 添加 Cookie 。
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 * @param path
	 * @param domain
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, Integer maxAge, String path,
			String domain) {

		try {
			name = URLEncoder.encode(name, CHARACTER_ENCODING);
			if (value != null) {
				value = URLEncoder.encode(value, CHARACTER_ENCODING);
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		Cookie cookie = new Cookie(name, value);

		if (maxAge != null) {
			cookie.setMaxAge(maxAge);
		}
		if (path != null) {
			cookie.setPath(path);
		}
		if (domain != null) {
			cookie.setDomain(domain);
		}

		response.addCookie(cookie);
	}

	/**
	 * 删除 Cookie（path 为根路径 "/"）。
	 * 
	 * @param request
	 * @param response
	 * @param name
	 */
	public static void removeCookie(HttpServletResponse response, String name) {

		addCookie(response, name, null, 0, "/", null);
	}

	/**
	 * 删除 Cookie 。
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param path
	 */
	public static void removeCookie(HttpServletResponse response, String name, String path) {

		addCookie(response, name, null, 0, path, null);
	}

	/**
	 * 删除 Cookie 。
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param path
	 * @param domain
	 */
	public static void removeCookie(HttpServletResponse response, String name, String path, String domain) {

		addCookie(response, name, null, 0, path, domain);
	}

	/**
	 * 返回 Cookie 。
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {

		Cookie[] cookies = request.getCookies();

		if (cookies != null && name != null) {
			try {
				name = URLEncoder.encode(name, CHARACTER_ENCODING);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			for (Cookie itemCookie : cookies) {
				if (name.equals(itemCookie.getName())) {
					return itemCookie;
				}
			}
		}

		return null;
	}

	/**
	 * 返回 Cookie 值。
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {

		Cookie cookie = getCookie(request, name);

		if (cookie != null) {
			try {
				return URLDecoder.decode(cookie.getValue(), CHARACTER_ENCODING);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

		return null;
	}

	/**
	 * 返回 Cookie 键值 Map 。
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getCookieMap(HttpServletRequest request) {

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			Map<String, String> cookieMap = new HashMap<String, String>();
			for (Cookie cookie : cookies) {
				try {
					cookieMap.put(URLDecoder.decode(cookie.getName(), CHARACTER_ENCODING),
							URLDecoder.decode(cookie.getValue(), CHARACTER_ENCODING));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
			return cookieMap;
		}

		return null;
	}

}
