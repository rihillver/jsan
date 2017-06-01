package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 控制器上的缓存注解。
 *
 */

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

@Cache("") // 定义一个默认的特殊注解，以便 DispatcherFilter 的 getCacheAnnotation 方法获取。
public @interface Cache {

	String value();

	boolean session() default false;

}
