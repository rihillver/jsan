package com.jsan.mvc.filter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import com.jsan.convert.BeanConvertUtils;
import com.jsan.convert.BeanProxyUtils;
import com.jsan.convert.ConvertFuncUtils;
import com.jsan.convert.ConvertService;
import com.jsan.convert.Converter;
import com.jsan.convert.Formatter;
import com.jsan.convert.GeneralConvertService;
import com.jsan.convert.Mold;
import com.jsan.convert.annotation.ConvertServiceRegister;
import com.jsan.convert.annotation.ConverterRegister;
import com.jsan.convert.annotation.DateTimePattern;
import com.jsan.convert.annotation.NumberPattern;
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
import com.jsan.mvc.adapter.SimpleRestMappingAdapter;
import com.jsan.mvc.adapter.StandardMappingAdapter;
import com.jsan.mvc.annotation.Cache;
import com.jsan.mvc.annotation.FormConvert;
import com.jsan.mvc.annotation.MultiValue;
import com.jsan.mvc.annotation.Render;
import com.jsan.mvc.intercept.GeneralInterceptService;
import com.jsan.mvc.intercept.InterceptService;
import com.jsan.mvc.intercept.Interceptor;
import com.jsan.mvc.resolve.GeneralResolveService;
import com.jsan.mvc.resolve.ResolveService;
import com.jsan.mvc.resolve.Resolver;

public abstract class AbstractDispatcher implements Filter {

	protected static final String DEFAULT_CONFIG_FILE = "/jsanmvc.properties"; // 默认配置文件

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

	protected abstract void initCustom(FilterConfig filterConfig);

	/**
	 * 如果通过 Spring IOC 容器的方式获取对象，当使用了 AOP 时返回的可能是代理对象
	 */
	protected abstract Object getControllerObject(ControllerInfo controllerInfo, MethodInfo methodInfo)
			throws Exception;

	@Override
	public String toString() {
		return getClass().getName() + " [customResolverList=" + customResolverList + ", customConverterList="
				+ customConverterList + ", customFormatterList=" + customFormatterList + ", customInterceptorList="
				+ customInterceptorList + ", resolveService=" + resolveService + ", convertService=" + convertService
				+ ", interceptService=" + interceptService + ", mvcConfig=" + mvcConfig + ", configProperties="
				+ configProperties + ", mappingAdapter=" + mappingAdapter + ", cacheAdapter=" + cacheAdapter + "]";
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig config) throws ServletException {

		initConfig(config); // 必须在最前
		initConfigProperties(); // 必须在第二

		// 后面的初始化依赖于前两个
		initStrategies();

		initCustom(config); // 初始化子类自定义的配置

		System.out.println("[mvc] " + toString());
	}

