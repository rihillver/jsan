package com.jsan.convert;

import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonTest {

	@Test
	public void foo() {

		String dateTimePattern = "yyyy/MM/dd";

		String json0 = "{\"a\":\"2017/08/09\",\"b\":\"2000/05/05\"}";

		Gson gson = new GsonBuilder().setDateFormat(dateTimePattern).create();

		Map<?, ?> map = gson.fromJson(json0, Map.class);

		for (Map.Entry<?, ?> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " - " + entry.getValue().getClass());
		}

		System.out.println();
		
		String json1 = "[\"2017/08/09\",\"2000/05/05\"]";

		Date[] objs = gson.fromJson(json1, Date[].class);

		for (Object obj : objs) {
			System.out.println(obj.getClass()+" - "+obj);
		}
	}
}
