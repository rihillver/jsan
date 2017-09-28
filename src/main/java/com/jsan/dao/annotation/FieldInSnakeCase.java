package com.jsan.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义字段名是否转换为下划线命名规范的注解。
 * <p>
 * 注：该注解只作用 Bean 形式的参数或结果集，不对 Map 形式的参数或结果集有任何影响。
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FieldInSnakeCase {

	/**
	 * 1、值为 true 时，则意味着字段名可能是含有下划线的命名，那么通过 Bean 作为参数来进行增删改时则将 Bean
	 * 字段的驼峰形式转换成下划线形式，同时对于查询的 Bean 结果集也同样将表字段的下划线形式转换成驼峰形式。<br>
	 * 2、值为 false 时（默认），不做任何特定的转换。<br>
	 * 
	 * @return
	 */
	boolean value();
}
