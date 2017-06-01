package com.jsan.mvc.resolve;

public interface ResolveService {

	public void registerResolver(Resolver resolver);

	public void registerResolver(Class<? extends Resolver> resolverClass);

	public Resolver lookupResolver(String name);
}
