package com.jsan.mvc.resolve.suport;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.AbstractResolver;
import com.jsan.mvc.resolve.Resolver;
import com.jsan.mvc.resolve.annotation.RegisterName;

/**
 * 服务端转发解析器。
 * <ul>
 * <li>根据点来判断视图文件的后缀名，因此请不要使用含有点的文件名（即仅允许后缀名前面使用点）。</li>
 * <li>当 url 没有后缀名，则加上默认视图后缀名。</li>
 * <li>当视图路径不为 null 时，则把视图路径加在 url 前面。</li>
 * <li>默认会把目录名及文件名转换为小写（原则上约定目录名应使用小写，文件名也最好也应使用小写）。</li>
 * </ul>
 *
 */

@RegisterName(Resolver.FORWARD)
public class ForwardResolver extends AbstractResolver {

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setStatusCode(response, view.getStatusCode());

		setContentType(response, view.getContentType(), "text/html");

		String url = view.getUrl();

		for (Map.Entry<String, Object> entry : view.getMap().entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue()); // 设置request属性
		}

		String viewSuffix = mvcConfig.getViewSuffix();

		if (url.indexOf('.') == -1) { // 由于是通过是否存在点来判断url是否含有后缀名，因此请不要在视图文件名中含有其他不必要的点（意味着除了后缀名前面的点以外不要存在其他的点）
			url += viewSuffix;
		}

		url = url.toLowerCase(); // 此处将url转换为小写

		// 当设置了视图路径时，则把视图路径添加到url的前面（通常用在以jsp作为视图时的情况下）
		String viewPath = mvcConfig.getViewPath();
		if (viewPath != null) {

			if (!url.startsWith("/")) {
				String uri = mappingInfo.getUri();
				uri = uri.substring(0, uri.lastIndexOf('/') + 1);
				uri = uri.toLowerCase(); // 此处将uri转换为小写
				url = uri + url;
			}

			if (viewPath.endsWith("/")) {
				viewPath = viewPath.substring(0, viewPath.length() - 1);
			}

			url = viewPath + url;
		}

		request.getRequestDispatcher(url).forward(request, response);

	}

}
