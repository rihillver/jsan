package com.jsan.mvc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求访问过滤器，一般情况下理应将该 Filter 设置在其他过滤器的前面。
 * <ul>
 * <li>参数 redirect 如果不为 null，则将被拒绝访问的请求将重定向到 redirect 值，否则根据 statusCode
 * 抛出状态消息。</li>
 * <li>参数 statusCode 默认值为 403，即默认抛出 403 状态消息，你也可以根据需要设置成 404 或者其他状态消息。</li>
 * <li>通过子类实现抽象方法 isPass() 来自行定义过滤规则。</li>
 * <li>一般用于访问权限过滤。</li>
 * </ul>
 *
 */

public abstract class AbstractRequestAccess implements Filter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String redirect;
	protected int statusCode = 403;

	@Override
	public String toString() {

		return getClass().getName() + " [redirect=" + redirect + ", statusCode=" + statusCode + "]";
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		redirect = filterConfig.getInitParameter("redirect");
		String statusCodeStr = filterConfig.getInitParameter("statusCode");
		if (statusCodeStr != null) {
			try {
				statusCode = Integer.parseInt(statusCodeStr);
			} catch (NumberFormatException e) {
				logger.error("Number parse failure", e);
				throw e;
			}
		}

		logger.info("Initialization: {}", toString());
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (isPass(request, response)) {
			filterChain.doFilter(request, response);
		} else {
			if (redirect == null) {
				response.sendError(statusCode);
			} else {
				response.sendRedirect(redirect);
				// request.getRequestDispatcher(redirect).forward(request,
				// response);
			}
		}
	}

	@Override
	public void destroy() {

	}

	public abstract boolean isPass(HttpServletRequest request, HttpServletResponse response);

}
