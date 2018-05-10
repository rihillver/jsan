package com.jsan.dao;

/**
 * 类型转换或字符编码转换处理器接口。
 *
 */

public interface TypeCastHandler {

	/**
	 * 处理输入，即 PreparedStatement.setObject(xxx) 前对执行 SQL 的参数对象进行类型转换或字符编码转换处理。
	 * 
	 * @param obj
	 * @return
	 */
	Object handleInput(Object obj);

	/**
	 * 处理输出，即 ResultSet.getObject(x) 后对获取到的对象进行类型转换或字符编码转换处理。
	 * 
	 * @param obj
	 * @return
	 */
	Object handeOutput(Object obj);

}
