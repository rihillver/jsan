package com.jsan.mvc.adapter;

import javax.servlet.http.HttpServletRequest;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;

/**
 * 简单的轻度 REST 风格的映射适配器。
 * <p>
 * 该模式下 URL
 * 的结尾有反斜杠和没有反斜杠都映射到同一控制器上，因此这种模式要求视图页面上的所有链接（比如js地址、css地址、图片地址、url链接等）不能使用相对路径，应该使用绝对路径。
 * 
 * <ul>
 * <li>建议在 MVC 的配置上将 viewPath 设置以确保其不为 null，即使设置为 "" 这样也可以确保
 * ForwardResolver、RedirectResolver 可以正确的处理 view 上设置成相对路径的 url。</li>
 * <li>只要确保 MVC 的配置中 viewPath 不为 null，控制器上@Render 注解上的 url 同样可以使用相对路径，因为
 * ForwardResolver、RedirectResolver 解析器上会根据 viewPath 通过包的层次匹配到到对应的 url
 * 并组合成绝对路径。<br>
 * <li>任何情况下视图页面上的所有链接（比如js地址、css地址、图片地址、url链接等）都不能使用相对路径，只有使用绝对路径才能确保访问地址后缀有无反斜杠都能使页面上链接的准确链接到相应的资源。</li>
 * </ul>
 *
 */

public class SimpleRestMappingAdapter implements MappingAdapter {

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

		if (uri.endsWith("/")) { // 如果以 "/" 结尾则去掉结尾的 "/"
			if (uri.length() == 1) {
				uri += defaultMethodName; // 如果 uri 刚好是 "/" ，则加上默认方法名
			} else {
				uri = uri.substring(0, uri.length() - 1);
			}
		}

		String methodValue = null;

		// String methodDelimiter = config.getMethodDelimiter();
		// if (methodDelimiter != null) {
		// int delimiterIndex = uri.indexOf(methodDelimiter.charAt(0));
		// if (delimiterIndex != -1) {
		// methodValue = uri.substring(delimiterIndex + 1);
		// uri = uri.substring(0, delimiterIndex);
		// }else {
		// methodValue = defaultMethodName; // 默认方法名
		// }
		// } else {
		// methodValue = request.getParameter(config.getMethodKey());
		// if (methodValue == null || methodValue.isEmpty()) {
		// methodValue = defaultMethodName; // 默认方法名
		// }
		// }

		String methodDelimiter = "/";
		int delimiterIndex = uri.lastIndexOf(methodDelimiter.charAt(0));
		if (delimiterIndex > -1) {
			methodValue = uri.substring(delimiterIndex + 1);
			uri = uri.substring(0, delimiterIndex + 1) + defaultClassName; // 默认类名
		}

		MappingInfo mappingInfo = new MappingInfo();
		mappingInfo.setUri(uri);
		mappingInfo.setSuffix(suffix);
		mappingInfo.setMethodValue(methodValue);

		return mappingInfo;
	}

}
