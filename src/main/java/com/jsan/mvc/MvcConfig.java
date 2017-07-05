package com.jsan.mvc;

import java.util.Set;

/**
 * MVC 配置信息。
 *
 */

public class MvcConfig {

	private boolean debug;
	private boolean multiton;
	private boolean cacheable;
	private boolean interceptable;

	private String viewPath;
	private String methodKey;
	private String configFile;
	private String viewSuffix; // 务必以反斜杠"/"开头
	private String contextPath; // 务必以反斜杠"/"开头
	private String executeTime;
	private String defaultRender;
	private String packagePrefix;
	private String ultimateMethod;
	private String methodDelimiter;
	private Set<String> mappingSuffix;

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isMultiton() {
		return multiton;
	}

	public void setMultiton(boolean multiton) {
		this.multiton = multiton;
	}

	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	public boolean isInterceptable() {
		return interceptable;
	}

	public void setInterceptable(boolean interceptable) {
		this.interceptable = interceptable;
	}

	public String getViewPath() {
		return viewPath;
	}

	public void setViewPath(String viewPath) {
		this.viewPath = viewPath;
	}

	public String getMethodKey() {
		return methodKey;
	}

	public void setMethodKey(String methodKey) {
		this.methodKey = methodKey;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getViewSuffix() {
		return viewSuffix;
	}

	public void setViewSuffix(String viewSuffix) {
		this.viewSuffix = viewSuffix;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	public String getDefaultRender() {
		return defaultRender;
	}

	public void setDefaultRender(String defaultRender) {
		this.defaultRender = defaultRender;
	}

	public String getPackagePrefix() {
		return packagePrefix;
	}

	public void setPackagePrefix(String packagePrefix) {
		this.packagePrefix = packagePrefix;
	}

	public String getUltimateMethod() {
		return ultimateMethod;
	}

	public void setUltimateMethod(String ultimateMethod) {
		this.ultimateMethod = ultimateMethod;
	}

	public String getMethodDelimiter() {
		return methodDelimiter;
	}

	public void setMethodDelimiter(String methodDelimiter) {
		this.methodDelimiter = methodDelimiter;
	}

	public Set<String> getMappingSuffix() {
		return mappingSuffix;
	}

	public void setMappingSuffix(Set<String> mappingSuffix) {
		this.mappingSuffix = mappingSuffix;
	}

	@Override
	public String toString() {
		return "MvcConfig [debug=" + debug + ", multiton=" + multiton + ", cacheable=" + cacheable + ", interceptable="
				+ interceptable + ", viewPath=" + viewPath + ", methodKey=" + methodKey + ", configFile=" + configFile
				+ ", viewSuffix=" + viewSuffix + ", contextPath=" + contextPath + ", executeTime=" + executeTime
				+ ", defaultRender=" + defaultRender + ", packagePrefix=" + packagePrefix + ", ultimateMethod="
				+ ultimateMethod + ", methodDelimiter=" + methodDelimiter + ", mappingSuffix=" + mappingSuffix + "]";
	}

}
