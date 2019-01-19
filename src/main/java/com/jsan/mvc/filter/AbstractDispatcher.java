package com.jsan.mvc.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsan.convert.BeanConvertUtils;
import com.jsan.convert.BeanProxyUtils;
import com.jsan.convert.ConvertFuncUtils;
import com.jsan.convert.ConvertService;
import com.jsan.convert.Converter;
import com.jsan.convert.Formatter;
import com.jsan.convert.GeneralConvertService;
import com.jsan.convert.Mold;
import com.jsan.convert.PropertiesConvertUtils;
import com.jsan.convert.annotation.ConvertServiceRegister;
import com.jsan.convert.annotation.ConverterRegister;
import com.jsan.convert.annotation.DateTimePattern;
import com.jsan.convert.annotation.NumberPattern;
import com.jsan.convert.cache.BeanConvertServiceCache;
import com.jsan.convert.cache.BeanConvertServiceContainer;
import com.jsan.convert.cache.BeanInformationCache;
import com.jsan.mvc.ControllerClassCache;
import com.jsan.mvc.ControllerInfo;
import com.jsan.mvc.ControllerInfoCache;
import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MethodInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.MvcFuncUtils;
import com.jsan.mvc.ParameterInfo;
import com.jsan.mvc.View;
import com.jsan.mvc.adapter.CacheAdapter;
import com.jsan.mvc.adapter.EhcacheCacheAdapter;
import com.jsan.mvc.adapter.MappingAdapter;
import com.jsan.mvc.adapter.StrictSimpleRestMappingAdapter;
import com.jsan.mvc.adapter.TraditionMappingAdapter;
import com.jsan.mvc.annotation.Cache;
import com.jsan.mvc.annotation.CookieObject;
import com.jsan.mvc.annotation.FormConvert;
import com.jsan.mvc.annotation.HeaderObject;
import com.jsan.mvc.annotation.MultiValue;
import com.jsan.mvc.annotation.ParamName;
import com.jsan.mvc.annotation.QuirkMode;
import com.jsan.mvc.annotation.Render;
import com.jsan.mvc.annotation.RequestObject;
import com.jsan.mvc.annotation.SessionObject;
import com.jsan.mvc.intercept.GeneralInterceptService;
import com.jsan.mvc.intercept.InterceptService;
import com.jsan.mvc.intercept.Interceptor;
import com.jsan.mvc.json.GeneralJsonParserConfigurator;
import com.jsan.mvc.json.GeneralJsonSerializeConfigurator;
import com.jsan.mvc.json.JsonParserConfigurator;
import com.jsan.mvc.json.JsonSerializeConfigurator;
import com.jsan.mvc.resolve.GeneralResolveService;
import com.jsan.mvc.resolve.ResolveService;
import com.jsan.mvc.resolve.Resolver;

/**
 * 抽象请求转发器。
 * <p>
 * 当映射无后缀的请求时，请求 URL 对应的 WEB 应用目录下不能有同名的文件夹，否则 WEB
 * 应用服务器（Apache、Tomcat）会将请求至该文件夹目录下而导致映射错误（例如请求 http://www.abc.com/news，如果 WEB
 * 根目录存在 news 文件夹，则实际请求路径将为 http://www.abc.com/news/，这可能不是我们原先想要的）。
 *
 */

