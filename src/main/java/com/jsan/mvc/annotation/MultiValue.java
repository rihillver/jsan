package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制器上映射方法形参上的表单取值方式注解。
 * <p>
 * 在方法参数上标识此参数是否返回原始数组，即返回 request.getParameterValues()，而不是返回
 * request.getParameterValues() 的第一个元素。
 *
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiValue {

	/**
	 * 可以指定多个字段值，一般用在把整个 form 转换成 Map 或 Bean 的情况下，指定的则直接返回原始数组（即
	 * request.getParameterValues() ），未指定的则取原始数组的第一个值，如果在没有指定任何字段默认空的情况下，只要有
	 * MultiValue 注解的则全部取原始数组。
	 * 
	 * @return
	 */
	String[] value() default {};
}