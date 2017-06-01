package com.jsan.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * 通过重写方法更改 Spring 注解 Bean 的默认作用域，使用多例作为 Bean 的默认作用域。
 *
 */

public class PrototypeScopeMetadataResolver extends AnnotationScopeMetadataResolver {

	@Override
	public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
		ScopeMetadata metadata = new ScopeMetadata();
		metadata.setScopeName(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
		metadata.setScopedProxyMode(ScopedProxyMode.NO);
		return metadata;
	}

}
