package com.jsan.dao;

import java.sql.SQLException;
import java.util.Map;

import com.jsan.convert.ConvertService;

public interface Model extends MapModel {
	

	// ==================================================
	
	Class<? extends Sqlx> getSqlxClass();

	void setSqlxClass(Class<? extends Sqlx> sqlxClass);

	String getDataSourceName();

	void setDataSourceName(String connecter);

	ConvertService getConvertService();

	void setConvertService(ConvertService convertService);
	
	FieldHandler getFieldHandler();
	
	void setFieldHandler(FieldHandler fieldHandler);

	String[] getPrimaryKey();

	void setPrimaryKey(String[] primaryKey);

	String[] getAutoIncrementKey();

	void setAutoIncrementKey(String[] autoIncrementKey);

	String[] getAutoIncrementValue();

	void setAutoIncrementValue(String[] autoIncrementValue);

	String getTable();

	void setTable(String table);

	String getTablePrefix();

	void setTablePrefix(String tablePrefix);

	boolean isFieldInSnakeCase();

	void setFieldInSnakeCase(boolean fieldInSnakeCase);

	boolean isTableInSnakeCase();

	void setTableInSnakeCase(boolean tableInSnakeCase);

	boolean isFieldToLowerCase();

	void setFieldToLowerCase(boolean fieldToLowerCase);

	boolean isFieldCaseInsensitive();

	void setFieldCaseInsensitive(boolean fieldCaseInsensitive);
	
	
	// ==================================================
	
	Map<String, Object> getParamMap();

	void setParamMap(Map<String, Object> paramMap);
	
	Map<String, Object> getOrderByMap();

	void setOrderByMap(Map<String, Object> orderByMap);
	
	void setOrderBy(String field, boolean order);

	Object[] getPrimaryValue();

	void setPrimaryValue(Object[] primaryValue);

	int getRowCount();

	void setRowCount(int rowCount);
	
	// ==================================================
	// String getRowCountSql();
	// void setRowCountSql(String rowCountSql);
	// ==================================================
	
	
	// ==================================================
	

	void set(String key, Object value);

	Map<String, Object> find() throws SQLException;

	boolean delete() throws SQLException;

	boolean update() throws SQLException;

	boolean insert() throws SQLException;

	int getInsert() throws SQLException;
	

	// ==================================================

}