	protected void initConfig(FilterConfig config) {

		mvcConfig = new MvcConfig();

		Field[] fields = MvcConfig.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String parameter = config.getInitParameter(field.getName());
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
				configProperties = MvcFuncUtils.getProperties(configFile);
			} catch (IOException e) {
				throw new RuntimeException("");
			}
		} else {
			try {
				configProperties = MvcFuncUtils.getProperties(DEFAULT_CONFIG_FILE);// 寻找默认配置文件
			} catch (IOException e) {
				// 没有默认配置文件，这种情况允许
				// logging...
				// e.printStackTrace();
			}
		}
	}

	protected void initStrategies() {

		initMappingAdapter();
		initCacheAdapter();

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

		mappingAdapter = (MappingAdapter) MvcFuncUtils.getObjectByProperties(configProperties,
				MappingAdapter.class.getName());

		if (mappingAdapter == null) {
			if ("/".equals(mvcConfig.getMethodDelimiter())) { // 斜杠作为方法分割符的情况下使用轻度REST风格的内置适配器
				mappingAdapter = new SimpleRestMappingAdapter();
			} else {
				mappingAdapter = new StandardMappingAdapter();
			}
		}
	}

	protected void initCacheAdapter() {

		if (mvcConfig.isCacheable()) {
			cacheAdapter = (CacheAdapter) MvcFuncUtils.getObjectByProperties(configProperties,
					CacheAdapter.class.getName());
			if (cacheAdapter == null) {
				cacheAdapter = new EhcacheCacheAdapter();
			}
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

		String[] customs = MvcFuncUtils.getStringArrayByProperties(configProperties, type.getName());

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

		Class<? extends InterceptService> interceptServiceClass = (Class<? extends InterceptService>) MvcFuncUtils
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

		Class<? extends ResolveService> resolveServiceClass = (Class<? extends ResolveService>) MvcFuncUtils
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

		Class<? extends ConvertService> convertServiceClass = (Class<? extends ConvertService>) MvcFuncUtils
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
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	protected boolean isDebug() {

		return mvcConfig.isDebug();
	}

	protected void printDebugMessage(String message) {

		printDebugMessage(message, false);
	}

	protected void printDebugMessage(String message, boolean url) {

		if (url) {
			System.out.println("[url] " + message);
		} else {
			System.out.println("[mvc] " + message);
		}
	}

	protected final ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>() {

		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm:ss");
		}
	};

	protected void printRequestTestMessage(HttpServletRequest request, boolean url) {

		if (isDebug()) {
			System.out.println("\n");
			printDebugMessage(createRequestTestMessage(request), url);
		}
	}

	protected String createRequestTestMessage(HttpServletRequest request) {

		StringBuilder sb = new StringBuilder();
		sb.append(simpleDateFormatThreadLocal.get().format(new Date()));
		sb.append(" -- (");
		sb.append(request.getRemoteAddr());
		sb.append(") ");
		sb.append(request.getRequestURI());
		String str = request.getQueryString();
		if (str != null) {
			sb.append("?");
			sb.append(str);
		}

		return sb.toString();
	}

	// ==================================================

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		try {
			doDispatcher(request, response, filterChain);
		} catch (Exception e) {
			e.printStackTrace();
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

					printRequestTestMessage(request, false); // test 输出

					View view = getView(mappingInfo, cInfo, mInfo, request, response);

					setExecuteTime(view, startTime);

					resolveView(view, mappingInfo, mInfo, request, response);

				} else {
					nextFilter(request, response, chain);
				}
			} else {
				nextFilter(request, response, chain);
			}
		} else {
			nextFilter(request, response, chain);
		}
	}

	protected void nextFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		printRequestTestMessage(request, true); // test输出
		chain.doFilter(request, response);
	}

	protected void setExecuteTime(View view, long startTime) {

		String executeTime = mvcConfig.getExecuteTime();
		if (executeTime != null && view.get(executeTime) == null) {
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

	protected void resolveView(View view, MappingInfo mappingInfo, MethodInfo mInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (!response.isCommitted()) { // 判断是否已将数据输出到客户端，如果已输出则无必要再执行视图解析
			view.getResolver().execute(view, mvcConfig, mappingInfo, request, response);
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
			if (returnValue instanceof View) { // 如果返回值为View，那么这个View可能为方法内部自己new出来的，所以还需要判断resolver是否为null，如果为null则再次设置上去，url则不能在此加上去
				view = (View) returnValue;
				if (view.getResolver() == null) {
					view.setResolver(getViewResolver(mInfo));
				}
			} else {
				view.addValue(returnValue);
			}
		}
		return view;
	}

	protected View getViewInitialized(MethodInfo mInfo) throws Exception {

		View view = new View();

		view.setUrl(getViewUrl(mInfo));
		view.setResolver(getViewResolver(mInfo));

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
						url = method.getName(); // 如果url为空，则取其方法名，不需要在此将方法名转换为小写，方法内部运行时可将url设置为null
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

		StringBuilder message = isDebug() ? new StringBuilder() : null;

		ParameterInfo[] parameterInfos = mInfo.getParameterInfos();
		int length = parameterInfos.length;
		Object[] parameterObjects = new Object[length];

		for (int i = 0; i < length; i++) {

			ParameterInfo pInfo = parameterInfos[i];
			Class<?> type = pInfo.getType();
			Type GenericType = pInfo.getGenericType();

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

				FormConvert formConvert = pInfo.getFormConvert();
				if (formConvert != null) {
					if (Map.class.isAssignableFrom(type)) { // Map处理
						parameterObjects[i] = getRequestFormToMap(service, pInfo, request);
					} else { // Bean处理
						boolean daoBeanMode = formConvert.value(); // Bean是否使用代理对象的方式
						parameterObjects[i] = getRequestFormToBean(service, cInfo, mInfo, pInfo, request, daoBeanMode);
					}
				} else {
					Object parameterValue;
					if (pInfo.getMethodValue() != null) {
						parameterValue = mappingInfo.getMethodValue();
					} else {
						if (pInfo.getMultiValue() != null) { // 是否为指定多值的参数
							parameterValue = request.getParameterValues(pInfo.getName());
						} else {
							parameterValue = request.getParameter(pInfo.getName());
						}
					}
					Converter converter = service.lookupConverter(type);
					parameterObjects[i] = converter.convert(parameterValue, GenericType);
				}
			}

			if (message != null) {
				if (i == 0) {
					message.append("\n");
				}
				message.append("----- ");
				message.append("(");
				message.append(type.getCanonicalName());
				message.append(") ");
				message.append(pInfo.getName());
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
						arrayStr = Arrays.toString((Object[]) parameterObjects[i]);
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
			message.insert(0, "invoker -- ");
			printDebugMessage(message.toString());
		}

		return parameterObjects;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getRequestFormToBean(ConvertService service, ControllerInfo cInfo, MethodInfo mInfo,
			ParameterInfo pInfo, HttpServletRequest request, boolean daoBeanMode) throws Exception {

		Class<T> beanClass = (Class<T>) pInfo.getType();

		// 创建表单 Bean 实例对象，当类的访问权限不足时（比如实例化在控制器类内创建的表单 Bean 类）自动通过 Cglib
		// 动态代理的方式创建实例对象。
		T bean = daoBeanMode ? BeanProxyUtils.getDaoBean(beanClass) : BeanProxyUtils.newInstance(beanClass);

		MultiValue multiValue = pInfo.getMultiValue();
		Set<String> multiValueSet = pInfo.getMultiValueSet();

		Map<String, String[]> parameterMap = request.getParameterMap();

		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

			String key = entry.getKey();
			String[] value = entry.getValue();
			Object obj = getMultiValueHandle(multiValue, multiValueSet, key, value);

			BeanConvertUtils.convertBeanElement(Mold.MVC, bean, beanClass, service, key, obj);
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

	protected Object getRequestFormToMap(ConvertService service, ParameterInfo pInfo, HttpServletRequest request) {

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Type genericType = pInfo.getGenericType();
		MultiValue multiValue = pInfo.getMultiValue();
		Set<String> multiValueSet = pInfo.getMultiValueSet();

		Map<String, String[]> requestParameterMap = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : requestParameterMap.entrySet()) {
			String key = entry.getKey();
			String[] value = entry.getValue();
			Object obj = getMultiValueHandle(multiValue, multiValueSet, key, value);
			map.put(key, obj);
		}

		Converter converter = service.lookupConverter(pInfo.getType());

		return converter.convert(map, genericType);
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

}