package com.jsan.mvc;

/**
 * 请求 URI 映射信息。
 *
 */

public class MappingInfo {

	String uri;
	String suffix; // 后缀
	String methodValue; // 映射到控制器类上的方法名

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getMethodValue() {
		return methodValue;
	}

	public void setMethodValue(String methodValue) {
		this.methodValue = methodValue;
	}

}
