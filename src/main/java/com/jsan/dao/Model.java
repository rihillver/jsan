package com.jsan.dao;

import java.sql.SQLException;
import java.util.Map;

import com.jsan.convert.ConvertService;

public interface Model extends MapModel {
	

	// ==================================================
	
	Class<? extends Sqlx> fetchSqlxClass();

	void giveSqlxClass(Class<? extends Sqlx> sqlxClass);

	@SuppressWarnings("rawtypes")
	Class<? extends Page> fetchPageClass();

	@SuppressWarnings("rawtypes")
	void givePageClass(Class<? extends Page> pageClass);

	String fetchDataSourceName();

	void giveDataSourceName(String connecter);

	ConvertService fetchConvertService();

	void giveConvertService(ConvertService convertService);

	FieldNameHandler fetchFieldNameHandler();

	void giveFieldNameHandler(FieldNameHandler fieldNameHandler);

	FieldValueHandler fetchFieldValueHandler();

	void giveFieldValueHandler(FieldValueHandler fieldValueHandler);

	String[] fetchPrimaryKey();

	void givePrimaryKey(String[] primaryKey);

	String[] fetchAutoIncrementKey();

	void giveAutoIncrementKey(String[] autoIncrementKey);

	String[] fetchAutoIncrementValue();

	void giveAutoIncrementValue(String[] autoIncrementValue);

	void giveTableName(String tableName);

	boolean areFieldInSnakeCase();

	void giveFieldInSnakeCase(boolean fieldInSnakeCase);

	boolean areTableInSnakeCase();

	void giveTableInSnakeCase(boolean tableInSnakeCase);

	boolean areFieldToLowerCase();

	void giveFieldToLowerCase(boolean fieldToLowerCase);

	boolean areFieldCaseInsensitive();

	void giveFieldCaseInsensitive(boolean fieldCaseInsensitive);
	
	
	// ==================================================
	
	Map<String, Object> fetchParamMap();

	void giveParamMap(Map<String, Object> paramMap);
	
	Map<String, Object> fetchOrderByMap();

	void giveOrderByMap(Map<String, Object> orderByMap);
	
	void giveOrderBy(String field, Object order);

	Object[] fetchPrimaryValue();

	void givePrimaryValue(Object[] primaryValue);

	int fetchRowCount();

	void giveRowCount(int rowCount);
	
	
	// ==================================================
	

	void set(String key, Object value);

	Map<String, Object> find() throws SQLException;

	boolean delete() throws SQLException;

	boolean update() throws SQLException;

	boolean insert() throws SQLException;

	int gainInsert() throws SQLException;
	

	// ==================================================

}
