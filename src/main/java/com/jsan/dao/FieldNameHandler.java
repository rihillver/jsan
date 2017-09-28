package com.jsan.dao;

/**
 * 字段名处理器接口。
 *
 */

public interface FieldNameHandler {

	/**
	 * 字段名处理。
	 * <ul>
	 * <li>此处传入的 columnName 名并非原始的表列名，根据 Param
	 * 的参数设置还可能是转换成了小写的列名或者是转换成驼峰形式的列名。</li>
	 * </ul>
	 * 
	 * @param columnIndex
	 * @param columnName
	 * @param obj
	 * @return
	 */
	String handle(int columnIndex, String columnName, Object obj);

}
