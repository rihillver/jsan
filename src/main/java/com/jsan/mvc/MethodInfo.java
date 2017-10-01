package com.jsan.mvc;

import java.lang.reflect.Method;

import com.jsan.convert.ConvertService;
import com.jsan.mvc.annotation.Cache;
import com.jsan.mvc.intercept.InterceptService;
import com.jsan.mvc.resolve.Resolver;

/**
 * 方法信息。
 *
 */

public class MethodInfo {

	private Object object; // 当使用了拦截器的时候，需要对每个方法上缓存对应拦截策略的控制器代理对象
	private Method method;
	private Cache cache;
	private String viewUrl;
	private Resolver viewResolver;
	private ConvertService convertService;
	private InterceptService interceptService;
	private ParameterInfo[] parameterInfos;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public Resolver getViewResolver() {
		return viewResolver;
	}

	public void setViewResolver(Resolver viewResolver) {
		this.viewResolver = viewResolver;
	}

	public ConvertService getConvertService() {
		return convertService;
	}

	public void setConvertService(ConvertService convertService) {
		this.convertService = convertService;
	}

	public InterceptService getInterceptService() {
		return interceptService;
	}

	public void setInterceptService(InterceptService interceptService) {
		this.interceptService = interceptService;
	}

	public ParameterInfo[] getParameterInfos() {
		return parameterInfos;
	}

	public void setParameterInfos(ParameterInfo[] parameterInfos) {
		this.parameterInfos = parameterInfos;
	}

}
