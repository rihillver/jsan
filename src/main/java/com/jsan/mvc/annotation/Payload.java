package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jsan.mvc.json.JsonParserConfigurator;

/**
 * 控制器上映射方法形参上的表单转换注解（Request Payload 方式）。
 * <p>
 * 当客户端请求的 Content-Type 为 text/plain、application/json等情况时，请求表单参数则在 Request Payload 中，此时服务端需要通过 request.getReader() 或 request.getInputStream() 来获取数据， 通过该注解可以将这些表单数据转换到对应的形参上。
 * 
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Payload {
	
	Class<? extends JsonParserConfigurator> value() default JsonParserConfigurator.class;
	
}
