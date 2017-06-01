package com.jsan.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jsan.dao.Sqlx;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Connecter {

	/**
	 * 数据源标识名。
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * Sql 方言。
	 * 
	 * @return
	 */
	Class<? extends Sqlx> sqlx() default Sqlx.class;

}
