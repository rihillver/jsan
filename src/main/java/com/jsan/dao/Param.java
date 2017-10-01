package com.jsan.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.jsan.convert.ConvertFuncUtils;
import com.jsan.convert.ConvertService;

/**
 * 封装 SQL 操作参数。
 *
 */

public class Param {

	private ConvertService convertService;
	private FieldNameHandler fieldNameHandler;
	private FieldValueHandler fieldValueHandler;

	private Map<String, Object> paramMap;
	private Map<String, Object> orderByMap;

	private String[] primaryKey;
	private Object[] primaryValue;

	private String[] autoIncrementKey;
	private String[] autoIncrementValue;

	private int pageSize;
	private int pageNumber;
	private Integer rowCount;

	private String sql;
	// ==================================================
	// private String rowCountSql;
	// ==================================================
	private String tableName; // 表名

	private boolean fieldInSnakeCase;
	private boolean tableInSnakeCase;
	private boolean fieldToLowerCase; // 结果集的字段名是否转换为小写，主要为了 Oracle
	private boolean fieldCaseInsensitive; // 结果集的字段名是否不区分大小写

	private boolean include;
	private String[] markFields;

	private String initializedSql;
	// ==================================================
	// private String initializedRowCountSql;
	// ==================================================
	private Object[] initializedParams;

	private int paramCount;

	public Param() {

	}

	public Param(String sql) {

		this.sql = sql;
	}

	public Param(int pageSize, int pageNumber) {

		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}

	public Param(String sql, int pageSize, int pageNumber) {

		this.sql = sql;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}

	public Param(String initializedSql, Object... initializedParams) {

		this.initializedSql = initializedSql;
		this.initializedParams = initializedParams;
	}

	public ConvertService getConvertService() {
		return convertService;
	}

	public void setConvertService(ConvertService convertService) {
		this.convertService = convertService;
	}

	public FieldNameHandler getFieldNameHandler() {
		return fieldNameHandler;
	}

	public void setFieldNameHandler(FieldNameHandler fieldNameHandler) {
		this.fieldNameHandler = fieldNameHandler;
	}

	public FieldValueHandler getFieldValueHandler() {
		return fieldValueHandler;
	}

	public void setFieldValueHandler(FieldValueHandler fieldValueHandler) {
		this.fieldValueHandler = fieldValueHandler;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

	public void set(Object object) {
		set(String.valueOf(++paramCount), object);
	}

	/**
	 * @param i
	 *            该参数从 1 开始
	 * @param object
	 */
	public void set(int i, Object object) {
		set(String.valueOf(i), object);
	}

	public void set(String name, Object object) {

		if (paramMap == null) {
			paramMap = new LinkedHashMap<String, Object>();
		}
		paramMap.put(name, object);
	}

	public Map<String, Object> getOrderByMap() {
		return orderByMap;
	}

	public void setOrderByMap(Map<String, Object> orderByMap) {
		this.orderByMap = orderByMap;
	}

	/**
	 * @param field
	 * @param order
	 *            null/asc/false/0 表示升序， desc/ture/1 表示降序
	 */
	public void setOrderBy(String field, Object order) {

		if (orderByMap == null) {
			orderByMap = new LinkedHashMap<String, Object>();
		}
		if (orderByMap.containsKey(field)) {
			orderByMap.remove(field);
		}
		orderByMap.put(field, order);
	}

	public String[] getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String[] primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Object[] getPrimaryValue() {
		return primaryValue;
	}

	public void setPrimaryValue(Object[] primaryValue) {
		this.primaryValue = primaryValue;
	}

	public String[] getAutoIncrementKey() {
		return autoIncrementKey;
	}

	public void setAutoIncrementKey(String[] autoIncrementKey) {
		this.autoIncrementKey = autoIncrementKey;
	}

	public String[] getAutoIncrementValue() {
		return autoIncrementValue;
	}

	public void setAutoIncrementValue(String[] autoIncrementValue) {
		this.autoIncrementValue = autoIncrementValue;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	// ==================================================
	// public String getRowCountSql() {
	// return rowCountSql;
	// }
	//
	// public void setRowCountSql(String rowCountSql) {
	// this.rowCountSql = rowCountSql;
	// }
	// ==================================================

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isFieldInSnakeCase() {
		return fieldInSnakeCase;
	}

	public void setFieldInSnakeCase(boolean fieldInSnakeCase) {
		this.fieldInSnakeCase = fieldInSnakeCase;
	}

	public boolean isTableInSnakeCase() {
		return tableInSnakeCase;
	}

	public void setTableInSnakeCase(boolean tableInSnakeCase) {
		this.tableInSnakeCase = tableInSnakeCase;
	}

	public boolean isFieldToLowerCase() {
		return fieldToLowerCase;
	}

	public void setFieldToLowerCase(boolean fieldToLowerCase) {
		this.fieldToLowerCase = fieldToLowerCase;
	}

	public boolean isFieldCaseInsensitive() {
		return fieldCaseInsensitive;
	}

	public void setFieldCaseInsensitive(boolean fieldCaseInsensitive) {
		this.fieldCaseInsensitive = fieldCaseInsensitive;
	}

	public boolean isInclude() {
		return include;
	}

	public void setInclude(boolean include) {
		this.include = include;
	}

	public String[] getMarkFields() {
		return markFields;
	}

	public void setMarkFields(String[] markFields) {
		this.markFields = markFields;
	}

	public String getInitializedSql() {
		return initializedSql;
	}

	public void setInitializedSql(String initializedSql) {
		this.initializedSql = initializedSql;
	}

	// ==================================================
	// public String getInitializedRowCountSql() {
	// return initializedRowCountSql;
	// }
	//
	// public void setInitializedRowCountSql(String initializedRowCountSql) {
	// this.initializedRowCountSql = initializedRowCountSql;
	// }
	// ==================================================

	public Object[] getInitializedParams() {
		return initializedParams;
	}

	public void setInitializedParams(Object[] initializedParams) {
		this.initializedParams = initializedParams;
	}

	/***********************************************************************/

	public String fixApos(String str) {

		if (str != null) {
			str = ConvertFuncUtils.parseAposFromSingleToDouble(str);
		}

		return str;
	}

	public String wrapApos(String str) {

		if (str != null) {
			str = "'" + str + "'";
		}

		return str;
	}

	public String wrapApos(String str, boolean fix) {

		if (fix) {
			str = fixApos(str);
		}

		return wrapApos(str);
	}

	public String wrapMate(String str) {

		if (str != null) {
			str = "%" + str + "%";
		}

		return str;
	}

	public String wrapMateApos(String str) {

		if (str != null) {
			str = "'%" + str + "%'";
		}

		return str;
	}

	public String wrapMateApos(String str, boolean fix) {

		if (fix) {
			str = fixApos(str);
		}

		return wrapMateApos(str);
	}

	public String wrapLeftMate(String str) {

		if (str != null) {
			str = "%" + str;
		}

		return str;
	}

	public String wrapLeftMateApos(String str) {

		if (str != null) {
			str = "'%" + str + "'";
		}

		return str;
	}

	public String wrapLeftMateApos(String str, boolean fix) {

		if (fix) {
			str = fixApos(str);
		}

		return wrapLeftMateApos(str);
	}

	public String wrapRightMate(String str) {

		if (str != null) {
			str = str + "%";
		}

		return str;
	}

	public String wrapRightMateApos(String str) {

		if (str != null) {
			str = "'" + str + "%'";
		}

		return str;
	}

	public String wrapRightMateApos(String str, boolean fix) {

		if (fix) {
			str = fixApos(str);
		}

		return wrapRightMateApos(str);
	}
}
