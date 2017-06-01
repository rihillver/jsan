package com.jsan.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {

	/**
	 * 表名。
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 主键。
	 * 
	 * @return
	 */
	String[] key() default {};

	/**
	 * 自增键。
	 * 
	 * @return
	 */
	String[] autoKey() default {};

	/**
	 * 自增键值（比如 Oracle 的序列）。
	 * 
	 * @return
	 */
	String[] autoValue() default {};

}