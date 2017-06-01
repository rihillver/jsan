package com.jsan.spring;

import javax.servlet.FilterConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jsan.mvc.ControllerInfo;
import com.jsan.mvc.MethodInfo;
import com.jsan.mvc.filter.AbstractDispatcher;

/**
 * 基于 Spring IOC 的请求转发器。
 * <ul>
 * <li>在 Spring 配置中必须通过全限定类名作为所有控制器类的默认命名策略，避免在 Spring 容器中出现重名的风险。</li>
 * <li>该类使用了 Spring Ioc 管理控制器对象，请使用 Spring AOP 或 AspestJ
 * 替代控制器映射方法上注解方式的拦截器功能。</li>
 * </ul>
 *
 */
public class DispatcherFilter extends AbstractDispatcher {

	private ApplicationContext applicationContext;

	@Override
	protected void initCustom(FilterConfig config) {

		applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()); // 无论多少次的获取返回的都是相同的对象引用
	}

	/**
	 * 从 Spring IOC 容器中获取控制器对象。
	 * <p>
	 * 如果通过 Spring Ioc 容器的方式获取对象，当使用了 AOP 时返回的可能是代理对象。
	 * 
	 * @param cInfo
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Object getControllerObject(ControllerInfo cInfo, MethodInfo mInfo) throws Exception {

		return applicationContext.getBean(cInfo.getType().getName()); // 通过控制器的全限定类名进行查找;
	}
}
