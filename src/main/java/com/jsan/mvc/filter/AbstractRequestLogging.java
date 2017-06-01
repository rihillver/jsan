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
import javax.servlet.http.HttpSession;

/**
 * 请求日志过滤器。
 * <p>
 * 通过子类实现抽象方法 beforeRequest() 和 afterRequest() 记录请求前后的日志。
 *
 */

public abstract class AbstractRequestLogging implements Filter {

	@Override
	public String toString() {

		return getClass().getName() + " []";
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		System.out.println("[mvc] " + toString());
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		beforeRequest(request);
		try {
			filterChain.doFilter(request, response);
		} finally {
			afterRequest(request);
		}
	}

	@Override
	public void destroy() {

	}

	protected abstract void beforeRequest(HttpServletRequest request);

	protected abstract void afterRequest(HttpServletRequest request);

	/**
	 * 返回真实 IP 地址，使通过 Apache ，Squid 等反向代理后仍能获得真实 IP 地址。
	 * 
	 * @param request
	 * @return
	 */
	protected String getRemoteIp(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if (ip != null) {
			int i = ip.indexOf(',');
			if (i != -1) {
				ip = ip.substring(0, i); // 如果不取逗号分隔的情况下可以获得一系列通过代理的IP
			}
		}

		return ip;
	}

	/**
	 * 生成基本的 request 信息
	 * 
	 * @param request
	 * @return
	 */
	protected String createMessage(HttpServletRequest request) {

		return createMessage(request, false);
	}

	/**
	 * 生成基本的 request 信息（可指定在反向代理的情况下正确获取 IP 的方法）。
	 * 
	 * @param request
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	protected String createMessage(HttpServletRequest request, boolean isReverseProxy) {

		StringBuilder msg = new StringBuilder();

		msg.append("uri=");
		msg.append(request.getRequestURI());

		msg.append('?');
		msg.append(request.getQueryString());

		msg.append(";client=");
		msg.append(isReverseProxy ? getRemoteIp(request) : request.getRemoteAddr());

		HttpSession session = request.getSession(false);
		if (session != null) {
			msg.append(";session=").append(session.getId());
		}

		return msg.toString();
	}

}
