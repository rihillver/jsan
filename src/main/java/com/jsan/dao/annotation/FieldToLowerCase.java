package com.jsan.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义字段名是否转换为小写的注解。
 * <p>
 * 注：例如 Oracle 数据库默认字段名全部大写，可通过该注解将结果集中的字段名转换为小写。
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FieldToLowerCase {

	/**
	 * 1、值为 true 时，则字段名转换成小写形式。<br>
	 * 2、值为 false 时（默认），不做任何特定的转换。
	 * 
	 * @return
	 */
	boolean value();
}
