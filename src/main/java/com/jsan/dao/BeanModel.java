package com.jsan.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.jsan.dao.map.ListMultiValueMap;
import com.jsan.dao.map.SetMultiValueMap;

public interface BeanModel<B> extends SqlxModel {
	
	Class<B> getBeanClass();
	
	// ==================================================

	B findById(Object... primaryValue) throws SQLException;

	B findByIds(String ids, Object... primaryValue) throws SQLException;
	

	boolean delete(B bean) throws SQLException;

	boolean update(B bean, String... excludeFields) throws SQLException;

	boolean updateInc(B bean, String... includeFields) throws SQLException;

	boolean insert(B bean, String... excludeFields) throws SQLException;

	boolean insertInc(B bean, String... includeFields) throws SQLException;

	int getInsert(B bean, String... excludeFields) throws SQLException;

	int getInsertInc(B bean, String... includeFields) throws SQLException;
	

	B queryFirst(String sql, Object... params) throws SQLException;

	List<B> query(String sql, Object... params) throws SQLException;

	Page<B> paginate(int pageSize, int pageNumber, String sql, Object... params) throws SQLException;
	
	
	B queryForBean(Param param) throws SQLException;

	List<B> queryForBeanList(Param param) throws SQLException;

	Page<B> queryForBeanPage(Param param) throws SQLException;
	
	
	<K> Map<K, B> queryForBeanKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;

	Map<String, B> queryForBeanCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;

	<K> ListMultiValueMap<K, B> queryForBeanListMultiValueKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;

	ListMultiValueMap<String, B> queryForBeanListMultiValueCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;

	<K> SetMultiValueMap<K, B> queryForBeanSetMultiValueKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;

	SetMultiValueMap<String, B> queryForBeanSetMultiValueCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;


	// ==================================================
	
	
	Map<String, Object> queryForMap(Param param) throws SQLException;

	List<Map<String, Object>> queryForMapList(Param param) throws SQLException;

	Page<Map<String, Object>> queryForMapPage(Param param) throws SQLException;
	

	<K> Map<K, Map<String, Object>> queryForMapKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;

	Map<String, Map<String, Object>> queryForMapCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;

	<K> ListMultiValueMap<K, Map<String, Object>> queryForMapListMultiValueKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;

	ListMultiValueMap<String, Map<String, Object>> queryForMapListMultiValueCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;

	<K> SetMultiValueMap<K, Map<String, Object>> queryForMapSetMultiValueKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;

	SetMultiValueMap<String, Map<String, Object>> queryForMapSetMultiValueCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;
	
	
	// ==================================================
}
