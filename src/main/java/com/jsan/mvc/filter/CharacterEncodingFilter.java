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

import com.jsan.mvc.RequestConverter;

/**
 * 字符编码过滤器，如果同时设置了 fromEncoding 和 toEncoding 参数，则对 GET 请求方式进行字符转码。
 * <p>
 * <strong>对于统一使用 utf-8 编码的网站，不用对 GET 的请求做额外的编码处理（即不用配置 fromEncoding 和
 * toEncoding），否则反而适得其反。</strong>
 * <p>
 * Filter 配置参数：
 * <ul>
 * <li>encoding ： 字符编码。</li>
 * <li>forceEncoding ： 是否强制编码（默认值： false），如为 true 则将覆盖源字符编码，并且设置响应字符编码。</li>
 * <li>fromEncoding ： 源字符编码，仅对于 GET 请求方式。</li>
 * <li>toEncoding ： 目标字符编码，仅对于 GET 请求方式。</li>
 * <li>trim ： 是否去除请求参数值的前后空白（默认值： false）。</li>
 * </ul>
 *
 */

public class CharacterEncodingFilter implements Filter {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String encoding;
	protected boolean forceEncoding;
	protected String fromEncoding;
	protected String toEncoding;
	protected boolean trim;

	@Override
	public String toString() {
		return getClass().getName() + " [encoding=" + encoding + ", forceEncoding=" + forceEncoding + ", fromEncoding="
				+ fromEncoding + ", toEncoding=" + toEncoding + ", trim=" + trim + "]";
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		encoding = filterConfig.getInitParameter("encoding");
		fromEncoding = filterConfig.getInitParameter("fromEncoding");
		toEncoding = filterConfig.getInitParameter("toEncoding");

		String forceEncodingStr = filterConfig.getInitParameter("forceEncoding");
		if ("true".equalsIgnoreCase(forceEncodingStr)) {
			forceEncoding = true;
		}

		String trimStr = filterConfig.getInitParameter("trim");
		if ("true".equalsIgnoreCase(trimStr)) {
			trim = true;
		}

		logger.info("Initialization: {}", toString());
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (encoding != null && (forceEncoding || request.getCharacterEncoding() == null)) {
			request.setCharacterEncoding(encoding);
			if (forceEncoding) {
				response.setCharacterEncoding(encoding);
			}
		}

		if (trim) { // GET、POST均处理
			request = new RequestConverter(request, fromEncoding, toEncoding, true);
		} else if (fromEncoding != null && toEncoding != null && request.getMethod().equalsIgnoreCase("GET")) { // 只处理GET
			request = new RequestConverter(request, fromEncoding, toEncoding, false);
		}

		filterChain.doFilter(request, response);

	}

	@Override
	public void destroy() {

	}

}
