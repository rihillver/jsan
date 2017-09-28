package com.jsan.dao;

/**
 * 字段值处理器接口。
 *
 */

public interface FieldValueHandler {

	/**
	 * 字段值处理。
	 * <ul>
	 * <li>此处传入的 columnName 名并非原始的表列名，根据 Param
	 * 的参数设置还可能是转换成了小写的列名或者是转换成驼峰形式的列名，而且也可能是经过 FieldNameHandler 处理后的表列名。</li>
	 * <li>此处传入的 columnName 也可能为 null，此时应该是 getObject(ResultSet, int,
	 * Class&lt;O&gt;, ConvertService) 方法调用。</li>
	 * <li>此处传入的 columnIndex 也可能为 0，此时应该是 getObject(ResultSet, String,
	 * Class&lt;O&gt;, ConvertService) 方法调用。</li>
	 * </ul>
	 * 
	 * @param columnIndex
	 * @param columnName
	 * @param obj
	 * @return
	 */
	Object handle(int columnIndex, String columnName, Object obj);

}
