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
	 * 应用解析器，使用解析器的注册名，如果解析器的实现类没有定义注册名则使用其全限定类名。
	 * <p>
	 * 可以通过静态常量匹配的方式简化，例如：FORWARD、REDIRECT、HTML、TEXT、JSON、JSONP、STREAM、BYTE、JPG、PNG、GIF等。
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 视图 url。
	 * 
	 * @return
	 */
	String url() default "";

	/**
	 * 指定解析器，自定义解析器，优先级高于 value()。
	 * 
	 * @return
	 */
	Class<? extends Resolver> resolver() default Resolver.class;

}