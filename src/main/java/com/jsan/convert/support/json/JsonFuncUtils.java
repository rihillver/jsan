package com.jsan.convert.support.json;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jsan.convert.Formatter;
import com.jsan.convert.Recursiveable;

public class JsonFuncUtils {

	private static final Gson gson = new Gson();
	
	private static final HashMap<String, Gson> gsonCache = new HashMap<String, Gson>();

	/**
	 * 返回 Gson 缓存的单实例对象。
	 * 
	 * @return
	 */
	public static Gson getGson() {

		return gson;
	}

	/**
	 * 返回 Gson 缓存的单实例对象（自定义时间转换格式）。
	 * 
	 * @param dateTimePattern
	 * @return
	 */
	public static Gson getGson(String dateTimePattern) {

		if (dateTimePattern == null) {
			return getGson();
		}

		Gson gson = gsonCache.get(dateTimePattern);
		if (gson == null) {
			synchronized (gsonCache) {
				gson = gsonCache.get(dateTimePattern);
				if (gson == null) {
					gson = new GsonBuilder().setDateFormat(dateTimePattern).create();
					gsonCache.put(dateTimePattern, gson);
				}
			}
		}

		return gson;
	}

	/**
	 * 通过判断字符串的开头和结尾是否有 "["、"]" 来判断是否为 Json 字符串（数组形式）。
	 * 
	 * @param recursiveable
	 * @param clazz
	 * @param source
	 * @return
	 */
	public static Object handleSourceByJsonToArray(Recursiveable recursiveable, Object source) {

		if (source instanceof String) {
			String json = (String) source;
			if (json.startsWith("[") && json.endsWith("]")) {
				Object[] objects = convertJsonToArray(json, getDateTimePattern(recursiveable));
				if (objects != null) {
					source = objects;
				}
			}
		}

		return source;
	}

	/**
	 * 通过判断字符串的开头和结尾是否有 "{"、"}" 来判断是否为 Json 字符串（对象形式）。
	 * 
	 * @param recursiveable
	 * @param clazz
	 * @param source
	 * @return
	 */
	public static Object handleSourceForJsonToMap(Recursiveable recursiveable, Object source) {

		if (source instanceof String) {
			String json = (String) source;
			if (json.startsWith("{") && json.endsWith("}")) {
				Map<?, ?> map = convertJsonToMap(json, getDateTimePattern(recursiveable));
				if (map != null) {
					source = map;
				}
			}
		}

		return source;
	}

	/**
	 * 此处必须捕获异常，使不符合 Json 转换的情况下返回 null 。
	 * 
	 * @param json
	 * @param dateTimePattern
	 * @return
	 */
	private static Object[] convertJsonToArray(String json, String dateTimePattern) {

		try {
			return getGson(dateTimePattern).fromJson(json, Object[].class);
		} catch (Exception e) {
			// logging...
			// e.printStackTrace();
			return null;
		}
	}

	/**
	 * 此处必须捕获异常，使不符合 Json 转换的情况下返回 null 。
	 * 
	 * @param json
	 * @param dateTimePattern
	 * @return
	 */
	private static Map<?, ?> convertJsonToMap(String json, String dateTimePattern) {

		try {
			return getGson(dateTimePattern).fromJson(json, Map.class);
		} catch (Exception e) {
			// logging...
			// e.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过@DateTimePattern 注解获得 Gson 的时间格式。
	 * 
	 * @param recursiveable
	 * @return
	 */
	private static String getDateTimePattern(Recursiveable recursiveable) {

		String dateTimePattern = null;
		Formatter formatter = recursiveable.lookupFormatter(Date.class);
		if (formatter != null) {
			dateTimePattern = formatter.getPattern();
		}

		return dateTimePattern;
	}

}
