package com.jsan.mvc.intercept;

/**
 * 控制器方法拦截器接口。
 *
 */

public interface Interceptor {

	/**
	 * 前置通知。
	 * 
	 * @param inv
	 */
	void before(Invocation inv);

	/**
	 * 后置通知，不管调用方法的时候是否异常都执行。
	 * 
	 * @param inv
	 */
	void after(Invocation inv);

	/**
	 * 返回通知。
	 * 
	 * @param inv
	 * @param result
	 */
	void afterReturning(Invocation inv, Object result);

	/**
	 * 异常通知。
	 * 
	 * @param inv
	 * @param e
	 */
	void afterThrowing(Invocation inv, Exception e);

}
