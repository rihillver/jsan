package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义控制器上映射方法的形参上标识此参数为 Session 对象的注解。
 *
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionObject {

	/**
	 * 可指定 session 的属性名，默认没有指定则按形参名作为属性名来获取 session 对象，
	 * 
	 * @return
	 */
	String value() default "";
}
