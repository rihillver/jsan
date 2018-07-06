package com.jsan.dao;

import java.sql.Connection;
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


public interface SqlxModel {
	
	void transactionBegin();

	void transactionBegin(int transactionIsolationLevel);

	void transactionCommit() throws SQLException;

	void transactionRollback();
	
	
	Connection getConnection(); // 一般情况下不建议使用，更不要在单例的情况下使用，除非在有准确把握的情况下

	void setConnection(Connection connection); // 一般情况下不建议使用，更不要在单例的情况下使用，除非在有准确把握的情况下

	
	Sqlx getSqlx();
	
	String getTableName();

	Param createParam();

	Param createParam(String sql);

	String createBeanDefinition() throws SQLException;
	
	
	// ==================================================
	
	
	boolean deleteById(Object... primaryValue) throws SQLException;

	boolean deleteByIds(String ids, Object... primaryValue) throws SQLException;

	
	// ==================================================
	
	
	int executeUpdate(String sql, Object... params) throws SQLException;

	int[] executeUpdateBatch(String sql, List<Object[]> params) throws SQLException;
	
	<T> T executeInsertBatch(String sql, ResultSetHandler<T> resultSetHandler, List<Object[]> params) throws SQLException;

	<T> T executeInsert(String sql, ResultSetHandler<T> resultSetHandler, Object... params) throws SQLException;
	
	<T> T executeQuery(String sql, ResultSetHandler<T> resultSetHandler, Object... params) throws SQLException;
	
	
	int insert(Param param) throws SQLException;

	int delete(Param param) throws SQLException;

	int update(Param param) throws SQLException;
	
	<T> T insert(Param param, Class<T> clazz) throws SQLException;
	
	
	int insert(String sql, Object... params) throws SQLException;
	
	int delete(String sql, Object... params) throws SQLException;
	
	int update(String sql, Object... params) throws SQLException;
	

	<T> T query(Param param, ResultSetHandler<T> resultSetHandler) throws SQLException;
	
	<T> T queryEnhanced(Param param, EnhancedResultSetHandler<T> enhancedResultSetHandler) throws SQLException;
	
	<T> T queryForObject(Param param, Class<T> clazz) throws SQLException;
	
	<T> T queryForRowCount(Param param, Class<T> clazz) throws SQLException;
	
	List<RowMetaData> queryForRowMetaData(Param param) throws SQLException;
	

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
	
	
	// ==================================================
	

	<T> T queriForBean(Param param, Class<T> beanClass) throws SQLException;

	<T> List<T> queriForBeanList(Param param, Class<T> beanClass) throws SQLException;

	<T> Page<T> queriForBeanPage(Param param, Class<T> beanClass) throws SQLException;
	
	<T> Page<T> queriForBeanPageEnhanced(Param param, BeanListHandler<T> beanListHandler) throws SQLException;
	
	
	<K, T> Map<K, T> queriForBeanKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass, String keyColumnName) throws SQLException;

	<T> Map<String, T> queriForBeanCombinationKeyedMap(Param param, Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException;

	<K, T> ListMultiValueMap<K, T> queriForBeanListMultiValueKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass, String keyColumnName) throws SQLException;

	<T> ListMultiValueMap<String, T> queriForBeanListMultiValueCombinationKeyedMap(Param param, Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException;

	<K, T> SetMultiValueMap<K, T> queriForBeanSetMultiValueKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass, String keyColumnName) throws SQLException;
	
	<T> SetMultiValueMap<String, T> queriForBeanSetMultiValueCombinationKeyedMap(Param param, Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException;
	

	// ==================================================


}
