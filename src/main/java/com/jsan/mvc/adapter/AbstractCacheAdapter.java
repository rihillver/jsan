package com.jsan.mvc.adapter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractCacheAdapter implements CacheAdapter {

	@Override
	public String getCacheKey(HttpServletRequest request, Method method, String methodValue, boolean session) {

		StringBuilder sb = new StringBuilder();

		sb.append(method);
		sb.append("#");

		sb.append(methodValue);
		sb.append("#");

		sb.append(request.getMethod());
		sb.append("#");

		Map<String, String[]> parameterMap = request.getParameterMap();

		// 其实不用判断是否为 null ，因为在 Tomcat 的 org.apache.catalina.connector.Request
		// 中是这样来的 protected ParameterMap<String, String[]> parameterMap = new
		// ParameterMap<>();

		// if (parameterMap != null && !parameterMap.isEmpty()) {

		if (!parameterMap.isEmpty()) {
			for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(Arrays.toString(entry.getValue()));
				sb.append(",");
			}
			sb.append("#");
		}

		// 通过取 parameterMap 的 hashCode
		// 这种方式效率较高，但是这种方式是存在一定的风险的，即所有键值不一定相同的 parameterMap 有可能出现相同的 hashCode
		// 值，不过概率很低。

		// sb.append(parameterMap.hashCode());
		// sb.append("#");

		if (session) {
			sb.append(request.getSession().getId());
		}

		return sb.toString();
	}
}
