package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jsan.mvc.json.JsonParserConfigurator;

/**
 * 控制器上映射方法形参上的 Request Payload 数据（通常为json字符串形式）转换的注解。
 * <p>
 * 当客户端请求的 Content-Type 为 text/plain、application/json 等情况时，请求表单参数则在 Request Payload 中，此时服务端需要通过 request.getReader() 或 request.getInputStream() 来获取数据， 通过该注解可以将这些表单数据转换到对应的形参上。
 * <p>
 * 注：<br>
 * 1、基于 Fastjson 进行 json 的转换。<br>
 * 2、形参类型为 String、JSONObject、JSONArray 时将直接以对应的对象返回，不会做更多的转换。
 * 
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PayloadConvert {

	Class<? extends JsonParserConfigurator> value() default JsonParserConfigurator.class;

	/**
	 * 声明是否使用动态代理的方式，仅对于Bean的类型转换才有效，当该值设为true时，请务必确保是对于Bean的转换，否则可能导致转换错误，因为此时的转换逻辑是先通过Fastjson将字符串转换成Map（JSONObject），再将Map（JSONObject）转成Bean。
	 * 
	 * @return
	 */
	boolean proxy() default false;

}
