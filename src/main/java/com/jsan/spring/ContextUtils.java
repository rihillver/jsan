package com.jsan.spring;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Spring 的 ApplicationContext、BeanDefinitionRegistry 工具类。
 * <p>
 * 实现了 ApplicationContextAware 接口，用于在非 Spring 框架管理的类中获取 Spring 管理的类，以及获取
 * ApplicationContext 和 BeanDefinitionRegistry 等。
 * <p>
 * 需要在 Spring 配置文件中加入 &lt;bean
 * class="com.jsan.spring.ContextUtils"&gt;&lt;/bean&gt;
 * 
 */

public class ContextUtils implements ApplicationContextAware {

	private static ApplicationContext context;

	/**
	 * 不可实例化。
	 * 
	 */
	private ContextUtils() {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		context = applicationContext;
	}

	/**
	 * 返回 ApplicationContext。
	 * <p>
	 * 当没有启动 Web 容器的时候，则通过加载默认的配置文件路径 "/WEB-INF/applicationContext.xml"，便于在没有启动
	 * Web 容器的时候启动 Ioc 容器进行测试。
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {

		if (context == null) {
			// 此处可以只执行 new FileSystemXmlApplicationContext(getWebRootPath() +
			// XmlWebApplicationContext.DEFAULT_CONFIG_LOCATION);
			// 因为 setApplicationContext(ApplicationContext) 该方法设置的对象与 new
			// FileSystemXmlApplicationContext(String) 返回的对象是同一对象，并且该方法将在下面 new
			// FileSystemXmlApplicationContext(String) 赋值之前先执行
			new FileSystemXmlApplicationContext(getWebRootPath() + XmlWebApplicationContext.DEFAULT_CONFIG_LOCATION);
			// context = new FileSystemXmlApplicationContext(getWebRootPath() +
			// XmlWebApplicationContext.DEFAULT_CONFIG_LOCATION);
		}

		return context;
	}

	/**
	 * 返回 BeanFactory 。
	 * 
	 * @return
	 */
	public static BeanFactory getBeanFactory() {

		return getDefaultListableBeanFactory();
	}

	/**
	 * 返回 BeanDefinitionRegistry ，用于通过代码将 Bean 动态注册到 Spring IoC 容器。
	 * 
	 * @return
	 */
	public static BeanDefinitionRegistry getBeanDefinitionRegistry() {

		return getDefaultListableBeanFactory();
	}

	/**
	 * 返回 DefaultListableBeanFactory。
	 * 
	 * @return
	 */
	public static DefaultListableBeanFactory getDefaultListableBeanFactory() {

		return (DefaultListableBeanFactory) ((ConfigurableApplicationContext) getApplicationContext()).getBeanFactory();
	}

	/**
	 * 返回 Spring IOC 容器中的 Bean 列表。
	 * 
	 * @return
	 */
	public static String[] getBeanDefinitionNames() {

		return getApplicationContext().getBeanDefinitionNames();
	}

	/**
	 * 返回当前 Web 根目录路径。
	 * 
	 * @return
	 */
	private static String getWebRootPath() {

		URL url = ContextUtils.class.getResource("/");
		File file = new File(url.getPath());
		File webInfFile = file.getParentFile();
		File webRootFile = webInfFile.getParentFile();
		try {
			return webRootFile.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getBean(String name) {

		return getApplicationContext().getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {

		return getApplicationContext().getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {

		return getApplicationContext().getBean(name, requiredType);
	}

	public static Object getBean(String name, Object... args) {

		return getApplicationContext().getBean(name, args);
	}

	public static <T> T getBean(Class<T> requiredType, Object... args) {

		return getApplicationContext().getBean(requiredType, args);
	}

}
