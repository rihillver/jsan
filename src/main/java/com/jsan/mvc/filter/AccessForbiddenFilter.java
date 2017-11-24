package com.jsan.mvc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 禁止访问过滤器，一般情况下理应将该 Filter 设置在其他过滤器的最前面。
 * 
 * <ul>
 * <li>参数 statusCode 默认值为 403，即默认抛出 403 状态消息，你也可以根据需要设置成 404 或者其他状态消息。</li>
 * <li>一般用于禁止某一类文件或者某一些目录的访问。</li>
 * </ul>
 *
 */

public class AccessForbiddenFilter implements Filter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected int statusCode = 403;

	@Override
	public String toString() {

		return getClass().getName() + " [statusCode=" + statusCode + "]";
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		String statusCodeStr = filterConfig.getInitParameter("statusCode");
		if (statusCodeStr != null) {
			try {
				statusCode = Integer.parseInt(statusCodeStr);
			} catch (NumberFormatException e) {
				logger.error("Failed to parse Integer: " + statusCodeStr, e);
				throw e;
			}
		}

		logger.info("Initialization: {}", toString());
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		((HttpServletResponse) servletResponse).sendError(statusCode);
	}

	@Override
	public void destroy() {

	}

}
