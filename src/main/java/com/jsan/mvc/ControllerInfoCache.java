package com.jsan.mvc;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jsan.convert.annotation.ConvertServiceRegister;
import com.jsan.convert.annotation.ConverterRegister;
import com.jsan.convert.annotation.DateTimePattern;
import com.jsan.convert.annotation.NumberPattern;
import com.jsan.mvc.annotation.Delete;
import com.jsan.mvc.annotation.FormConvert;
import com.jsan.mvc.annotation.Get;
import com.jsan.mvc.annotation.JsonConvert;
import com.jsan.mvc.annotation.MethodValue;
import com.jsan.mvc.annotation.MultiValue;
import com.jsan.mvc.annotation.ParamName;
import com.jsan.mvc.annotation.Post;
import com.jsan.mvc.annotation.Put;
import com.jsan.mvc.annotation.QuirkMode;
import com.jsan.mvc.annotation.Render;

/**
 * ControllerInfo 缓存。
 *
 */

public class ControllerInfoCache {

	private static final Map<Class<?>, ControllerInfo> controllerInfoMap = new HashMap<Class<?>, ControllerInfo>(); // 多个Filter共用

	public static ControllerInfo get(Class<?> controllerClass) {

		ControllerInfo cInfo = controllerInfoMap.get(controllerClass);

		if (cInfo == null) {
			synchronized (controllerInfoMap) {
				cInfo = controllerInfoMap.get(controllerClass);
				if (cInfo == null) {
					cInfo = getInstance(controllerClass);
					if (cInfo != null) {
						controllerInfoMap.put(controllerClass, cInfo);
					}
				}
			}
		}
		return cInfo;
	}

	/**
	 * 返回一个 ControllerInfo 实例，并且初始化部分基本设置，但设置对象 setObject()
	 * 不用在此设置，而是在调度器那里根据情况设置。
	 * 
	 * @param controllerClass
	 * @return
	 */
	private static ControllerInfo getInstance(Class<?> controllerClass) {

		ControllerInfo cInfo = new ControllerInfo();
		cInfo.setType(controllerClass);
		cInfo.setMethodInfoMap(getMethodInfoMap(controllerClass));

		return cInfo;
	}

