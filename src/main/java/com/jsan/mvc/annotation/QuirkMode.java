package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 兼容模式，即是否将含有下划线的表单字段转换成驼峰形式（对于将表单转换成 Map 时无效）。
 *
 */

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

@QuirkMode(false) // 定义一个默认值为false的特殊注解，以便 AbstractDispatcher 的 getQuirkModeAnnotation 方法获取。
public @interface QuirkMode {

	boolean value() default true;

}
