package com.jsan.convert.support.json;

import java.util.Map;

import com.google.gson.Gson;

public class JsonFuncUtils {

	private static final Gson gson = new Gson();

	/**
	 * 通过判断字符串的开头和结尾是否有 "["、"]" 来判断是否为 Json 字符串（数组形式）。
	 * 
	 * @param source
	 * @return
	 */
	public static Object handleSourceByJsonToArray(Object source) {

		if (source instanceof String) {
			String json = (String) source;
			if (json.startsWith("[") && json.endsWith("]")) {
				try {
					source = gson.fromJson(json, Object[].class);
				} catch (Exception e) {
					// logging...
					// e.printStackTrace();
				}
			}
		}

		return source;
	}

	/**
	 * 通过判断字符串的开头和结尾是否有 "{"、"}" 来判断是否为 Json 字符串（对象形式）。
	 * 
	 * @param source
	 * @return
	 */
	public static Object handleSourceForJsonToMap(Object source) {

		if (source instanceof String) {
			String json = (String) source;
			if (json.startsWith("{") && json.endsWith("}")) {
				try {
					source = gson.fromJson(json, Map.class);
				} catch (Exception e) {
					// logging...
					// e.printStackTrace();
				}
			}
		}

		return source;
	}

}
