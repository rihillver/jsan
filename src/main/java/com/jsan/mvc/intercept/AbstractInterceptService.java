package com.jsan.mvc.intercept;

import java.util.ArrayList;
import java.util.List;

import com.jsan.convert.BeanProxyUtils;

public abstract class AbstractInterceptService implements InterceptService, Cloneable {

	private List<Interceptor> interceptorList = new ArrayList<Interceptor>();

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
	public void reinitializeInterceptorList(List<Interceptor> interceptorList) {

		this.interceptorList = new ArrayList<Interceptor>(interceptorList);
	}

	@Override
	public void addInterceptor(Interceptor interceptor) {

		interceptorList.add(interceptor);
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
	public List<Interceptor> getInterceptorList() {

		return interceptorList;
	}

}
