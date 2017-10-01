package com.jsan.mvc.intercept;

import java.util.List;

public interface InterceptService {

	InterceptService clone();

	void reinitializeInterceptorList(List<Interceptor> interceptorList);

	void addInterceptor(Interceptor interceptor);

	void addInterceptor(Class<? extends Interceptor> interceptorClass);

	List<Interceptor> getInterceptorList();
}
