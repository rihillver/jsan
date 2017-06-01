package com.jsan.mvc.intercept;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

public class Invocation {

	private Object returnValue;
	private boolean hasReturnValue;
	private Object target;
	private Method method;
	private Object[] args;
	private MethodProxy methodProxy;

	// private HttpServletRequest request;
	// private HttpServletResponse response;
	// private String methodValue; // 映射到控制器类上的方法名，与MappingInfo的methodValue一致

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {

		setHasReturnValue(true); // 此处设置存在有认为设定的返回值标识
		this.returnValue = returnValue;
	}

	public boolean isHasReturnValue() {
		return hasReturnValue;
	}

	public void setHasReturnValue(boolean hasReturnValue) {
		this.hasReturnValue = hasReturnValue;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public MethodProxy getMethodProxy() {
		return methodProxy;
	}

	public void setMethodProxy(MethodProxy methodProxy) {
		this.methodProxy = methodProxy;
	}

	public Object getArg(int index) {

		if (index >= args.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return args[index];
	}

	public void setArg(int index, Object value) {

		if (index >= args.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		args[index] = value;
	}

}
