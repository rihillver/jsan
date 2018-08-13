package com.jsan.mvc.filter;

import java.lang.reflect.Method;

import com.jsan.mvc.ControllerInfo;
import com.jsan.mvc.MethodInfo;
import com.jsan.mvc.annotation.InterceptServiceRegister;
import com.jsan.mvc.annotation.InterceptorRegister;
import com.jsan.mvc.intercept.ControllerMethodInterceptor;
import com.jsan.mvc.intercept.InterceptService;
import com.jsan.mvc.intercept.InterceptServiceCache;
import com.jsan.mvc.intercept.Interceptor;

import net.sf.cglib.proxy.Enhancer;

/**
 * 请求转发器。
 * <p>
 * 详细配置参数请参见 {@link com.jsan.mvc.MvcConfig} 。
 * <p>
 * 拦截服务或拦截器注解：<br>
 * 1、在控制器类级别上定义拦截服务或拦截器注解将适用于拦截该控制器类的所有映射方法（即注有@Render注解的方法）。
 * 2、在控制器类内方法互相调用的情况，如果调用没有@Render注解的其他方法，则不会触发任何拦截服务；如果调用含有@Render
 * 注解的其他方法，此时需要看情况了，如果被调用的方法已经响应过映射请求，则会触发改方法上对应的拦截服务，如果被调
 * 用的方法从未响应过映射请求，则不会触发改方法上对应的拦截服务（因为拦截服务只有在第一次响应映射请求的时候才放入缓存，
 * 当该方法从未响应过映射请求的情况下被其他方法调用，找不到相应的拦截服务缓存，所以不会触发对应的拦截服务，也可以看做是
 * 设计中存在的Bug吧，使用过程中知道有这种情况存在即可）。
 * <p>
 * MvcConfig 中的关于 multiton 和 interceptable 的配置只在当不依赖 Spring IOC
 * 管理控制器对象的情况下有效，若集成 Spring 的情况下请使用 @Scope 注解来实现单例或多例（或在 Spring 配置文件通过
 * component-scan 全局指定为多例策略）。
 * <p>
 * 该类使用了 Cglib 动态代理控制器对象，可以在控制器映射方法上通过注解方式来轻度实现拦截功能。如果改用依赖 Spring IOC
 * 管理控制器对象时，请使用 Spring AOP 或 AspestJ 来实现控制器映射方法上的拦截功能。
 *
 */

public class DispatcherFilter extends AbstractDispatcher {

	@Override
	protected void initCustom() {

	}

	@Override
	protected Object getControllerObject(ControllerInfo cInfo, MethodInfo mInfo) throws Exception {

		if (getMvcConfig().isMultiton()) {
			return getControllerObjectByMultiton(cInfo, mInfo); // 多例模式
		} else {
			return getControllerObjectBySingleton(cInfo, mInfo); // 单例模式
		}
	}

	protected Object getControllerObjectByMultiton(ControllerInfo cInfo, MethodInfo mInfo) throws Exception {

		if (getMvcConfig().isInterceptable()) {
			return createControllerInstanceByInterceptable(cInfo, mInfo);
		} else {
			return createControllerInstanceByGeneralCreation(cInfo);
		}
	}

	protected Object getControllerObjectBySingleton(ControllerInfo cInfo, MethodInfo mInfo) throws Exception {

		Object obj;

		if (getMvcConfig().isInterceptable()) {
			obj = mInfo.getObject(); // 使用拦截器的情况下通过mInfo获取缓存的代理对象
			if (obj == null) {
				synchronized (mInfo) {
					obj = mInfo.getObject();
					if (obj == null) {
						obj = createControllerInstanceByInterceptable(cInfo, mInfo);
						mInfo.setObject(obj);
					}
				}
			}
		} else {
			obj = getControllerObjectForGeneralCreation(cInfo);
		}

		return obj;
	}

