package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jsan.mvc.json.JsonParserConfigurator;

/**
 * 控制器上映射方法形参上的表单字段 json 转换注解。
 * 
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonConvert {

	Class<? extends JsonParserConfigurator> value() default JsonParserConfigurator.class;
}
