package com.jsan.mvc.resolve;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;

/**
 * 视图解析器接口。
 *
 */

public interface Resolver {

	String FORWARD = "forward";
	String REDIRECT = "redirect";
	String HTML = "html";
	String JSON = "json";
	String JSONP = "jsonp";
	String TEXT = "text";
	String BYTE = "byte";
	String STREAM = "stream";

	void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception;
}