	/**
	 * 获取通过普通方式创建的控制器对象。
	 * 
	 * @param cInfo
	 * @return
	 * @throws Exception
	 */
	protected Object getControllerObjectForGeneralCreation(ControllerInfo cInfo) throws Exception {

		Object obj = cInfo.getObject();

		if (obj == null) {
			synchronized (cInfo) {
				obj = cInfo.getObject();
				if (obj == null) {
					obj = createControllerInstanceByGeneralCreation(cInfo);
					cInfo.setObject(obj);
				}
			}
		}

		return obj;
	}

	protected Object createControllerInstanceByGeneralCreation(ControllerInfo cInfo) throws Exception {

		return newInstance(cInfo.getType());
	}

	protected Object createControllerInstanceByInterceptable(ControllerInfo cInfo, MethodInfo mInfo) throws Exception {

		InterceptService service = mInfo.getInterceptService();
		if (service == null) {
			synchronized (mInfo) {
				service = mInfo.getInterceptService();
				if (service == null) {
					service = getMethodInterceptService(cInfo, mInfo);
					mInfo.setInterceptService(service);
					InterceptServiceCache.put(mInfo.getMethod(), service); // 加入拦截器服务的独立缓存，用于在控制器方法拦截器ControllerMethodInterceptor中调用
				}
			}
		}

		Interceptor[] list = service.getInterceptorList();

		if (list == null) {
			return createControllerInstanceByGeneralCreation(cInfo);
		} else {
			return Enhancer.create(cInfo.getType(), ControllerMethodInterceptor.INSTANCE);
		}
	}

	protected InterceptService getMethodInterceptService(ControllerInfo cInfo, MethodInfo mInfo) {

		InterceptService service = mInfo.getInterceptService();

		if (service == null) {
			synchronized (mInfo) {
				service = mInfo.getInterceptService();
				if (service == null) {
					Method method = mInfo.getMethod();
					if (method.isAnnotationPresent(InterceptServiceRegister.class)
							|| method.isAnnotationPresent(InterceptorRegister.class)) {
						service = createMethodInterceptService(cInfo, mInfo);
					} else {
						service = getTypeInterceptService(cInfo); // 获取定义在类上的拦截服务
					}
					mInfo.setInterceptService(service);
				}
			}
		}

		return service;
	}

	protected InterceptService createMethodInterceptService(ControllerInfo cInfo, MethodInfo mInfo) {

		Method method = mInfo.getMethod();
		InterceptServiceRegister register = method.getAnnotation(InterceptServiceRegister.class);

		InterceptService service;
		if (register != null) {
			service = newInstance(register.value());
		} else {
			service = getTypeInterceptService(cInfo).clone();
		}

		addInterceptor(service, method.getAnnotation(InterceptorRegister.class));

		return service;
	}

	protected InterceptService getTypeInterceptService(ControllerInfo cInfo) {

		InterceptService service = cInfo.getInterceptService();

		if (service == null) {
			synchronized (cInfo) {
				service = cInfo.getInterceptService();
				if (service == null) {
					Class<?> type = cInfo.getType();
					if (type.isAnnotationPresent(InterceptServiceRegister.class)
							|| type.isAnnotationPresent(InterceptorRegister.class)) {
						service = createTypeInterceptService(cInfo);
					} else {
						service = getInterceptService(); // 获取定义在DispatcherFilter上的拦截服务
					}
					cInfo.setInterceptService(service);
				}
			}
		}

		return service;
	}

	protected InterceptService createTypeInterceptService(ControllerInfo cInfo) {

		Class<?> type = cInfo.getType();
		InterceptServiceRegister register = type.getAnnotation(InterceptServiceRegister.class);

		InterceptService service;
		if (register != null) {
			service = newInstance(register.value());
		} else {
			service = getInterceptService().clone();
		}

		addInterceptor(service, type.getAnnotation(InterceptorRegister.class));

		return service;
	}

	protected void addInterceptor(InterceptService service, InterceptorRegister register) {

		if (register != null) {
			Class<? extends Interceptor>[] classes = register.value();
			for (Class<? extends Interceptor> clazz : classes) {
				service.addInterceptor(clazz);
			}
		}
	}

}
