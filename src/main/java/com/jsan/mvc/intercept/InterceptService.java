package com.jsan.mvc.intercept;

public interface InterceptService {

	InterceptService clone();

	void reinitializeInterceptorList(Interceptor[] interceptorList);

	void addInterceptor(Interceptor interceptor);

	void addInterceptor(Class<? extends Interceptor> interceptorClass);

	Interceptor[] getInterceptorList();
}
