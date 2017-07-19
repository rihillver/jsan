package com.jsan.convert.support.json;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class JsonFuncUtils {

	private static final Logger logger = LoggerFactory.getLogger(JsonFuncUtils.class);

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
					source = JSON.parseObject(json, Object[].class);
				} catch (Exception e) {
					logger.warn("Cannot convert to JSON array: {}", source);
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
					source = JSON.parseObject(json, Map.class);
				} catch (Exception e) {
					logger.warn("Cannot convert to JSON object: {}", source);
				}
			}
		}

		return source;
	}

}
