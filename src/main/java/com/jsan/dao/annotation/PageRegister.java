package com.jsan.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jsan.dao.Page;

/**
 * 分页实现注册注解。
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PageRegister {

	@SuppressWarnings("rawtypes")
	Class<? extends Page> value();

}
