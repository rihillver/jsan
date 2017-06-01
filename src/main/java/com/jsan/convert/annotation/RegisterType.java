package com.jsan.convert.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 专为 Converter 和 Formatter 设计的注解，用以标识转换器和格式化器的注册类型。
 * <p>
 * 没有使用该注解来指定注册类型的情况下：
 * <ul>
 * <li>Converter 接口通过 convert(Object, Type) 方法的返回值确定注册类型。</li>
 * <li>Formatter 接口通过 parse(String) 方法的返回值确定注册类型。</li>
 * </ul>
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegisterType {

	Class<?> value();
}