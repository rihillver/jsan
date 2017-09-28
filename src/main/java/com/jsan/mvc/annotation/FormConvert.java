package com.jsan.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制器上映射方法形参上的表单转换注解。
 * <p>
 * <strong>当没有明确指定参与表单转换的参数时，通过将表单字段转换成 Bean 代理对象的方式是存在风险的，因为在 Dao 操作过程中（主要指
 * insert、update 操作）仅排除代理对象未被设置的字段，如果在 Dao 操作过程中（主要指 insert、update
 * 操作）没有显示排除一些需要保护的字段，那么提交的表单中如果存在（或者恶意的加上）这些字段，则这些字段就不会被排除掉，因此当没有明确指定参与表单转换的参数时，一些需要绝对保护的字段在
 * Dao 操作过程（主要指 insert、update 操作）中请务必显式的排除。</strong>
 * 
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormConvert {

	/**
	 * 指定参与表单转换的参数。
	 * 
	 * @return
	 */
	String[] params() default {};

	/**
	 * 声明是否使用动态代理的方式，仅对于 Bean 的转换类型才有效，对于 Map 的转换类型无效。
	 * 
	 * @return
	 */
	boolean value() default false;

	/**
	 * 兼容模式，即是否将含有下划线的表单字段转换成驼峰形式，仅对于 Bean 的转换类型才有效，对于 Map 的转换类型无效。
	 * 
	 * @return
	 */
	boolean quirkMode() default false;
}
