package com.jsan.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MapModel extends SqlxModel {
	
	Map<String, Object> findById(Object... primaryValue) throws SQLException;

	Map<String, Object> findByIds(String ids, Object... primaryValue) throws SQLException;
	

	boolean delete(Map<String, Object> map) throws SQLException;

	boolean update(Map<String, Object> map, String... excludeFields) throws SQLException;

	boolean updateInc(Map<String, Object> map, String... includeFields) throws SQLException;

	boolean insert(Map<String, Object> map, String... excludeFields) throws SQLException;

	boolean insertInc(Map<String, Object> map, String... includeFields) throws SQLException;

	int gainInsert(Map<String, Object> map, String... excludeFields) throws SQLException;

	int gainInsertInc(Map<String, Object> map, String... includeFields) throws SQLException;
	

	Map<String, Object> queryFirst(String sql, Object... params) throws SQLException;

	List<Map<String, Object>> query(String sql, Object... params) throws SQLException;

	// Page<Map<String, Object>> paginate(int pageSize, int pageNumber, String sql, Object... params) throws SQLException;
	
	
//	Map<String, Object> queryForMap(Param param) throws SQLException;
//
//	List<Map<String, Object>> queryForMapList(Param param) throws SQLException;
//
//	Page<Map<String, Object>> queryForMapPage(Param param) throws SQLException;
//	
//	Page<Map<String, Object>> queryForMapPageEnhanced(Param param, MapListHandler mapListHandler) throws SQLException;
//	
//
//	<K> Map<K, Map<String, Object>> queryForMapKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;
//
//	Map<String, Map<String, Object>> queryForMapCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;
//
//	<K> ListMultiValueMap<K, Map<String, Object>> queryForMapListMultiValueKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;
//
//	ListMultiValueMap<String, Map<String, Object>> queryForMapListMultiValueCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;
//
//	<K> SetMultiValueMap<K, Map<String, Object>> queryForMapSetMultiValueKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;
//
//	SetMultiValueMap<String, Map<String, Object>> queryForMapSetMultiValueCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;
//	
//	
//	// ==================================================
//	
//
//	<T> T queryForBean(Param param, Class<T> beanClass) throws SQLException;
//
//	<T> List<T> queryForBeanList(Param param, Class<T> beanClass) throws SQLException;
//
//	<T> Page<T> queryForBeanPage(Param param, Class<T> beanClass) throws SQLException;
//	
//	<T> Page<T> queryForBeanPageEnhanced(Param param, BeanListHandler<T> beanListHandler) throws SQLException;
//	
//	
//	<K, T> Map<K, T> queryForBeanKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass, String keyColumnName) throws SQLException;
//
//	<T> Map<String, T> queryForBeanCombinationKeyedMap(Param param, Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException;
//
//	<K, T> ListMultiValueMap<K, T> queryForBeanListMultiValueKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass, String keyColumnName) throws SQLException;
//
//	<T> ListMultiValueMap<String, T> queryForBeanListMultiValueCombinationKeyedMap(Param param, Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException;
//
//	<K, T> SetMultiValueMap<K, T> queryForBeanSetMultiValueKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass, String keyColumnName) throws SQLException;
//	
//	<T> SetMultiValueMap<String, T> queryForBeanSetMultiValueCombinationKeyedMap(Param param, Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException;
//	
//
//	// ==================================================

}
