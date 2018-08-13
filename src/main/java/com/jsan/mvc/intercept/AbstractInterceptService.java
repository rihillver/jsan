package com.jsan.mvc.intercept;

import com.jsan.convert.BeanProxyUtils;

public abstract class AbstractInterceptService implements InterceptService, Cloneable {

	private Interceptor[] interceptorList = null;

	{
		defaultAddInterceptor();
	}

	protected abstract void defaultAddInterceptor();

	@Override
	public InterceptService clone() {

		InterceptService interceptService = null;

		try {
			interceptService = (InterceptService) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}

		interceptService.reinitializeInterceptorList(interceptorList);

		return interceptService;
	}

	@Override
	public void reinitializeInterceptorList(Interceptor[] interceptorList) {

		if (interceptorList == null) {
			this.interceptorList = null;
		} else {
			int length = interceptorList.length;
			this.interceptorList = new Interceptor[length];
			System.arraycopy(interceptorList, 0, this.interceptorList, 0, length);
		}
	}

	@Override
	public void addInterceptor(Interceptor interceptor) {

		if (this.interceptorList == null) {
			this.interceptorList = new Interceptor[0];
		}

		Interceptor[] oldInterceptorList = this.interceptorList;
		int length = oldInterceptorList.length;
		this.interceptorList = new Interceptor[length + 1];
		System.arraycopy(oldInterceptorList, 0, this.interceptorList, 0, length);
		this.interceptorList[length] = interceptor;
	}

	@Override
	public void addInterceptor(Class<? extends Interceptor> interceptorClass) {

		try {
			Interceptor interceptor = BeanProxyUtils.newInstance(interceptorClass);
			addInterceptor(interceptor);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Interceptor[] getInterceptorList() {

		return interceptorList;
	}

}
