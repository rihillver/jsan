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
 * <li>默认会把 uri[mappingInfo.getUri()] 上对应的目录名转换为小写（因此 Web 应用目录里面的自建目录的名称原则上需要约定使用小写，否则在严格区分目录名大小写的系统上可能会导致映射路径无法匹配的情况）。</li>
 * <li>默认不会把 url[view.getUrl()] 上对应的目录名（如果设置有）和文件名转换为小写（因此如果设置 url 是含有目录路径原则上需要约定目录名使用小写，而文件名如果在显式设置 url 时也可根据实际情况使用小写或者大小写混合，而在不显式设置 url 时默认与其映射方法名大小写一致）。</li>
 * </ul>
 *
 */

@RegisterName(Resolver.FORWARD)
public class ForwardResolver extends AbstractResolver {

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		sendError(response, view.getErrorCode());

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

		// url = url.toLowerCase(); // 此处将url转换为小写，最后更新说明：不对url转换为小写，因为让目标文件与映射方法名一致更一目了然和方便理解

		// 当设置了视图路径时，则把视图路径添加到url的前面（通常用在以jsp作为视图时的情况下）
		String viewPath = mvcConfig.getViewPath();
		if (viewPath != null) {

			if (!url.startsWith("/")) {
				String uri = mappingInfo.getUri();
				uri = uri.substring(0, uri.lastIndexOf('/') + 1);
				uri = uri.toLowerCase(); // 此处将uri转换为小写，最后更新说明：这里对uri转换为小写又是必要的，因为这里的uri来自于客户端的请求uri，允许模糊匹配到对应的目录路径，因此我们需要形成一个约定，即Web应用目录里面的自建目录名称一律使用小写，不要使用大写或大小写混合的目录名称，避免在严格区分目录名大小写的系统上可能导致目录路径无法匹配的情况
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
