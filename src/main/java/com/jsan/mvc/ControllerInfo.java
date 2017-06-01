package com.jsan.mvc;

import java.util.Map;

import com.jsan.convert.ConvertService;
import com.jsan.mvc.intercept.InterceptService;

public class ControllerInfo {

	private Class<?> type;
	private Object object;
	private ConvertService convertService;
	private InterceptService interceptService;
	private Map<String, MethodInfo> methodInfoMap;

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
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

	public Map<String, MethodInfo> getMethodInfoMap() {
		return methodInfoMap;
	}

	public void setMethodInfoMap(Map<String, MethodInfo> methodInfoMap) {
		this.methodInfoMap = methodInfoMap;
	}

	/**
	 * 通过指定方法名和请求方法（POST/GET）在 methodInfoMap 缓存中查找对应的 MethodInfo。
	 * 
	 * @param methodValue
	 * @param requestMethod
	 * @return
	 */
	public MethodInfo getMethodInfo(String methodValue, String requestMethod) {

		MethodInfo methodInfo = null;

		if (methodInfoMap != null) {
			methodInfo = methodInfoMap.get(ControllerInfoCache.getMethodInfoMapKey(methodValue, requestMethod));
		}

		return methodInfo;
	}

}
