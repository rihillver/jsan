package com.jsan.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * 通过重写方法更改 Spring 注解 Bean 的默认命名策略，使用全限定类名作为 Bean 的默认命名策略。
 *
 */

public class QualifiedBeanNameGenerator extends AnnotationBeanNameGenerator {

	@Override
	protected String buildDefaultBeanName(BeanDefinition definition) {

		return definition.getBeanClassName();
	}

}