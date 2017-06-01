package com.jsan.mvc.resolve.suport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.Resolver;
import com.jsan.mvc.resolve.annotation.RegisterName;

@RegisterName("json")
public class JsonResolver implements Resolver {

	public static final String DATE_FORMAT = "dateFormat";
	public static final String SERIALIZE_NULLS = "serializeNulls";
	public static final String PRETTY_PRINTING = "prettyPrinting";

	private static final Gson gson = new Gson();
	private static final Gson gsonBySerializeNulls = new GsonBuilder().serializeNulls().create();
	private static final Gson gsonByPrettyPrinting = new GsonBuilder().setPrettyPrinting().create();
	private static final HashMap<String, Gson> gsonByDateFormatCache = new HashMap<String, Gson>();

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setContentType("application/json");

		output(response, getJsonString(view));
	}

	protected void output(HttpServletResponse response, String str) throws IOException {

		PrintWriter out = response.getWriter();
		out.print(str);
		out.flush();
		out.close();
	}

	protected String getJsonString(View view) {

		String dateFormat = (String) view.getAttribute(DATE_FORMAT);
		Boolean serializeNulls = (Boolean) view.getAttribute(SERIALIZE_NULLS);
		Boolean prettyPrinting = (Boolean) view.getAttribute(PRETTY_PRINTING);

		int count = 0;

		if (dateFormat != null) {
			count++;
		}
		if (serializeNulls != null && serializeNulls) {
			count++;
		}
		if (prettyPrinting != null && prettyPrinting) {
			count++;
		}

		Gson gs = null;
		if (count == 0) {
			gs = getGson();
		} else if (count == 1) {
			if (dateFormat != null) {
				gs = getGsonByDateFormat(dateFormat);
			} else if (serializeNulls != null) {
				gs = getGsonBySerializeNulls();
			} else {
				gs = getGsonByPrettyPrinting();
			}
		} else {
			GsonBuilder gsonBuilder = new GsonBuilder();
			if (dateFormat != null) {
				gsonBuilder.setDateFormat(dateFormat);
			}
			if (serializeNulls != null) {
				gsonBuilder.serializeNulls();
			}
			if (prettyPrinting != null) {
				gsonBuilder.setPrettyPrinting();
			}
			gs = gsonBuilder.create();
		}

		return gs.toJson(view.getValue());
	}

	/**
	 * 返回 Gson 对象。
	 * 
	 * @return
	 */
	protected static Gson getGson() {

		return gson;
	}

	/**
	 * 对于需要对日期进行格式化的 Gson 对象进行缓存。
	 * 
	 * @param dateFormat
	 * @return
	 */
	protected static Gson getGsonByDateFormat(String dateFormat) {

		Gson gs = gsonByDateFormatCache.get(dateFormat);
		if (gs == null) {
			synchronized (gsonByDateFormatCache) {
				gs = gsonByDateFormatCache.get(dateFormat);
				if (gs == null) {
					gs = new GsonBuilder().setDateFormat(dateFormat).create();
					gsonByDateFormatCache.put(dateFormat, gs);
				}
			}
		}

		return gs;
	}

	/**
	 * 返回 Gson 对象（可以序列化 null）。
	 * 
	 * @return
	 */
	protected static Gson getGsonBySerializeNulls() {

		return gsonBySerializeNulls;
	}

	/**
	 * 返回 Gson 对象（格式化输出）。
	 * 
	 * @return
	 */
	protected static Gson getGsonByPrettyPrinting() {

		return gsonByPrettyPrinting;
	}

}
