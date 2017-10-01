package com.jsan.convert.support.split;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SplitFuncUtils {

	public static Object handleSourceBySplitToMap(Object source, boolean trim) {

		if (source instanceof String) {
			String str = (String) source;
			if (str.contains(",")) {
				source = splitStringToMap(str, trim);
			}
		}

		return source;
	}

	public static Object handleSourceBySplitToArray(Object source, boolean trim) {

		if (source instanceof String) {
			String str = (String) source;
			if (str.contains(",")) {
				source = splitStringToArray(str, trim);
			}
		}

		return source;
	}

	private static Map<String, String> splitStringToMap(String str, boolean trim) {

		Map<String, String> map = new LinkedHashMap<String, String>();
		List<String> list = splitStringToList(str, trim);

		for (int i = 0; i < list.size(); i++) {
			String itemStr = list.get(i);
			int j = itemStr.indexOf(':');
			if (j == -1) {
				map.put(itemStr, null);
			} else {
				String key = itemStr.substring(0, j);
				String value = itemStr.substring(j + 1);
				map.put(trim ? key.trim() : key, trim ? value.trim() : value);
			}
		}

		return map;
	}

	private static List<String> splitStringToList(String str, boolean trim) {

		List<String> list = new ArrayList<String>();
		int len = str.length();
		int i = 0;
		int start = 0;
		while (i < len) {
			if (str.charAt(i) == ',') {
				String tmp = str.substring(start, i);
				list.add(trim ? tmp.trim() : tmp);
				start = ++i;
				continue;
			}
			i++;
		}
		String tmp = str.substring(start, i);
		list.add(trim ? tmp.trim() : tmp);

		return list;
	}

	private static String[] splitStringToArray(String str, boolean trim) {

		List<String> list = splitStringToList(str, trim);

		return list.toArray(new String[list.size()]);
	}

}
