package com.jsan.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义表名是否转换为下划线命名规范的注解。
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TableInSnakeCase {

	/**
	 * 1、值为 true 时，则意味着表名可能是含有下划线的命名，当没有明确通过 @table
	 * 注解指定表名的时候，则通过类名转换成小驼峰形式后再转换成下划线形式作为最终表名。<br>
	 * 2、值为 false 时（默认），当没有明确通过 @table 注解指定表名的时候，则通过类名转换成小驼峰形式作为最终表名。
	 * 
	 * @return
	 */
	boolean value();
}