	/**
	 * 返回指定控制器类的 MethodInfo 的 Map。
	 * <p>
	 * 设置转换服务 setConvertService() 和解析器 setResolver() 不用在此设置，而是在调度器那里判断设置。
	 * 
	 * @param controllerClass
	 * @return
	 */
	private static Map<String, MethodInfo> getMethodInfoMap(Class<?> controllerClass) {

		Map<String, MethodInfo> map = new HashMap<String, MethodInfo>();

		Map<String, List<String>> parameterNamesMap;
		try {
			parameterNamesMap = MvcFuncUtils.getParameterNamesMap(controllerClass);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Method[] methods = controllerClass.getDeclaredMethods();

		for (Method method : methods) {
			method.setAccessible(true);
			Render render = method.getAnnotation(Render.class);
			if (render != null) {
				MethodInfo methodInfo = new MethodInfo();
				methodInfo.setMethod(method);
				methodInfo.setParameterInfos(getParameterInfos(method, parameterNamesMap));
				putMethodInfoToMap(map, method, methodInfo);
			}
		}

		return map;
	}

	/**
	 * 返回方法形参名。
	 * 
	 * @param method
	 * @param parameterNamesMap
	 * @return
	 */
	private static ParameterInfo[] getParameterInfos(Method method, Map<String, List<String>> parameterNamesMap) {

		Class<?>[] parameterTypes = method.getParameterTypes();
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		int length = parameterTypes.length;
		String parameterNamesKey = MvcFuncUtils.getParameterNamesKey(method);
		String[] parameterNames = getParameterNames(length, parameterNamesKey, parameterNamesMap); // 方法形参名，通过java字节码获取

		if (parameterNames == null) {
			throw new RuntimeException("Method: " + method + "'s parameterNames can not be null");
		}

		ParameterInfo[] pInfos = new ParameterInfo[length];

		for (int i = 0; i < length; i++) {
			ParameterInfo info = new ParameterInfo();
			info.setName(parameterNames[i]);
			info.setType(parameterTypes[i]);
			info.setGenericType(genericParameterTypes[i]);

			fillAnnotationToParameterInfo(info, parameterAnnotations[i]);

			pInfos[i] = info;
		}

		return pInfos;
	}

	private static void fillAnnotationToParameterInfo(ParameterInfo pInfo, Annotation[] annotations) {

		for (Annotation annotation : annotations) {
			if (annotation instanceof ParamName) {
				pInfo.setParamName((ParamName) annotation);
			} else if (annotation instanceof FormConvert) {
				FormConvert formConvert = (FormConvert) annotation;
				pInfo.setFormConvert(formConvert);
				String[] params = formConvert.params();
				if (params.length > 0) {
					Set<String> set = new HashSet<String>(Arrays.asList(params));
					pInfo.setFormConvertParamSet(set); // 初始化formConvertParamSet
				}
			} else if (annotation instanceof JsonConvert) {
				pInfo.setJsonConvert((JsonConvert) annotation);
			} else if (annotation instanceof QuirkMode) {
				pInfo.setQuirkMode((QuirkMode) annotation);

			} else if (annotation instanceof MethodValue) {
				pInfo.setMethodValue((MethodValue) annotation);
			} else if (annotation instanceof MultiValue) {
				MultiValue multiValue = (MultiValue) annotation;
				pInfo.setMultiValue(multiValue);
				String[] value = multiValue.value();
				if (value.length > 0) {
					Set<String> set = new HashSet<String>(Arrays.asList(value));
					pInfo.setMultiValueSet(set); // 初始化 multiValueSet
				}
			} else if (annotation instanceof ConvertServiceRegister) {
				pInfo.setConvertServiceRegister((ConvertServiceRegister) annotation);
			} else if (annotation instanceof ConverterRegister) {
				pInfo.setConverterRegister((ConverterRegister) annotation);
			} else if (annotation instanceof DateTimePattern) {
				pInfo.setDateTimePattern((DateTimePattern) annotation);
			} else if (annotation instanceof NumberPattern) {
				pInfo.setNumberPattern((NumberPattern) annotation);
			}
		}
	}

	/**
	 * 当返回的是 null 的值，那说明方法形参名的获取一定存在异常（当 method.getParameterTypes()
	 * 即使方法无形参返回到也是空，而不是 null）。
	 * 
	 * @param method
	 * @param parameterNamesMap
	 * @return
	 */
	private static String[] getParameterNames(int length, String parameterNamesKey,
			Map<String, List<String>> parameterNamesMap) {

		String[] strs = null;

		if (parameterNamesMap != null) {
			if (length == 0) {
				strs = new String[0];
			} else {
				List<String> list = parameterNamesMap.get(parameterNamesKey);
				if (list != null) {
					if (list.size() != length) {
						list = list.subList(0, length);
					}
					strs = list.toArray(new String[length]);
				}
			}
		}

		return strs;
	}

	/**
	 * 将方法名加上 POST、 GET、PUT、DELETE 形式作为 key 的 MethodInfo 放入 Map。
	 * <p>
	 * 1、当控制器上的映射方法上没有使用 @Get、@Post、@Put、@Delete 中的任何注解时，只有 POST 和 GET
	 * 这两种只读的请求方法才会被匹配到。<br>
	 * 2、 对于 PUT 和 DELETE 请求方法必须显式的在映射方法上使用 @Put 和 @Delete 注解才能被匹配到。<br>
	 * 3、其他 OPTIONS、HEAD、TRACE、CONNECT 这些请求方法均无法被匹配到映射方法上。<br>
	 * 4、目前绝大多数浏览器对页面表单仅支持 GET 和 POST，但可以通过 jQuery 的 Ajax 来执行 PUT 和 DELETE
	 * 等其他请求方法。
	 * 
	 * @param map
	 * @param method
	 * @param mInfo
	 */
	private static void putMethodInfoToMap(Map<String, MethodInfo> map, Method method, MethodInfo mInfo) {

		String methodName = method.getName();

		if (method.isAnnotationPresent(Put.class) || method.isAnnotationPresent(Delete.class)) {
			if (method.isAnnotationPresent(Put.class)) {
				map.put(getMethodInfoMapKey(methodName, "put"), mInfo);
			} else {
				map.put(getMethodInfoMapKey(methodName, "delete"), mInfo);
			}
		} else {
			String keyNameByGet = getMethodInfoMapKey(methodName, "get");
			String keyNameByPost = getMethodInfoMapKey(methodName, "post");

			if (method.isAnnotationPresent(Get.class)) {
				map.put(keyNameByGet, mInfo);
			} else if (method.isAnnotationPresent(Post.class)) {
				map.put(keyNameByPost, mInfo);
			} else {
				if (!map.containsKey(keyNameByGet)) {
					map.put(keyNameByGet, mInfo);
				}
				if (!map.containsKey(keyNameByPost)) {
					map.put(keyNameByPost, mInfo);
				}
			}
		}

	}

	/**
	 * 返回方法键名。
	 * 
	 * @param methodName
	 * @param requestMethod
	 * @return
	 */
	static String getMethodInfoMapKey(String methodName, String requestMethod) {

		String key = methodName + "#" + requestMethod;
		return key.toLowerCase(); // 转换为小写
	}

}
