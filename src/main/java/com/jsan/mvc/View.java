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

	public static final String VALUE = "value"; // 默认设置的 request 属性名

	private Map<String, Object> map = new LinkedHashMap<String, Object>();
	private Map<String, Object> attribute;

	private String url = ""; // 初始化为""，当被设置为null时，则表示人为的要求抛出404错误
	private Resolver resolver;

	private String contentType;
	private String jsonpCallback;
	private JsonSerializeConfigurator jsonSerializeConfigurator;

	public Object getValue() {
		return get(VALUE);
	}

	public void addValue(Object value) {
		add(VALUE, value);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		return "View [map=" + map + ", attribute=" + attribute + ", url=" + url + ", resolver=" + resolver
				+ ", contentType=" + contentType + ", jsonpCallback=" + jsonpCallback + ", jsonSerializeConfigurator="
				+ jsonSerializeConfigurator + "]";
	}

}
