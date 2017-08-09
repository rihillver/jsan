package com.jsan.mvc.resolve;

public interface ResolveService {

	void registerResolver(Resolver resolver);

	void registerResolver(Class<? extends Resolver> resolverClass);

	Resolver lookupResolver(String name);
}
