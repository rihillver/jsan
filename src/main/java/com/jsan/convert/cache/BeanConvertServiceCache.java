package com.jsan.convert.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.jsan.convert.Mold;
import com.jsan.convert.ConvertFuncUtils;
import com.jsan.convert.ConvertService;
import com.jsan.convert.annotation.ConvertServiceRegister;
import com.jsan.convert.annotation.ConverterRegister;
import com.jsan.convert.annotation.DateTimePattern;
import com.jsan.convert.annotation.NumberPattern;

/**
 * 提供对 Bean 的 setter 方法上的参数级别转换提供相应的 ConvertService 缓存。
 * 
 * <p>
 * Bean 上可以由以下四个注解进行相应的定义：
 * <ul>
 * <li>@ConverterRegister</li>
 * <li>@ConvertServiceRegister</li>
 * <li>@DateTimePattern</li>
 * <li>@NumberPattern</li>
 * </ul>
 *
 */

public class BeanConvertServiceCache {

	private static final Map<Class<?>, BeanConvertServiceContainer> common_beanConvertServiceContainerMap = new HashMap<Class<?>, BeanConvertServiceContainer>();
	private static final Map<Class<?>, BeanConvertServiceContainer> mvc_beanConvertServiceContainerMap = new HashMap<Class<?>, BeanConvertServiceContainer>();
	private static final Map<Class<?>, BeanConvertServiceContainer> dao_beanConvertServiceContainerMap = new HashMap<Class<?>, BeanConvertServiceContainer>();

	public static ConvertService getConvertService(Mold mold, Class<?> beanClass, Method method,
			ConvertService convertService) {

		Map<Class<?>, BeanConvertServiceContainer> beanConvertServiceContainerMap;

		switch (mold) {
		case MVC:
			beanConvertServiceContainerMap = mvc_beanConvertServiceContainerMap;
			break;
		case DAO:
			beanConvertServiceContainerMap = dao_beanConvertServiceContainerMap;
			break;
		case COMMON:
			beanConvertServiceContainerMap = common_beanConvertServiceContainerMap;
			break;
		default:
			beanConvertServiceContainerMap = common_beanConvertServiceContainerMap;
			break;
		}

		return getBeanConvertService(beanClass, method, convertService, beanConvertServiceContainerMap);
	}

