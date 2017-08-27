package com.jsan.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jsan.dao.handler.EnhancedResultSetHandler;
import com.jsan.dao.handler.ResultSetHandler;
import com.jsan.dao.handler.support.BeanListHandler;
import com.jsan.dao.handler.support.MapListHandler;
import com.jsan.dao.map.ListMultiValueMap;
import com.jsan.dao.map.SetMultiValueMap;

public interface Sqlx {

	void setConnection(Connection connection);

	Connection getConnection();
	
	void setResultSet(ResultSet resultSet);

	ResultSet getResultSet();

	void close() throws SQLException;

	int executeUpdate(String sql, Object... params) throws SQLException;

	int[] executeUpdateBatch(String sql, List<Object[]> params) throws SQLException;

	<T> T executeInsertBatch(String sql, ResultSetHandler<T> resultSetHandler, List<Object[]> params) throws SQLException;

	<T> T executeInsert(String sql, ResultSetHandler<T> resultSetHandler, Object... params) throws SQLException;

	<T> T executeQuery(String sql, ResultSetHandler<T> resultSetHandler, Object... params) throws SQLException;

	int insert(Param param) throws SQLException;

	int delete(Param param) throws SQLException;

	int update(Param param) throws SQLException;

	<T> T insert(Param param, Class<T> clazz) throws SQLException;

	<T> T query(Param param, ResultSetHandler<T> resultSetHandler) throws SQLException;
	
	<T> T queryEnhanced(Param param, EnhancedResultSetHandler<T> enhancedResultSetHandler) throws SQLException;

	<T> T queryForRowCount(Param param, Class<T> clazz) throws SQLException;
	
	List<RowMetaData> queryForRowMetaData(Param param) throws SQLException;
	
	// ==================================================
	

	Map<String, Object> queryForMap(Param param) throws SQLException;

	List<Map<String, Object>> queryForMapList(Param param) throws SQLException;

	Page<Map<String, Object>> queryForMapPage(Param param) throws SQLException;
	
	Page<Map<String, Object>> queryForMapPageEnhanced(Param param, MapListHandler mapListHandler) throws SQLException;
	

	<K> Map<K, Map<String, Object>> queryForMapKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;

	Map<String, Map<String, Object>> queryForMapCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;

	<K> ListMultiValueMap<K, Map<String, Object>> queryForMapListMultiValueKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;

	ListMultiValueMap<String, Map<String, Object>> queryForMapListMultiValueCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;

	<K> SetMultiValueMap<K, Map<String, Object>> queryForMapSetMultiValueKeyedMap(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;
	
	SetMultiValueMap<String, Map<String, Object>> queryForMapSetMultiValueCombinationKeyedMap(Param param, String separator, String... keyColumnNames) throws SQLException;

	

	<T> T queryForBean(Param param, Class<T> beanClass) throws SQLException;

	<T> List<T> queryForBeanList(Param param, Class<T> beanClass) throws SQLException;

	<T> Page<T> queryForBeanPage(Param param, Class<T> beanClass) throws SQLException;
	
	<T> Page<T> queryForBeanPageEnhanced(Param param, BeanListHandler<T> beanListHandler) throws SQLException;
	
	
	<K, T> Map<K, T> queryForBeanKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass, String keyColumnName) throws SQLException;

	<T> Map<String, T> queryForBeanCombinationKeyedMap(Param param, Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException;

	<K, T> ListMultiValueMap<K, T> queryForBeanListMultiValueKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass, String keyColumnName) throws SQLException;

	<T> ListMultiValueMap<String, T> queryForBeanListMultiValueCombinationKeyedMap(Param param, Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException;

	<K, T> SetMultiValueMap<K, T> queryForBeanSetMultiValueKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass, String keyColumnName) throws SQLException;
	
	<T> SetMultiValueMap<String, T> queryForBeanSetMultiValueCombinationKeyedMap(Param param, Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException;
	
	
	<K, V> Map<K, V> queryForPairKeyedMap(Param param, Class<K> keyClass, String keyColumnName, Class<V> valueClass, String valueColumnName) throws SQLException;

	<V> Map<String, V> queryForPairCombinationKeyedMap(Param param, Class<V> valueClass, String valueColumnName, String separator, String... keyColumnNames) throws SQLException;

	<K, V> ListMultiValueMap<K, V> queryForPairListMultiValueKeyedMap(Param param, Class<K> keyClass, String keyColumnName, Class<V> valueClass, String valueColumnName) throws SQLException;

	<V> ListMultiValueMap<String, V> queryForPairListMultiValueCombinationKeyedMap(Param param, Class<V> valueClass, String valueColumnName, String separator, String... keyColumnNames) throws SQLException;
	
	<K, V> SetMultiValueMap<K, V> queryForPairSetMultiValueKeyedMap(Param param, Class<K> keyClass, String keyColumnName, Class<V> valueClass, String valueColumnName) throws SQLException;
	
	<V> SetMultiValueMap<String, V> queryForPairSetMultiValueCombinationKeyedMap(Param param, Class<V> valueClass, String valueColumnName, String separator, String... keyColumnNames) throws SQLException;
	
	
	<K> List<K> queryForKeyList(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;

	<K> Set<K> queryForKeySet(Param param, Class<K> keyClass, String keyColumnName) throws SQLException;

	List<String> queryForCombinationKeyList(Param param, String separator, String... keyColumnNames) throws SQLException;

	Set<String> queryForCombinationKeySet(Param param, String separator, String... keyColumnNames) throws SQLException;

}
