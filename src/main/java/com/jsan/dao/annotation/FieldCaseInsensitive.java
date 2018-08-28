package com.jsan.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义字段名（即对于 Map 结果集的 key）是否大小写不敏感的注解。
 * <p>
 * 注：仅对于对 Map 结果集的情况。
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FieldCaseInsensitive {

	/**
	 * 1、值为 true 时，则 Map 结果集的 key 忽略大小写，即使用 CaseInsensitiveMap 来储存。<br>
	 * 2、值为 false 时（默认），即使用 LinkedHashMap 来储存。
	 * 
	 * @return
	 */
	boolean value();
}