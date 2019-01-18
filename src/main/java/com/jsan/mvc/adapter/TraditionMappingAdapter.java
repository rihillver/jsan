package com.jsan.mvc.adapter;

import javax.servlet.http.HttpServletRequest;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;

/**
 * 标准（一般）映射适配器。
 *
 */

public class TraditionMappingAdapter implements MappingAdapter {

	private static final String defaultClassName = "index";
	private static final String defaultMethodName = "index";

	@Override
	public MappingInfo getMappingInfo(MvcConfig config, HttpServletRequest request) {

		String uri = request.getRequestURI();

		String contextPath = config.getContextPath();
		if (contextPath != null && uri.startsWith(contextPath)) {
			uri = uri.substring(contextPath.length());
		}

		String suffix;
		int dotIndex = uri.indexOf('.'); // 以点（.）作为区分请求的后缀
		if (dotIndex > -1) {
			suffix = uri.substring(dotIndex); // 必须在前
			uri = uri.substring(0, dotIndex);
		} else {
			suffix = "";
		}

		if (uri.endsWith("/")) { // 如果以 "/" 结尾则加上默认方法
			uri += defaultClassName; // 默认类名
		}

		String methodValue = null;

		String methodDelimiter = config.getMethodDelimiter();
		if (methodDelimiter != null) {
			int delimiterIndex = uri.indexOf(methodDelimiter.charAt(0));
			if (delimiterIndex != -1) {
				methodValue = uri.substring(delimiterIndex + 1);
				uri = uri.substring(0, delimiterIndex);
			} else {
				methodValue = defaultMethodName; // 默认方法名
			}
		} else {
			methodValue = request.getParameter(config.getMethodKey());
			if (methodValue == null || methodValue.isEmpty()) {
				methodValue = defaultMethodName; // 默认方法名
			}
		}

		MappingInfo mappingInfo = new MappingInfo();
		mappingInfo.setUri(uri);
		mappingInfo.setSuffix(suffix);
		mappingInfo.setMethodValue(methodValue);

		return mappingInfo;
	}

}
