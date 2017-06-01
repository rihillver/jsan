package com.jsan.dao;

/**
 * 字段处理器接口。
 *
 */

public interface FieldHandler {

	/**
	 * 字段处理。
	 * <ul>
	 * <li>此处传入的 columnName 名并非原始的表列名，而是转换成驼峰形式的列名，根据 Param
	 * 的参数设置还可能是转换成了小写的列名。</li>
	 * <li>此处传入的 columnName 也可能为 null，此时应该是 getObject(ResultSet, int,
	 * Class&lt;O&gt;, ConvertService) 方法调用。</li>
	 * </ul>
	 * 
	 * @param columnName
	 * @param obj
	 * @return
	 */
	Object handle(String columnName, Object obj);

}
