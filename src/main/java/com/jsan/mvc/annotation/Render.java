package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jsan.mvc.resolve.Resolver;

/**
 * 控制器上映射方法的转发注解。
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Render {

	/**
	 * 解析器名，解析器的全限定名（可以通过静态方法的方式简化，例如：FORWARD、REDIRECT、HTML、TEXT、JSON、JSONP、STREAM、BYTE、JPG、PNG、GIF
	 * 等）。
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 视图。
	 * 
	 * @return
	 */
	String url() default "";

	/**
	 * 解析器。
	 * 
	 * @return
	 */
	Class<? extends Resolver> resolver() default Resolver.class;

}