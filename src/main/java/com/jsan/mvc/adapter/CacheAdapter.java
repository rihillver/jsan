package com.jsan.mvc.adapter;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.jsan.mvc.View;

/**
 * View 缓存适配器。
 *
 */

public interface CacheAdapter {

	void setCache(String cacheName, String cacheKey, View view);

	View getCache(String cacheName, String cacheKey);

	String getCacheKey(HttpServletRequest request, Method method, String methodValue, boolean session);

}
