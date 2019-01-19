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
 * <p>
 * 将表单字段转换成 Bean 的逻辑是通过 writeMethod 来注入，而非通过 writeMethodBaseOnField 来注入，因此只要有相应的 writeMethod（即setter方法） 与表单字段相对应即可正确转换。
 * 
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormConvert {
	
	/**
	 * 声明是否使用动态代理的方式，仅对于 Bean 的转换类型才有效，对于 Map 的转换类型无效。
	 * 
	 * @return
	 */
	boolean proxy() default false;

	/**
	 * 是否处理客户端对参数以深度序列化的形式提交请求参数的情况进行有效的特殊转换。
	 * <p>
	 * 注：例如客户端通过jQuery以ajax的方式直接以json对象作为data提交时，jQuery默认将对json对象进行深度序列化并转换成比较特殊的方式表示键值对，如items[]=[1,2,3]、obj[a]=testa、obj[b][c]=testb等，因为jQuery默认调用jQuery.param序列化参数，jQuery.param(obj, traditional)默认情况下traditional为false，即jquery会深度序列化参数对象，以适应如PHP和Ruby on Rails框架，但servelt无法处理，因此可以通过该注解进行识别转换。
	 * 
	 * @return
	 */
	boolean deep() default false;

	/**
	 * 指定参与表单转换的参数。
	 * 
	 * @return
	 */
	String[] params() default {};
	
}