public abstract class AbstractDispatcher implements Filter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected static final String DEFAULT_CONFIG_FILE = "/jsanmvc.properties"; // 默认配置文件

	protected FilterConfig filterConfig;

	protected final List<Class<? extends Resolver>> customResolverList = new ArrayList<Class<? extends Resolver>>(); // 多个Filter不共用
	protected final List<Class<? extends Converter>> customConverterList = new ArrayList<Class<? extends Converter>>(); // 多个Filter不共用
	protected final List<Class<? extends Formatter>> customFormatterList = new ArrayList<Class<? extends Formatter>>(); // 多个Filter不共用
	protected final List<Class<? extends Interceptor>> customInterceptorList = new ArrayList<Class<? extends Interceptor>>(); // 多个Filter不共用

	protected Class<? extends ConvertService> convertServiceClass;
	protected Class<? extends ResolveService> resolveServiceClass;
	protected Class<? extends InterceptService> interceptServiceClass;

	protected ResolveService resolveService;
	protected ConvertService convertService;
	protected InterceptService interceptService;

	protected MvcConfig mvcConfig;
	protected Properties configProperties;
	protected MappingAdapter mappingAdapter;
	protected CacheAdapter cacheAdapter;

	protected JsonSerializeConfigurator jsonSerializeConfigurator;
	protected JsonParserConfigurator jsonParserConfigurator;

	protected abstract void initCustom();

	/**
	 * 如果通过 Spring IOC 容器的方式获取对象，当使用了 AOP 时返回的可能是代理对象
	 */
	protected abstract Object getControllerObject(ControllerInfo controllerInfo, MethodInfo methodInfo)
			throws Exception;

	@Override
	public String toString() {
		return getClass().getName() + " [customResolverList=" + customResolverList + ", customConverterList="
				+ customConverterList + ", customFormatterList=" + customFormatterList + ", customInterceptorList="
				+ customInterceptorList + ", convertServiceClass=" + convertServiceClass + ", resolveServiceClass="
				+ resolveServiceClass + ", interceptServiceClass=" + interceptServiceClass + ", resolveService="
				+ resolveService + ", convertService=" + convertService + ", interceptService=" + interceptService
				+ ", mvcConfig=" + mvcConfig + ", configProperties=" + configProperties + ", mappingAdapter="
				+ mappingAdapter + ", cacheAdapter=" + cacheAdapter + ", jsonSerializeConfigurator="
				+ jsonSerializeConfigurator + ", jsonParserConfigurator=" + jsonParserConfigurator + "]";
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig config) throws ServletException {

		filterConfig = config;

		try {

			initConfig(); // 必须在最前
			initConfigProperties(); // 必须在第二

			// 后面的初始化依赖于前两个
			initStrategies();

			initCustom(); // 初始化子类自定义的配置

		} catch (Exception e) {
			logger.error("Initialization failed", e);
			throw new ServletException(e);
		}

		logger.info("Initialization: {}", toString());
	}

	protected void initConfig() {

		mvcConfig = new MvcConfig();

		Field[] fields = MvcConfig.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String parameter = filterConfig.getInitParameter(field.getName());
			if (parameter != null) {
				try {
					if (field.getType() == boolean.class) {
						field.set(mvcConfig, Boolean.valueOf(parameter));
					} else if (field.getType() == String.class) {
						field.set(mvcConfig, parameter);
					} else if (field.getType() == Set.class) { // 对于mappingSuffix的处理
						String[] strs = parameter.split(",");
						Set<String> set = new HashSet<String>();
						for (String str : strs) {
							set.add(str);
						}
						field.set(mvcConfig, set);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		// 设置缺省配置
		if (mvcConfig.getMethodKey() == null) {
			mvcConfig.setMethodKey("mod");
		}
		if (mvcConfig.getViewSuffix() == null) {
			mvcConfig.setViewSuffix(".jsp");
		}
		if (mvcConfig.getDefaultRender() == null) {
			mvcConfig.setDefaultRender("forward");
		}
		if (mvcConfig.getPackagePrefix() == null) {
			mvcConfig.setPackagePrefix("root");
		}
		if (mvcConfig.getUltimateMethod() == null) {
			mvcConfig.setUltimateMethod("ultimate");
		}
		if (mvcConfig.getMappingSuffix() == null) { // 如果映射后缀没有设置，则默认无后缀映射
			Set<String> set = new HashSet<String>();
			set.add("");
			mvcConfig.setMappingSuffix(set);
		}

	}

	protected void initConfigProperties() {

		String configFile = mvcConfig.getConfigFile();

		if (configFile != null && !configFile.isEmpty()) {
			try {
				configProperties = PropertiesConvertUtils.loadProperties(configFile);
			} catch (IOException e) {
				logger.error("Cannot load the custom configuration file: {}", configFile);
				throw new RuntimeException(e);
			}
		} else {
			try {
				configProperties = PropertiesConvertUtils.loadProperties(DEFAULT_CONFIG_FILE);// 寻找默认配置文件
			} catch (IOException e) {
				logger.warn("Cannot load the default configuration file: {}", DEFAULT_CONFIG_FILE);
			}
		}
	}

	protected void initStrategies() {

		initMappingAdapter();
		initCacheAdapter();

		initJsonSerializeConfigurator();
		initJsonParserConfigurator();

		initCustomResolver(); // 必须在初始化ResolveService之前
		initCustomConverter(); // 必须在初始化ConvertService之前
		initCustomFormatter(); // 必须在初始化ConvertService之前
		initCustomInterceptor(); // 必须在初始化InterceptService之前

		initResolveService();
		initConvertService();
		initInterceptService();
		initControllerCache(mvcConfig.getPackagePrefix());
	}

	protected void initControllerCache(String packagePrefix) {

		ControllerClassCache.init(packagePrefix);
	}

	protected void initMappingAdapter() {

		mappingAdapter = (MappingAdapter) ConvertFuncUtils.getInstanceByProperties(configProperties,
				MappingAdapter.class.getName());

		if (mappingAdapter == null) {
			if ("/".equals(mvcConfig.getMethodDelimiter())) { // 斜杠作为方法分割符的情况下使用严谨映射的轻度REST风格的内置适配器
				mappingAdapter = new StrictSimpleRestMappingAdapter();
			} else {
				mappingAdapter = new TraditionMappingAdapter();
			}
		}
	}

	protected void initCacheAdapter() {

		if (mvcConfig.isCacheable()) {
			cacheAdapter = (CacheAdapter) ConvertFuncUtils.getInstanceByProperties(configProperties,
					CacheAdapter.class.getName());
			if (cacheAdapter == null) {
				cacheAdapter = new EhcacheCacheAdapter();
			}
		}

	}

	protected void initJsonSerializeConfigurator() {

		jsonSerializeConfigurator = (JsonSerializeConfigurator) ConvertFuncUtils
				.getInstanceByProperties(configProperties, JsonSerializeConfigurator.class.getName());
		if (jsonSerializeConfigurator == null) {
			jsonSerializeConfigurator = new GeneralJsonSerializeConfigurator();
		}
	}

	protected void initJsonParserConfigurator() {

		jsonParserConfigurator = (JsonParserConfigurator) ConvertFuncUtils.getInstanceByProperties(configProperties,
				JsonParserConfigurator.class.getName());
		if (jsonParserConfigurator == null) {
			jsonParserConfigurator = new GeneralJsonParserConfigurator();
		}
	}

	protected void initCustomResolver() {

		fillListByCustom(Resolver.class);
	}

	protected void initCustomConverter() {

		fillListByCustom(Converter.class);
	}

	protected void initCustomFormatter() {

		fillListByCustom(Formatter.class);
	}

	protected void initCustomInterceptor() {

		fillListByCustom(Interceptor.class);
	}

	@SuppressWarnings("unchecked")
	protected void fillListByCustom(Class<?> type) {

		String[] customs = ConvertFuncUtils.getStringArrayByProperties(configProperties, type.getName());

		if (customs != null) {
			for (String className : customs) {
				Class<?> clazz;
				try {
					clazz = Class.forName(className);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (type == Resolver.class) {
					customResolverList.add((Class<? extends Resolver>) clazz);
				} else if (type == Converter.class) {
					customConverterList.add((Class<? extends Converter>) clazz);
				} else if (type == Formatter.class) {
					customFormatterList.add((Class<? extends Formatter>) clazz);
				} else if (type == Interceptor.class) {
					customInterceptorList.add((Class<? extends Interceptor>) clazz);
				}
			}
		}
	}

	protected Class<?> getControllerClass(String qualifiedClassName) {

		return ControllerClassCache.get(qualifiedClassName);
	}

	protected ControllerInfo getControllerInfo(Class<?> controllerClass) {

		return ControllerInfoCache.get(controllerClass);
	}

	protected void initInterceptService() {

		interceptServiceClass = createInterceptServiceClass();
		interceptService = createInterceptService();
	}

	@SuppressWarnings("unchecked")
	private Class<? extends InterceptService> createInterceptServiceClass() {

		Class<? extends InterceptService> interceptServiceClass = (Class<? extends InterceptService>) ConvertFuncUtils
				.getClassByProperties(configProperties, InterceptService.class.getName());

		if (interceptServiceClass == null) {
			interceptServiceClass = GeneralInterceptService.class;
		}

		return interceptServiceClass;
	}

	protected InterceptService createInterceptService() {

		InterceptService service = newInstance(interceptServiceClass);

		for (Class<? extends Interceptor> interceptorClass : customInterceptorList) {
			service.addInterceptor(interceptorClass);
		}

		return service;
	}

	protected InterceptService getInterceptService() {

		return interceptService;
	}

	protected void initResolveService() {

		resolveServiceClass = createResolveServiceClass();
		resolveService = createResolveService();
	}

	@SuppressWarnings("unchecked")
	private Class<? extends ResolveService> createResolveServiceClass() {

		Class<? extends ResolveService> resolveServiceClass = (Class<? extends ResolveService>) ConvertFuncUtils
				.getClassByProperties(configProperties, ResolveService.class.getName());

		if (resolveServiceClass == null) {
			resolveServiceClass = GeneralResolveService.class;
		}

		return resolveServiceClass;
	}

	protected ResolveService createResolveService() {

		ResolveService service = newInstance(resolveServiceClass);

		for (Class<? extends Resolver> resolverClass : customResolverList) {
			service.registerResolver(resolverClass);
		}
		return service;
	}

	protected ResolveService getResolveService() {

		return resolveService;
	}

	protected void initConvertService() {

		convertServiceClass = createConvertServiceClass(); // 通过配置文件载入默认ConvertService类
		convertService = createConvertService();
	}

	protected ConvertService getConvertService() {

		return convertService;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends ConvertService> createConvertServiceClass() {

		Class<? extends ConvertService> convertServiceClass = (Class<? extends ConvertService>) ConvertFuncUtils
				.getClassByProperties(configProperties, ConvertService.class.getName());

		if (convertServiceClass == null) {
			convertServiceClass = GeneralConvertService.class;
		}

		return convertServiceClass;
	}

	protected ConvertService createConvertService() {

		ConvertService service = newInstance(convertServiceClass);

		for (Class<? extends Converter> converterClass : customConverterList) {
			service.registerConverter(converterClass);
		}

		for (Class<? extends Formatter> formatterClass : customFormatterList) {
			service.declareFormatterClass(formatterClass);
		}

		return service;
	}

	protected <T> T newInstance(Class<T> clazz) {

		try {
			return BeanProxyUtils.newInstance(clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected JsonParserConfigurator getJsonParserConfigurator(ParameterInfo pInfo) {

		JsonParserConfigurator configurator = pInfo.getJsonParserConfigurator();

		if (configurator == null) {
			synchronized (pInfo) {
				configurator = pInfo.getJsonParserConfigurator();
				if (configurator == null) {
					Class<? extends JsonParserConfigurator> configuratorClass = pInfo.getJsonConvert().value(); // 这里不用判断JsonConvert是否为null，因为该方法被调用的情况下JsonConvert是一定不为null的
					if (configuratorClass == JsonParserConfigurator.class) {
						configurator = getJsonParserConfigurator();
					} else {
						configurator = newInstance(configuratorClass);
					}
					pInfo.setJsonParserConfigurator(configurator);
				}
			}
		}

		return configurator;
	}

	protected ConvertService getParameterConvertService(ControllerInfo cInfo, MethodInfo mInfo, ParameterInfo pInfo) {

		ConvertService service = pInfo.getConvertService();

		if (service == null) {
			synchronized (pInfo) {
				service = pInfo.getConvertService();
				if (service == null) {
					if (pInfo.getConvertServiceRegister() != null || pInfo.getConverterRegister() != null
							|| pInfo.getDateTimePattern() != null || pInfo.getNumberPattern() != null) {
						service = createParameterConvertService(cInfo, mInfo, pInfo);
					} else {
						service = getMethodConvertService(cInfo, mInfo); // 获取定义在方法上的转换服务
					}
					pInfo.setConvertService(service);
				}
			}
		}

		return service;
	}

	protected ConvertService createParameterConvertService(ControllerInfo cInfo, MethodInfo mInfo,
			ParameterInfo pInfo) {

		ConvertServiceRegister register = pInfo.getConvertServiceRegister();

		ConvertService service;
		if (register != null) {
			service = newInstance(register.value());
		} else {
			service = getMethodConvertService(cInfo, mInfo).clone();
		}

		ConvertFuncUtils.registerConverterAndFormatter(service, pInfo.getConverterRegister(),
				pInfo.getDateTimePattern(), pInfo.getNumberPattern());

		return service;
	}

	protected ConvertService getMethodConvertService(ControllerInfo cInfo, MethodInfo mInfo) {

		ConvertService service = mInfo.getConvertService();

		if (service == null) {
			synchronized (mInfo) {
				service = mInfo.getConvertService();
				if (service == null) {
					Method method = mInfo.getMethod();
					if (method.isAnnotationPresent(ConvertServiceRegister.class)
							|| method.isAnnotationPresent(ConverterRegister.class)
							|| method.isAnnotationPresent(DateTimePattern.class)
							|| method.isAnnotationPresent(NumberPattern.class)) {
						service = createMethodConvertService(cInfo, mInfo);
					} else {
						service = getTypeConvertService(cInfo); // 获取定义在类上的转换服务
					}
					mInfo.setConvertService(service);
				}
			}
		}

		return service;
	}

	protected ConvertService createMethodConvertService(ControllerInfo cInfo, MethodInfo mInfo) {

		Method method = mInfo.getMethod();
		ConvertServiceRegister register = method.getAnnotation(ConvertServiceRegister.class);

		ConvertService service;
		if (register != null) {
			service = newInstance(register.value());
		} else {
			service = getTypeConvertService(cInfo).clone();
		}

		ConvertFuncUtils.registerConverterAndFormatter(service, method.getAnnotation(ConverterRegister.class),
				method.getAnnotation(DateTimePattern.class), method.getAnnotation(NumberPattern.class));

		return service;
	}

	protected ConvertService getTypeConvertService(ControllerInfo cInfo) {

		ConvertService service = cInfo.getConvertService();

		if (service == null) {
			synchronized (cInfo) {
				service = cInfo.getConvertService();
				if (service == null) {
					Class<?> type = cInfo.getType();
					if (type.isAnnotationPresent(ConvertServiceRegister.class)
							|| type.isAnnotationPresent(ConverterRegister.class)
							|| type.isAnnotationPresent(DateTimePattern.class)
							|| type.isAnnotationPresent(NumberPattern.class)) {
						service = createTypeConvertService(cInfo);
					} else {
						service = getConvertService(); // 获取定义在DispatcherFilter上的转换服务
					}
					cInfo.setConvertService(service);
				}
			}
		}

		return service;
	}

	protected ConvertService createTypeConvertService(ControllerInfo cInfo) {

		Class<?> type = cInfo.getType();
		ConvertServiceRegister register = type.getAnnotation(ConvertServiceRegister.class);

		ConvertService service;
		if (register != null) {
			service = newInstance(register.value());
		} else {
			service = getConvertService().clone();
		}

		ConvertFuncUtils.registerConverterAndFormatter(service, type.getAnnotation(ConverterRegister.class),
				type.getAnnotation(DateTimePattern.class), type.getAnnotation(NumberPattern.class));

		return service;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		try {
			doDispatcher(request, response, filterChain);
		} catch (Exception e) {
			logger.error("Exception capture", e);
			throw new ServletException(e);
		}

	}

	protected void doDispatcher(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws Exception {

		long startTime = System.nanoTime();

		MappingInfo mappingInfo = getMappingInfo(request);
		if (mvcConfig.getMappingSuffix().contains(mappingInfo.getSuffix().toLowerCase())) { // 映射后缀在此转换为小写

			ControllerInfo cInfo = getControllerInfo(mvcConfig.getPackagePrefix(), mappingInfo.getUri());
			if (cInfo != null) {

				MethodInfo mInfo = getMethodInfo(mappingInfo, cInfo, request);
				if (mInfo != null) {

					View view = getView(mappingInfo, cInfo, mInfo, request, response);

					setExecuteTime(view, startTime);

					resolveView(view, mappingInfo, mInfo, request, response);

				} else {
					chain.doFilter(request, response);
				}
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * 如果 MvcConfig 配置了执行耗时的键名，则在 View
	 * 中请勿再使用与此键名相同的作为传递请求响应的参数名，因为该参数值始终将会在此被设置成执行耗时。
	 * 
	 * @param view
	 * @param startTime
	 */
	protected void setExecuteTime(View view, long startTime) {

		String executeTime = mvcConfig.getExecuteTime();
		if (executeTime != null) {
			double usedTime = (System.nanoTime() - startTime) / 1000000.0;
			view.add(executeTime, usedTime);
		}
	}

	protected MethodInfo getMethodInfo(MappingInfo mappingInfo, ControllerInfo cInfo, HttpServletRequest request) {

		MethodInfo mInfo = cInfo.getMethodInfo(mappingInfo.getMethodValue(), request.getMethod());

		if (mInfo == null) {
			mInfo = cInfo.getMethodInfo(mvcConfig.getUltimateMethod(), request.getMethod());
		}

		return mInfo;
	}

	protected MappingInfo getMappingInfo(HttpServletRequest request) {

		return mappingAdapter.getMappingInfo(mvcConfig, request);
	}

	/**
	 * View 的 url 值若为 null 则抛出404错误。
	 * 
	 * @param view
	 * @param mappingInfo
	 * @param mInfo
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void resolveView(View view, MappingInfo mappingInfo, MethodInfo mInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (!response.isCommitted()) { // 判断是否已将数据输出到客户端，如果已输出则无必要再执行视图解析
			if (view.getUrl() != null) {
				view.getResolver().execute(view, mvcConfig, mappingInfo, request, response);
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI()); // url为null则抛出404错误
			}
		}
	}

	protected View getView(MappingInfo mappingInfo, ControllerInfo cInfo, MethodInfo mInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (mvcConfig.isCacheable()) {
			Cache cache = getCacheAnnotation(cInfo, mInfo);
			if (cache.value().isEmpty()) {
				return getViewCreate(mappingInfo, cInfo, mInfo, request, response);
			} else {
				return getViewCache(cache, mappingInfo, cInfo, mInfo, request, response);
			}
		} else {
			return getViewCreate(mappingInfo, cInfo, mInfo, request, response);
		}
	}

	protected Cache getCacheAnnotation(ControllerInfo cInfo, MethodInfo mInfo) {

		Cache cache = mInfo.getCache();

		if (cache == null) {
			synchronized (mInfo) {
				cache = mInfo.getCache();
				if (cache == null) {
					cache = mInfo.getMethod().getAnnotation(Cache.class); // 从方法上寻找
					if (cache == null) {
						cache = cInfo.getType().getAnnotation(Cache.class); // 从类上寻找
						if (cache == null) {
							cache = Cache.class.getAnnotation(Cache.class); // 如果都不存在@Cache注解，则定义一个空的@Cache放入mInfo中去
						}
					}
					mInfo.setCache(cache);
				}
			}
		}

		return cache;
	}

	protected boolean isParameterQuirkMode(ControllerInfo cInfo, MethodInfo mInfo, ParameterInfo pInfo) {

		QuirkMode quirkMode = pInfo.getQuirkMode();

		if (quirkMode == null) {
			synchronized (pInfo) {
				quirkMode = pInfo.getQuirkMode();
				if (quirkMode == null) {
					quirkMode = mInfo.getMethod().getAnnotation(QuirkMode.class); // 从方法上寻找
					if (quirkMode == null) {
						quirkMode = cInfo.getType().getAnnotation(QuirkMode.class); // 从类上寻找
						if (quirkMode == null) {
							quirkMode = QuirkMode.class.getAnnotation(QuirkMode.class); // 如果都不存在@QuirkMode注解，则定义一个默认值为false的@QuirkMode放入pInfo中去
						}
					}
					pInfo.setQuirkMode(quirkMode);
				}
			}
		}

		return quirkMode.value();
	}

	protected View getViewCache(Cache cache, MappingInfo mappingInfo, ControllerInfo cInfo, MethodInfo mInfo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String cacheName = cache.value();
		String cacheKey = cacheAdapter.getCacheKey(request, mInfo.getMethod(), mappingInfo.getMethodValue(),
				cache.session());

		View view = cacheAdapter.getCache(cacheName, cacheKey);

		if (view == null) {
			view = getViewCreate(mappingInfo, cInfo, mInfo, request, response);
			cacheAdapter.setCache(cacheName, cacheKey, view);
		}

		return view;
	}

	/**
	 * 不允许返回值为 NULL 的 View 对象。
	 * 
	 * @param mappingInfo
	 * @param cInfo
	 * @param mInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected View getViewCreate(MappingInfo mappingInfo, ControllerInfo cInfo, MethodInfo mInfo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		View view = getViewInitialized(mInfo);
		Object returnValue = getReturnValue(view, mappingInfo, cInfo, mInfo, request, response);

		Class<?> returnType = mInfo.getMethod().getReturnType();

		if (returnType != Void.TYPE) { // 此处必须判断返回值是否为void，因为在void的情况下，很可能是在方法内部直接用view来设置值
			if (returnValue instanceof View) { // 如果返回值为View，那么这个View可能为方法内部自己new出来的，这种情况需要补充以下设置
				view = (View) returnValue;
				String url = view.getUrl();
				if (url != null && url.isEmpty()) { // 需要判断url是否为空，若为空则需在此设置上去，如果url为null，则意味着在方法内人为设置上去的，url为null则表示抛出404错误
					view.setUrl(getViewUrl(mInfo));
				}
				if (view.getResolver() == null) { // 需要判断resolver是否为null，如果为null则在此设置上去，url则不能在此加上去
					view.setResolver(getViewResolver(mInfo));
				}
				if (view.getJsonSerializeConfigurator() == null) { // 需要判断JsonSerializeConfigurator是否为null，如果为null则在此设置上去，View上的JsonSerializeConfigurator不能为null
					view.setJsonSerializeConfigurator(getJsonSerializeConfigurator());
				}
			} else {
				view.addData(returnValue);
			}
		}

		return view;
	}

	protected View getViewInitialized(MethodInfo mInfo) throws Exception {

		View view = new View();

		view.setUrl(getViewUrl(mInfo));
		view.setResolver(getViewResolver(mInfo));
		view.setJsonSerializeConfigurator(getJsonSerializeConfigurator());

		return view;
	}

	protected String getViewUrl(MethodInfo mInfo) {

		String url = mInfo.getViewUrl();

		if (url == null) {
			synchronized (mInfo) {
				url = mInfo.getViewUrl();
				if (url == null) {
					Method method = mInfo.getMethod();
					Render render = method.getAnnotation(Render.class);
					url = render.url();
					if (url.isEmpty()) {
						url = method.getName(); // 如果url为空，则取其方法名，方法内部运行时可将url设置为null
					}
					mInfo.setViewUrl(url);
				}
			}
		}

		return url;
	}

	protected Resolver getViewResolver(MethodInfo mInfo) throws Exception {

		Resolver resolver = mInfo.getViewResolver();

		if (resolver == null) {
			synchronized (mInfo) {
				resolver = mInfo.getViewResolver();
				if (resolver == null) {
					Render render = mInfo.getMethod().getAnnotation(Render.class);
					Class<? extends Resolver> clazz = render.resolver();
					if (clazz != Resolver.class) {
						resolver = BeanProxyUtils.newInstance(clazz);
					} else {
						String resolverName = render.value();
						if (resolverName.isEmpty()) {
							resolverName = mvcConfig.getDefaultRender(); // 设置为DispatcherFilter配置上定义的默认视图解析器名
						}
						resolver = getResolveService().lookupResolver(resolverName);
					}

					mInfo.setViewResolver(resolver);
				}
			}
		}

		return resolver;
	}

	protected Object getReturnValue(View view, MappingInfo mappingInfo, ControllerInfo cInfo, MethodInfo mInfo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Method method = mInfo.getMethod();
		Object controller = getControllerObject(cInfo, mInfo);
		Object[] parameterObjects = getParameterObjects(view, mappingInfo, cInfo, mInfo, request, response);

		return method.invoke(controller, parameterObjects);
	}

	protected Object[] getParameterObjects(View view, MappingInfo mappingInfo, ControllerInfo cInfo, MethodInfo mInfo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		StringBuilder message = logger.isDebugEnabled() ? new StringBuilder() : null;

		ParameterInfo[] parameterInfos = mInfo.getParameterInfos();
		int length = parameterInfos.length;
		Object[] parameterObjects = new Object[length];

		for (int i = 0; i < length; i++) {

			ParameterInfo pInfo = parameterInfos[i];
			Class<?> type = pInfo.getType();
			Type genericType = pInfo.getGenericType();
			boolean parameterQuirkMode = isParameterQuirkMode(cInfo, mInfo, pInfo);
			boolean standardConvertFlag = false;

			if (View.class.isAssignableFrom(type)) {
				parameterObjects[i] = view;
			} else if (HttpServletRequest.class.isAssignableFrom(type)) {
				parameterObjects[i] = request;
			} else if (HttpServletResponse.class.isAssignableFrom(type)) {
				parameterObjects[i] = response;
			} else if (HttpSession.class.isAssignableFrom(type)) {
				parameterObjects[i] = request.getSession();
			} else {

				ConvertService service = getParameterConvertService(cInfo, mInfo, pInfo);

				if (pInfo.getFormConvert() != null) {
					if (Map.class.isAssignableFrom(type)) { // 表单转Map处理
						parameterQuirkMode = false; // 表单转Map不对@QuirkMode做处理，@QuirkMode只针对表单转 Bean
						parameterObjects[i] = getRequestFormToMap(service, pInfo, parameterQuirkMode, request);
					} else { // 表单转Bean处理
						parameterObjects[i] = getRequestFormToBean(service, pInfo, parameterQuirkMode, request);
					}
				} else if (pInfo.getJsonConvert() != null) { // Request Payload形式的字符串（使用场景中多数情况为json字符串）转Object处理
					parameterObjects[i] = getRequestJsonToObject(pInfo, parameterQuirkMode, request);
				} else if (pInfo.getRequestObject() != null) { // request属性对象处理
					parameterObjects[i] = getRequestObject(service, pInfo, request);
				} else if (pInfo.getSessionObject() != null) { // session属性对象处理
					parameterObjects[i] = getSessionObject(service, pInfo, request);
				} else if (pInfo.getHeaderObject() != null) { // header值处理
					parameterObjects[i] = getHeaderObject(service, pInfo, request);
				} else if (pInfo.getCookieObject() != null) { // cookie值处理
					parameterObjects[i] = getCookieObject(service, pInfo, request);
				} else {
					standardConvertFlag = true;
					Object parameterValue;
					if (pInfo.getMethodValue() != null) {
						parameterValue = mappingInfo.getMethodValue();
					} else {
						if (pInfo.getMultiValue() != null) { // 是否为指定多值的参数
							parameterValue = getRequestParameterValues(pInfo, parameterQuirkMode, request);
						} else {
							parameterValue = getRequestParameterValue(pInfo, parameterQuirkMode, request);
						}
					}
					Converter converter = service.lookupConverter(type);
					parameterObjects[i] = converter.convert(parameterValue, genericType);
				}
			}

			if (message != null) {
				String parameterName = pInfo.getName();
				if (i == 0) {
					message.append("\n");
				}
				message.append("----- ");
				message.append("(");
				message.append(type.getCanonicalName());
				message.append(") ");
				message.append(parameterName);
				if (standardConvertFlag) {
					if (pInfo.getParamName() != null) {
						message.append(" [");
						message.append(pInfo.getParamName().value());
						message.append("]");
					} else if (parameterQuirkMode) {
						if (request.getParameterValues(parameterName) == null) {
							String originalParameterName = ConvertFuncUtils.parseCamelCaseToSnakeCase(parameterName);
							if (request.getParameterValues(originalParameterName) != null) {
								message.append(" [");
								message.append(originalParameterName);
								message.append("]");
							}
						}
					}
				}
				message.append(" : ");
				if (type.isArray()) {
					String arrayStr;
					if (type == int[].class) {
						arrayStr = Arrays.toString((int[]) parameterObjects[i]);
					} else if (type == long[].class) {
						arrayStr = Arrays.toString((long[]) parameterObjects[i]);
					} else if (type == double[].class) {
						arrayStr = Arrays.toString((double[]) parameterObjects[i]);
					} else if (type == float[].class) {
						arrayStr = Arrays.toString((float[]) parameterObjects[i]);
					} else if (type == boolean[].class) {
						arrayStr = Arrays.toString((boolean[]) parameterObjects[i]);
					} else if (type == char[].class) {
						arrayStr = Arrays.toString((char[]) parameterObjects[i]);
					} else if (type == short[].class) {
						arrayStr = Arrays.toString((short[]) parameterObjects[i]);
					} else if (type == byte[].class) {
						arrayStr = Arrays.toString((byte[]) parameterObjects[i]);
					} else {
						arrayStr = Arrays.deepToString((Object[]) parameterObjects[i]);
					}
					message.append(arrayStr);
				} else {
					message.append(parameterObjects[i]);
				}
				if (i < length - 1) {
					message.append("\n");
				}
			}
		}

		if (message != null) {
			String methodStr = mInfo.getMethod().toGenericString();
			methodStr = methodStr.substring(0, methodStr.indexOf('('));
			message.insert(0, "()");
			message.insert(0, methodStr);
			message.insert(0, "] ");
			message.insert(0, request.getMethod());
			message.insert(0, "[");
			message.insert(0, "invoke -- ");
			logger.debug(message.toString());
		}

		return parameterObjects;
	}

	protected Object getHeaderObject(ConvertService service, ParameterInfo pInfo, HttpServletRequest request) {

		HeaderObject headerObject = pInfo.getHeaderObject();
		String key = headerObject.value();
		if (key.isEmpty()) {
			key = pInfo.getName();
		}
		Object value = request.getHeader(key);
		if (pInfo.getType() != String.class) {
			Converter converter = service.lookupConverter(pInfo.getType());
			value = converter.convert(value, pInfo.getGenericType());
		}

		return value;
	}

	protected Object getCookieObject(ConvertService service, ParameterInfo pInfo, HttpServletRequest request) {

		CookieObject cookieObject = pInfo.getCookieObject();
		String key = cookieObject.value();
		if (key.isEmpty()) {
			key = pInfo.getName();
		}
		Object value = MvcFuncUtils.getCookieValue(request, key);
		if (pInfo.getType() != String.class) {
			Converter converter = service.lookupConverter(pInfo.getType());
			value = converter.convert(value, pInfo.getGenericType());
		}

		return value;
	}

	protected Object getRequestObject(ConvertService service, ParameterInfo pInfo, HttpServletRequest request) {

		RequestObject requestObject = pInfo.getRequestObject();
		String attributeName = requestObject.value();
		if (attributeName.isEmpty()) {
			attributeName = pInfo.getName();
		}
		Object object = request.getAttribute(attributeName);
		if (pInfo.getType().isPrimitive()) {
			Converter converter = service.lookupConverter(pInfo.getType());
			object = converter.convert(object, pInfo.getGenericType());
		}

		return object;
	}

	protected Object getSessionObject(ConvertService service, ParameterInfo pInfo, HttpServletRequest request) {

		SessionObject sessionObject = pInfo.getSessionObject();
		String attributeName = sessionObject.value();
		if (attributeName.isEmpty()) {
			attributeName = pInfo.getName();
		}
		Object object = request.getSession().getAttribute(attributeName);
		if (pInfo.getType().isPrimitive()) {
			Converter converter = service.lookupConverter(pInfo.getType());
			object = converter.convert(object, pInfo.getGenericType());
		}

		return object;
	}

	protected String[] getRequestParameterValues(ParameterInfo pInfo, boolean parameterQuirkMode,
			HttpServletRequest request) {

		String requestParameterName = getRequestParameterName(pInfo, false);
		String[] parameterValue = request.getParameterValues(requestParameterName);
		if (parameterValue == null && parameterQuirkMode && pInfo.getParamName() == null) {
			parameterValue = request.getParameterValues(getRequestParameterName(pInfo, true));
		}

		return parameterValue;
	}

	protected String getRequestParameterValue(ParameterInfo pInfo, boolean parameterQuirkMode,
			HttpServletRequest request) {

		String requestParameterName = getRequestParameterName(pInfo, false);
		String parameterValue = request.getParameter(requestParameterName);
		if (parameterValue == null && parameterQuirkMode && pInfo.getParamName() == null) {
			parameterValue = request.getParameter(getRequestParameterName(pInfo, true));
		}

		return parameterValue;
	}

	/**
	 * 返回请求参数名。
	 * <p>
	 * 当通过 @ParamName 注解明确指定参数名时将忽略兼容模式，即不会再转换成下划线的形式进行尝试。
	 * 
	 * @param pInfo
	 * @return
	 */
	protected String getRequestParameterName(ParameterInfo pInfo, boolean parameterQuirkMode) {

		ParamName paramName = pInfo.getParamName();
		if (paramName != null) {
			return paramName.value();
		}

		if (parameterQuirkMode) {
			return ConvertFuncUtils.parseCamelCaseToSnakeCase(pInfo.getName());
		} else {
			return pInfo.getName();
		}
	}
	
	/**
	 * Request Payload 形式的字符串（使用场景中多数情况为json字符串）转对象。
	 * 
	 * @param pInfo
	 * @param request
	 * @param jsonConvert
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getRequestPayloadToObject(ParameterInfo pInfo, HttpServletRequest request) throws IOException {

		String requestPayloadValue = null;
		BufferedReader reader = null;

		try {
			reader = request.getReader();
			StringBuilder sb = new StringBuilder();
			char[] buff = new char[256];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
			requestPayloadValue = sb.toString();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Class<?> type = pInfo.getType();
		Type GenericType = pInfo.getGenericType();
		JsonParserConfigurator configurator = getJsonParserConfigurator(pInfo);

		if (type == String.class) { // 如果是参数类型是String，直接返回原始字符串
			return (T) requestPayloadValue;
		}

		if (type == JSONArray.class) { // 如果是参数类型是JSONArray，直接返回对应的转换的JSONArray
			return (T) JSON.parseArray(requestPayloadValue);
		}

		if (type == JSONObject.class) { // 如果是参数类型是JSONObject，直接返回对应的转换的JSONObject
			return (T) JSON.parseObject(requestPayloadValue, configurator.getFeatures());
		}

		return JSON.parseObject(requestPayloadValue, GenericType, configurator.getParserConfig(), configurator.getParseProcess(), configurator.getFeatureValues(), configurator.getFeatures());
	}

	/**
	 * json 字符串转对象。
	 * 
	 * @param pInfo
	 * @param request
	 * @param jsonConvert
	 * @return
	 */
	protected <T> T getRequestJsonToObject(ParameterInfo pInfo, boolean parameterQuirkMode,
			HttpServletRequest request) {

		String parameterValue = getRequestParameterValue(pInfo, parameterQuirkMode, request);
		Type GenericType = pInfo.getGenericType();
		JsonParserConfigurator configurator = getJsonParserConfigurator(pInfo);

		return JSON.parseObject(parameterValue, GenericType, configurator.getParserConfig(),
				configurator.getParseProcess(), configurator.getFeatureValues(), configurator.getFeatures());
	}

	@SuppressWarnings("unchecked")
	protected <T> T getRequestFormToBean(ConvertService service, ParameterInfo pInfo, boolean parameterQuirkMode,
			HttpServletRequest request) throws Exception {

		Class<T> beanClass = (Class<T>) pInfo.getType();

		// 判断是否使用daoBean模式，这里不用判断FormConvert是否为null，因为该方法被调用的情况下FormConvert是一定不为null的
		// 创建表单Bean实例对象过程中当类的访问权限不足时（比如实例化在控制器类内创建的表单Bean类）自动通过Cglib动态代理的方式创建实例对象
		FormConvert formConvert = pInfo.getFormConvert();
		T bean = formConvert.proxy() ? BeanProxyUtils.getDaoBean(beanClass) : BeanProxyUtils.newInstance(beanClass);

		MultiValue multiValue = pInfo.getMultiValue();
		Set<String> multiValueSet = pInfo.getMultiValueSet();
		Set<String> formConvertParamSet = pInfo.getFormConvertParamSet();

		Map<String, String[]> parameterMap = request.getParameterMap();

		Map<String, Method> writeMethodMap = BeanInformationCache.getWriteMethodMap(beanClass);
		BeanConvertServiceContainer container = BeanConvertServiceCache.getConvertServiceContainer(Mold.MVC, beanClass,
				service);

		if (pInfo.getFormConvert().deep()) { // 参数被深度序列化的情况

			Map<String, Object> map = getRequestParameterMapHandleForDeepSerialize(pInfo, parameterMap, parameterQuirkMode);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				Method method = writeMethodMap.get(entry.getKey());
				if (method != null) {
					BeanConvertUtils.convertBeanElement(bean, beanClass, service, container, method, entry.getValue());
				}
			}

		} else {

			for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

				String key = entry.getKey();

				if (formConvertParamSet != null && !formConvertParamSet.contains(key)) {
					continue;
				}

				if (parameterQuirkMode) {
					key = ConvertFuncUtils.parseSnakeCaseToCamelCase(key); // 将key转换为驼峰命名规范
				}

				Method method = writeMethodMap.get(key);
				if (method != null) {
					String[] value = entry.getValue();
					Object obj = getMultiValueHandle(multiValue, multiValueSet, key, value);

					BeanConvertUtils.convertBeanElement(bean, beanClass, service, container, method, obj);
				}
			}

		}

		return bean;
	}

	protected Object getMultiValueHandle(MultiValue multiValue, Set<String> multiValueSet, String key, String[] value) {

		Object obj;
		if (multiValue != null) { // @multiValue注解存在的时候
			if (multiValueSet != null) { // 若@multiValue注解的value值不为空，则指定的字段才取多值，其他取单值
				if (multiValueSet.contains(key)) {
					obj = value;
				} else {
					obj = value[0];
				}
			} else { // 若@multiValue注解的value值为空，则所有都取多值
				obj = value;
			}
		} else { // @multiValue注解不存在的时候，取单值（即数组的第一个值）
			obj = value[0];
		}

		return obj;
	}

	protected Object getRequestFormToMap(ConvertService service, ParameterInfo pInfo, boolean parameterQuirkMode,
			HttpServletRequest request) {

		Map<String, Object> map = null;
		
		Class<?> type = pInfo.getType();
		Type genericType = pInfo.getGenericType();
		Map<String, String[]> parameterMap = request.getParameterMap();
		
		if(pInfo.getFormConvert().deep()){ // 参数被深度序列化的情况
			
			map = getRequestParameterMapHandleForDeepSerialize(pInfo, parameterMap, parameterQuirkMode);
			
		}else {
			
			MultiValue multiValue = pInfo.getMultiValue();
			Set<String> multiValueSet = pInfo.getMultiValueSet();
			Set<String> formConvertParamSet = pInfo.getFormConvertParamSet();

			map = new LinkedHashMap<>(parameterMap.size());

			for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

				String key = entry.getKey();

				if (formConvertParamSet != null && !formConvertParamSet.contains(key)) {
					continue;
				}

				if (parameterQuirkMode) {
					key = ConvertFuncUtils.parseSnakeCaseToCamelCase(key); // 将key转换为驼峰命名规范
				}

				String[] value = entry.getValue();
				Object obj = getMultiValueHandle(multiValue, multiValueSet, key, value);

				map.put(key, obj);
			}
			
		}
		
		// 如果返回的类型是Map（无参数化）或Map<String, Object>（参数化）则直接返回，无须再自行多余的转换操作
		if (type == Map.class) {
			if (genericType instanceof ParameterizedType) { // 参数化类型时
				ParameterizedType actualParameterizedType = (ParameterizedType) genericType;
				Type kType = actualParameterizedType.getActualTypeArguments()[0];
				Type vType = actualParameterizedType.getActualTypeArguments()[1];
				if (kType == String.class && vType == Object.class) {
					return map;
				}
			} else {
				return map;
			}
		}

		Converter converter = service.lookupConverter(type);

		return converter.convert(map, genericType);
	}
	
	protected Map<String, Object> getRequestParameterMapHandleForDeepSerialize(ParameterInfo pInfo, Map<String, String[]> parameterMap, boolean parameterQuirkMode) {

		boolean flag = true;

		Set<String> formConvertParamSet = pInfo.getFormConvertParamSet();

		Map<String, Object> map = new LinkedHashMap<>(parameterMap.size());

		// 第一层级处理
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

			String key = entry.getKey();

			// 判断是否为指定参与表单转换的参数只需要在第一层判断处理即可
			int symbolIndex = key.indexOf('[');
			String tempKey = symbolIndex == -1 ? key : key.substring(0, symbolIndex);
			if (formConvertParamSet != null && !formConvertParamSet.contains(tempKey)) {
				continue;
			}

			if (parameterQuirkMode) {
				key = ConvertFuncUtils.parseSnakeCaseToCamelCase(key); // 将key转换为驼峰命名规范
			}

			String[] value = entry.getValue();
			if (key.endsWith("[]")) {
				map.put(key.substring(0, key.length() - 2), Arrays.asList(value));
			} else {
				map.put(key, value[0]);
				if (key.indexOf('[') != -1 && key.indexOf(']') != -1) {
					flag = false;
				}
			}
		}

		if (flag) {
			return map;
		}

		return recursiveHandleForDeepSerialize(map);
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> recursiveHandleForDeepSerialize(Map<String, Object> map) {

		boolean flag = true;

		Map<String, Object> tempMap = new LinkedHashMap<>();

		for (Map.Entry<String, Object> entry : map.entrySet()) {

			String key = entry.getKey();
			Object value = entry.getValue();

			int leftIndex = key.lastIndexOf('[');
			int rightIndex = key.lastIndexOf(']');

			if (leftIndex != -1 && rightIndex != -1) {
				String name = key.substring(0, leftIndex);
				String itemName = key.substring(leftIndex + 1, rightIndex);

				Object tempObject = tempMap.get(name);
				if (tempObject == null) {
					Map<String, Object> itemMap = new LinkedHashMap<>();
					itemMap.put(itemName, value);

					tempMap.put(name, itemMap);
				} else {
					Map<String, Object> itemMap = (Map<String, Object>) tempObject;
					itemMap.put(itemName, value);

					tempMap.put(name, itemMap);
				}

				flag = false;
			} else {
				tempMap.put(key, value);
			}

		}

		if (flag) {
			return tempMap;
		}

		return recursiveHandleForDeepSerialize(tempMap);
	}

	/**
	 * 通过请求访问 url 映射到对应的类名以及方法名是不区分大小写的，这是必须的无需问为什么。
	 * 
	 * @param packagePrefix
	 * @param uri
	 * @return
	 */
	protected ControllerInfo getControllerInfo(String packagePrefix, String uri) {

		String qualifiedClassName = packagePrefix + uri.replace('/', '.');
		Class<?> controllerClass = getControllerClass(qualifiedClassName); // 通过url模糊匹配控制器类
		if (controllerClass != null) {
			return getControllerInfo(controllerClass);
		} else {
			return null;
		}
	}

	protected MvcConfig getMvcConfig() {

		return mvcConfig;
	}

	protected JsonSerializeConfigurator getJsonSerializeConfigurator() {

		return jsonSerializeConfigurator;
	}

	protected JsonParserConfigurator getJsonParserConfigurator() {

		return jsonParserConfigurator;
	}

}
