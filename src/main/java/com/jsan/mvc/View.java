package com.jsan.mvc;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jsan.mvc.json.JsonSerializeConfigurator;
import com.jsan.mvc.resolve.Resolver;

/**
 * 封装视图结果集以及相关信息。
 *
 */

public class View {

	public static final String DATA = "data"; // 默认设置的request属性名

	private Map<String, Object> map = new LinkedHashMap<String, Object>();
	private Map<String, Object> attribute;

	private String url = ""; // 初始化为""，当被设置为null时，则表示人为的要求抛出404错误
	private int statusCode; // http状态码
	private int errorCode; // http状态码，但是 HttpServletResponse 执行的是 sendError() 操作

	private Resolver resolver;

	private String contentType;
	private String jsonpCallback;
	private JsonSerializeConfigurator jsonSerializeConfigurator;

	public Object getData() {
		return get(DATA);
	}

	public void addData(Object data) {
		add(DATA, data);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public Resolver getResolver() {
		return resolver;
	}

	public void setResolver(Resolver resolver) {
		this.resolver = resolver;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getJsonpCallback() {
		return jsonpCallback;
	}

	public void setJsonpCallback(String jsonpCallback) {
		this.jsonpCallback = jsonpCallback;
	}

	public JsonSerializeConfigurator getJsonSerializeConfigurator() {
		return jsonSerializeConfigurator;
	}

	public void setJsonSerializeConfigurator(JsonSerializeConfigurator jsonSerializeConfigurator) {
		this.jsonSerializeConfigurator = jsonSerializeConfigurator;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public void add(String key, Object value) {

		map.put(key, value);
	}

	public Object get(String key) {

		return map.get(key);
	}

	public void setAttribute(String key, Object value) {

		if (attribute == null) {
			attribute = new HashMap<String, Object>();
		}
		attribute.put(key, value);
	}

	public Object getAttribute(String key) {

		if (attribute == null) {
			return null;
		} else {
			return attribute.get(key);
		}
	}

	@Override
	public String toString() {
		return "View [map=" + map + ", attribute=" + attribute + ", url=" + url + ", statusCode=" + statusCode + ", errorCode=" + errorCode + ", resolver=" + resolver + ", contentType=" + contentType + ", jsonpCallback=" + jsonpCallback + ", jsonSerializeConfigurator=" + jsonSerializeConfigurator + "]";
	}

}
