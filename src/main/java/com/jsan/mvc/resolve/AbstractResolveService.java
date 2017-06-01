package com.jsan.mvc.resolve;

import java.util.HashMap;
import java.util.Map;

import com.jsan.convert.BeanProxyUtils;
import com.jsan.mvc.resolve.annotation.RegisterName;

public abstract class AbstractResolveService implements ResolveService {

	private final Map<String, Resolver> resolverMap = new HashMap<String, Resolver>();

	{
		defaultRegisterResolver();
	}

	protected abstract void defaultRegisterResolver();

	@Override
	public void registerResolver(Resolver resolver) {

		Class<?> resolverClass = resolver.getClass();
		RegisterName resolverName = resolverClass.getAnnotation(RegisterName.class);
		String name = resolverName == null ? resolverClass.getName() : resolverName.value();
		resolverMap.put(name, resolver);
	}

	@Override
	public void registerResolver(Class<? extends Resolver> resolverClass) {

		try {
			Resolver resolver = BeanProxyUtils.newInstance(resolverClass);
			registerResolver(resolver);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Resolver lookupResolver(String name) {

		Resolver resolver = resolverMap.get(name);
		if (resolver == null) {
			throw new RuntimeException("Resolver for " + name + " is not registered"); // 抛出转换器未注册异常
		}

		return resolver;
	}

}
