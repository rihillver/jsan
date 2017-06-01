package com.jsan.mvc.resolve.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 专为 Resolver 设计的注解，用以标识视图解析器的注册名。
 * <p>
 * 当解析器注册的时候使用该标识名字进行注册，没有通过注解的视图解析器则使用全限定类名作为标志名进行注册。
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegisterName {

	String value();
}
