package com.jsan.mvc.resolve.suport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.Resolver;
import com.jsan.mvc.resolve.annotation.RegisterName;

/**
 * 客户端转发解析器（有可能会对相对路径作相应处理，主要适用于站内链接转发）。
 * 
 */

@RegisterName(Resolver.REDIRECT)
public class RedirectResolver implements Resolver {

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String url = view.getUrl();

		String viewPath = mvcConfig.getViewPath();
		if (viewPath != null) { // 主要针对SimpleRestMappingAdapter适配器的处理，对于使用SimpleRestMappingAdapter的情况下，为了使视图解析更加正常（或更具备兼容性）一般不会将viewPath设置为null的，哪怕是设置为""也会设置的，设置为""时表示视图路径为WEB应用根目录下

			if (!url.startsWith("/")) {
				String uri = mappingInfo.getUri();
				uri = uri.substring(0, uri.lastIndexOf('/') + 1);
				// uri = uri.toLowerCase(); // 此处将 uri 转换为小写，客户端转发可不用转换为小写
				url = uri + url;
			}
		}

		response.sendRedirect(url);
	}

}
