package com.jsan.dao;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jsan.convert.ConvertService;

/**
 * 该类及其子类均不推荐在单例下使用，非线程安全。
 *
 */

public class ModelBuilder extends MapModelBuilder implements Model {

	protected Map<String, Object> paramMap;
	protected Map<String, Boolean> orderByMap;

	protected Object[] primaryValue;
	protected int rowCount;
	protected String rowCountSql;

	@Override
	public Param createParam(String sql, int pageSize, int pageNumber) {

		Param param = super.createParam(sql, pageSize, pageNumber);

		param.setPrimaryValue(primaryValue);
		param.setOrderByMap(orderByMap);
		param.setRowCount(rowCount);
		// ==================================================
		// param.setRowCountSql(rowCountSql);
		// ==================================================

		return param;
	}

	// ==================================================

	@Override
	public Class<? extends Sqlx> getSqlxClass() {
		return sqlxClass;
	}

	@Override
	public void setSqlxClass(Class<? extends Sqlx> sqlxClass) {
		this.sqlxClass = sqlxClass;
	}

	@Override
	public String getDataSourceName() {
		return dataSourceName;
	}

	@Override
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	@Override
	public ConvertService getConvertService() {
		return convertService;
	}

	@Override
	public void setConvertService(ConvertService convertService) {
		this.convertService = convertService;
	}

	@Override
	public FieldHandler getFieldHandler() {
		return fieldHandler;
	}

	@Override
	public void setFieldHandler(FieldHandler fieldHandler) {
		this.fieldHandler = fieldHandler;
	}

	@Override
	public String[] getPrimaryKey() {
		return primaryKey;
	}

	@Override
	public void setPrimaryKey(String[] primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public String[] getAutoIncrementKey() {
		return autoIncrementKey;
	}

	@Override
	public void setAutoIncrementKey(String[] autoIncrementKey) {
		this.autoIncrementKey = autoIncrementKey;
	}

	@Override
	public String[] getAutoIncrementValue() {
		return autoIncrementValue;
	}

	@Override
	public void setAutoIncrementValue(String[] autoIncrementValue) {
		this.autoIncrementValue = autoIncrementValue;
	}

	@Override
	public String getTable() {
		return table;
	}

	@Override
	public void setTable(String table) {
		this.table = table;
	}

	@Override
	public String getTablePrefix() {
		return tablePrefix;
	}

	@Override
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	@Override
	public boolean isFieldInSnakeCase() {
		return fieldInSnakeCase;
	}

	@Override
	public void setFieldInSnakeCase(boolean fieldInSnakeCase) {
		this.fieldInSnakeCase = fieldInSnakeCase;
	}

	@Override
	public boolean isTableInSnakeCase() {
		return tableInSnakeCase;
	}

	@Override
	public void setTableInSnakeCase(boolean tableInSnakeCase) {
		this.tableInSnakeCase = tableInSnakeCase;
	}

	@Override
	public boolean isFieldToLowerCase() {
		return fieldToLowerCase;
	}

	@Override
	public void setFieldToLowerCase(boolean fieldToLowerCase) {
		this.fieldToLowerCase = fieldToLowerCase;
	}

	@Override
	public boolean isFieldCaseInsensitive() {
		return fieldCaseInsensitive;
	}

	@Override
	public void setFieldCaseInsensitive(boolean fieldCaseInsensitive) {
		this.fieldCaseInsensitive = fieldCaseInsensitive;
	}

	// ==================================================

	@Override
	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	@Override
	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

	@Override
	public Map<String, Boolean> getOrderByMap() {
		return orderByMap;
	}

	@Override
	public void setOrderByMap(Map<String, Boolean> orderByMap) {
		this.orderByMap = orderByMap;
	}

	@Override
	public void setOrderBy(String field, boolean desc) {

		if (orderByMap == null) {
			orderByMap = new LinkedHashMap<String, Boolean>();
		}
		if (orderByMap.containsKey(field)) {
			orderByMap.remove(field);
		}
		orderByMap.put(field, desc);
	}

	@Override
	public Object[] getPrimaryValue() {
		return primaryValue;
	}

	@Override
	public void setPrimaryValue(Object[] primaryValue) {
		this.primaryValue = primaryValue;
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	// ==================================================
	// @Override
	// public String getRowCountSql() {
	// return rowCountSql;
	// }
	//
	// @Override
	// public void setRowCountSql(String rowCountSql) {
	// this.rowCountSql = rowCountSql;
	// }
	// ==================================================

	// ==================================================

	@Override
	public void set(String key, Object value) {

		if (paramMap == null) {
			paramMap = new LinkedHashMap<String, Object>();
		}
		paramMap.put(key, value);
	}

	@Override
	public Map<String, Object> find() throws SQLException {

		Param param = createParam();
		return queryForMap(param);
	}

	@Override
	public boolean delete() throws SQLException {

		Param param = createParam();
		return delete(param) > 0;
	}

	@Override
	public boolean update() throws SQLException {

		Param param = createParam();
		return update(param) > 0;
	}

	@Override
	public boolean insert() throws SQLException {

		Param param = createParam();
		return insert(param) > 0;
	}

	@Override
	public int getInsert() throws SQLException {

		Param param = createParam();
		return insert(param, int.class);
	}

}