	private static ConvertService createConvertServiceInstance(Class<? extends ConvertService> convertServiceClass) {

		try {
			return convertServiceClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static ConvertService getBeanTypeConvertService(Class<?> beanClass, ConvertService convertService) {

		ConvertService typeConvertService = null;
		if (beanClass.isAnnotationPresent(ConvertServiceRegister.class)
				|| beanClass.isAnnotationPresent(ConverterRegister.class)
				|| beanClass.isAnnotationPresent(DateTimePattern.class)
				|| beanClass.isAnnotationPresent(NumberPattern.class)) {
			ConvertServiceRegister convertServiceRegister = beanClass.getAnnotation(ConvertServiceRegister.class);
			if (convertServiceRegister != null) {
				typeConvertService = createConvertServiceInstance(convertServiceRegister.value());
			} else {
				typeConvertService = convertService.clone();
			}
			ConvertFuncUtils.registerConverterAndFormatter(typeConvertService,
					beanClass.getAnnotation(ConverterRegister.class), beanClass.getAnnotation(DateTimePattern.class),
					beanClass.getAnnotation(NumberPattern.class));
		}

		return typeConvertService;
	}

	private static ConvertService getBeanMethodConvertService(Class<?> beanClass, Method method,
			ConvertService convertService, ConvertService typeConvertService) {

		ConvertService methodConvertService = null;
		if (method.isAnnotationPresent(ConvertServiceRegister.class)
				|| method.isAnnotationPresent(ConverterRegister.class)
				|| method.isAnnotationPresent(DateTimePattern.class)
				|| method.isAnnotationPresent(NumberPattern.class)) {
			ConvertServiceRegister convertServiceRegister = method.getAnnotation(ConvertServiceRegister.class);
			if (convertServiceRegister != null) {
				methodConvertService = createConvertServiceInstance(convertServiceRegister.value());
			} else {
				methodConvertService = typeConvertService == null ? convertService.clone() : typeConvertService.clone();
			}
			ConvertFuncUtils.registerConverterAndFormatter(methodConvertService,
					method.getAnnotation(ConverterRegister.class), method.getAnnotation(DateTimePattern.class),
					method.getAnnotation(NumberPattern.class));
		}

		return methodConvertService;
	}

	private static ConvertService getBeanParameterConvertService(Class<?> beanClass, Method method,
			ConvertService convertService, ConvertService typeConvertService, ConvertService methodConvertService) {

		ConvertService parameterConvertService = null;

		ConvertServiceRegister parameterConvertServiceRegister = null;
		ConverterRegister parameterConverterRegister = null;
		DateTimePattern parameterDateTimePattern = null;
		NumberPattern parameterNumberPattern = null;

		Annotation[] firstParameterAnnotations = method.getParameterAnnotations()[0];
		for (Annotation annotation : firstParameterAnnotations) {
			if (annotation instanceof ConvertServiceRegister) {
				parameterConvertServiceRegister = (ConvertServiceRegister) annotation;
			} else if (annotation instanceof ConverterRegister) {
				parameterConverterRegister = (ConverterRegister) annotation;
			} else if (annotation instanceof DateTimePattern) {
				parameterDateTimePattern = (DateTimePattern) annotation;
			} else if (annotation instanceof NumberPattern) {
				parameterNumberPattern = (NumberPattern) annotation;
			}
		}

		if (parameterConvertServiceRegister != null || parameterConverterRegister != null
				|| parameterDateTimePattern != null || parameterNumberPattern != null) {
			if (parameterConvertServiceRegister != null) {
				parameterConvertService = createConvertServiceInstance(parameterConvertServiceRegister.value());
			} else {
				if (methodConvertService == null) {
					parameterConvertService = typeConvertService == null ? convertService.clone()
							: typeConvertService.clone();
				} else {
					parameterConvertService = methodConvertService.clone();
				}
			}
			ConvertFuncUtils.registerConverterAndFormatter(parameterConvertService, parameterConverterRegister,
					parameterDateTimePattern, parameterNumberPattern);
		}

		if (parameterConvertService == null) {
			if (methodConvertService == null) {
				if (typeConvertService == null) {
					parameterConvertService = convertService;
				} else {
					parameterConvertService = typeConvertService;
				}
			} else {
				parameterConvertService = methodConvertService;
			}
		}

		return parameterConvertService;
	}

	private static ConvertService createBeanWriteMethodParameterConvertServiceInstance(Class<?> beanClass,
			Method method, ConvertService convertService) {

		ConvertService typeConvertService = getBeanTypeConvertService(beanClass, convertService);
		ConvertService methodConvertService = getBeanMethodConvertService(beanClass, method, convertService,
				typeConvertService);
		ConvertService parameterConvertService = getBeanParameterConvertService(beanClass, method, convertService,
				typeConvertService, methodConvertService);

		return parameterConvertService;
	}

	private static Map<Method, ConvertService> createBeanWriteMethodParameterConvertServiceMap(Class<?> beanClass,
			ConvertService convertService) {

		Map<Method, ConvertService> map = new HashMap<Method, ConvertService>();

		ConvertService typeConvertService = getBeanTypeConvertService(beanClass, convertService);
		Map<String, Method> writeMethodMap = BeanInformationCache.getWriteMethodMap(beanClass);

		for (Map.Entry<String, Method> entry : writeMethodMap.entrySet()) {
			Method method = entry.getValue();
			ConvertService methodConvertService = getBeanMethodConvertService(beanClass, method, convertService,
					typeConvertService);
			ConvertService parameterConvertService = getBeanParameterConvertService(beanClass, method, convertService,
					typeConvertService, methodConvertService);
			map.put(method, parameterConvertService);
		}

		return map;
	}

	private static BeanConvertServiceContainer createBeanConvertServiceInfo(Class<?> beanClass,
			ConvertService convertService) {

		BeanConvertServiceContainer container = new BeanConvertServiceContainer();
		container.setFirstConvertService(convertService);
		container.setWriteMethodParameterConvertServiceMap(
				createBeanWriteMethodParameterConvertServiceMap(beanClass, convertService));

		return container;
	}

	private static ConvertService getBeanConvertService(Class<?> beanClass, Method method,
			ConvertService convertService, Map<Class<?>, BeanConvertServiceContainer> beanConvertServiceContainerMap) {

		BeanConvertServiceContainer container = beanConvertServiceContainerMap.get(beanClass);

		if (container == null) {
			synchronized (beanConvertServiceContainerMap) {
				container = beanConvertServiceContainerMap.get(beanClass);
				if (container == null) {
					container = createBeanConvertServiceInfo(beanClass, convertService);
					beanConvertServiceContainerMap.put(beanClass, container);
				}
			}
		}

		if (container.getFirstConvertService() == convertService) {
			// 当上层 convertService 与 firstConvertService 是同一对象时则直接取缓存
			return container.getWriteMethodParameterConvertService(method);
		} else {
			// 当上层 convertService 与 firstConvertService
			// 不是同一对象时则不能能使用缓存了，只能够直接通过克隆新的 convertService 再进行注册格式化器
			return createBeanWriteMethodParameterConvertServiceInstance(beanClass, method, convertService);
		}
	}

}
