package com.jsan.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.jsan.dao.handler.support.BeanListHandler;
import com.jsan.dao.handler.support.MapListHandler;
import com.jsan.dao.map.ListMultiValueMap;
import com.jsan.dao.map.SetMultiValueMap;

public class MapModelBuilder extends SqlxModelBuilder implements MapModel {

	@Override
	public Map<String, Object> findById(Object... primaryValue) throws SQLException {

		Param param = createParam();
		param.setPrimaryValue(primaryValue);

		Sqlx sqlx = getSqlx();
		Map<String, Object> map = sqlx.queryForMap(param);
		sqlx.close();

		return map;
	}

	@Override
	public Map<String, Object> findByIds(String ids, Object... primaryValue) throws SQLException {

		String[] fields = ids.split(",");

		Param param = createParam();
		for (int i = 0; i < fields.length; i++) {
			param.set(fields[i], primaryValue[i]);
		}

		Sqlx sqlx = getSqlx();
		Map<String, Object> map = sqlx.queryForMap(param);
		sqlx.close();

		return map;
	}

	/**
	 * 该方法只能够通过设置主键值来实现删除。
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public boolean delete(Map<String, Object> map) throws SQLException {

		Param param = createParam();
		String[] primaryKey = param.getPrimaryKey();
		Object[] primaryValue = new Object[primaryKey.length];
		for (int i = 0; i < primaryKey.length; i++) {
			primaryValue[i] = map.get(primaryKey[i]);
		}
		param.setPrimaryValue(primaryValue);

		return delete(param) > 0;
	}

	@Override
	public boolean update(Map<String, Object> map, String... excludeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(map);
		param.setMarkFields(excludeFields);

		return update(param) > 0;
	}

	@Override
	public boolean updateInc(Map<String, Object> map, String... includeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(map);
		param.setMarkFields(includeFields);
		param.setInclude(true);

		return update(param) > 0;
	}

	@Override
	public boolean insert(Map<String, Object> map, String... excludeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(map);
		param.setMarkFields(excludeFields);

		return insert(param) > 0;
	}

	@Override
	public boolean insertInc(Map<String, Object> map, String... includeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(map);
		param.setMarkFields(includeFields);
		param.setInclude(true);

		return insert(param) > 0;
	}

	@Override
	public int getInsert(Map<String, Object> map, String... excludeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(map);
		param.setMarkFields(excludeFields);

		return insert(param, int.class);
	}

	@Override
	public int getInsertInc(Map<String, Object> map, String... includeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(map);
		param.setMarkFields(includeFields);
		param.setInclude(true);

		return insert(param, int.class);
	}

	@Override
	public Map<String, Object> queryFirst(String sql, Object... params) throws SQLException {

		Param param = createParam();
		param.setInitializedSql(sql);
		param.setInitializedParams(params);
		return queryForMap(param);
	}

	@Override
	public List<Map<String, Object>> query(String sql, Object... params) throws SQLException {

		Param param = createParam();
		param.setInitializedSql(sql);
		param.setInitializedParams(params);
		return queryForMapList(param);
	}

	@Override
	public Page<Map<String, Object>> paginate(int pageSize, int pageNumber, String sql, Object... params)
			throws SQLException {

		Param param = createParam();
		param.setInitializedSql(sql);
		param.setInitializedParams(params);
		param.setPageSize(pageSize);
		param.setPageNumber(pageNumber);

		return queryForMapPage(param);
	}

	@Override
	public Map<String, Object> queryForMap(Param param) throws SQLException {

		Sqlx sqlx = getSqlx();
		Map<String, Object> map = sqlx.queryForMap(param);
		sqlx.close();

		return map;
	}

	@Override
	public List<Map<String, Object>> queryForMapList(Param param) throws SQLException {

		Sqlx sqlx = getSqlx();
		List<Map<String, Object>> list = sqlx.queryForMapList(param);
		sqlx.close();

		return list;
	}

	@Override
	public Page<Map<String, Object>> queryForMapPage(Param param) throws SQLException {

		Sqlx sqlx = getSqlx();
		Page<Map<String, Object>> page = sqlx.queryForMapPage(param);
		sqlx.close();

		return page;
	}

	@Override
	public Page<Map<String, Object>> queryForMapPageEnhanced(Param param, MapListHandler mapListHandler)
			throws SQLException {
		
		Sqlx sqlx = getSqlx();
		Page<Map<String, Object>> page = sqlx.queryForMapPageEnhanced(param, mapListHandler);
		sqlx.close();

		return page;
	}

	@Override
	public <K> Map<K, Map<String, Object>> queryForMapKeyedMap(Param param, Class<K> keyClass, String keyColumnName)
			throws SQLException {

		Sqlx sqlx = getSqlx();
		Map<K, Map<String, Object>> map = sqlx.queryForMapKeyedMap(param, keyClass, keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public Map<String, Map<String, Object>> queryForMapCombinationKeyedMap(Param param, String separator,
			String... keyColumnNames) throws SQLException {

		Sqlx sqlx = getSqlx();
		Map<String, Map<String, Object>> map = sqlx.queryForMapCombinationKeyedMap(param, separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K> ListMultiValueMap<K, Map<String, Object>> queryForMapListMultiValueKeyedMap(Param param,
			Class<K> keyClass, String keyColumnName) throws SQLException {

		Sqlx sqlx = getSqlx();
		ListMultiValueMap<K, Map<String, Object>> map = sqlx.queryForMapListMultiValueKeyedMap(param, keyClass,
				keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public ListMultiValueMap<String, Map<String, Object>> queryForMapListMultiValueCombinationKeyedMap(Param param,
			String separator, String... keyColumnNames) throws SQLException {

		Sqlx sqlx = getSqlx();
		ListMultiValueMap<String, Map<String, Object>> map = sqlx.queryForMapListMultiValueCombinationKeyedMap(param,
				separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K> SetMultiValueMap<K, Map<String, Object>> queryForMapSetMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName) throws SQLException {

		Sqlx sqlx = getSqlx();
		SetMultiValueMap<K, Map<String, Object>> map = sqlx.queryForMapSetMultiValueKeyedMap(param, keyClass,
				keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public SetMultiValueMap<String, Map<String, Object>> queryForMapSetMultiValueCombinationKeyedMap(Param param,
			String separator, String... keyColumnNames) throws SQLException {

		Sqlx sqlx = getSqlx();
		SetMultiValueMap<String, Map<String, Object>> map = sqlx.queryForMapSetMultiValueCombinationKeyedMap(param,
				separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <T> T queryForBean(Param param, Class<T> beanClass) throws SQLException {

		Sqlx sqlx = getSqlx();
		T t = sqlx.queryForBean(param, beanClass);
		sqlx.close();

		return t;
	}

	@Override
	public <T> List<T> queryForBeanList(Param param, Class<T> beanClass) throws SQLException {

		Sqlx sqlx = getSqlx();
		List<T> list = sqlx.queryForBeanList(param, beanClass);
		sqlx.close();

		return list;
	}

	@Override
	public <T> Page<T> queryForBeanPage(Param param, Class<T> beanClass) throws SQLException {

		Sqlx sqlx = getSqlx();
		Page<T> page = sqlx.queryForBeanPage(param, beanClass);
		sqlx.close();

		return page;
	}

	@Override
	public <T> Page<T> queryForBeanPageEnhanced(Param param, BeanListHandler<T> beanListHandler) throws SQLException {

		Sqlx sqlx = getSqlx();
		Page<T> page = sqlx.queryForBeanPageEnhanced(param, beanListHandler);
		sqlx.close();

		return page;
	}

	@Override
	public <K, T> Map<K, T> queryForBeanKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass,
			String keyColumnName) throws SQLException {

		Sqlx sqlx = getSqlx();
		Map<K, T> map = sqlx.queryForBeanKeyedMap(param, beanClass, keyClass, keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <T> Map<String, T> queryForBeanCombinationKeyedMap(Param param, Class<T> beanClass, String separator,
			String... keyColumnNames) throws SQLException {

		Sqlx sqlx = getSqlx();
		Map<String, T> map = sqlx.queryForBeanCombinationKeyedMap(param, beanClass, separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K, T> ListMultiValueMap<K, T> queryForBeanListMultiValueKeyedMap(Param param, Class<T> beanClass,
			Class<K> keyClass, String keyColumnName) throws SQLException {

		Sqlx sqlx = getSqlx();
		ListMultiValueMap<K, T> map = sqlx.queryForBeanListMultiValueKeyedMap(param, beanClass, keyClass,
				keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <T> ListMultiValueMap<String, T> queryForBeanListMultiValueCombinationKeyedMap(Param param,
			Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException {

		Sqlx sqlx = getSqlx();
		ListMultiValueMap<String, T> map = sqlx.queryForBeanListMultiValueCombinationKeyedMap(param, beanClass,
				separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K, T> SetMultiValueMap<K, T> queryForBeanSetMultiValueKeyedMap(Param param, Class<T> beanClass,
			Class<K> keyClass, String keyColumnName) throws SQLException {

		Sqlx sqlx = getSqlx();
		SetMultiValueMap<K, T> map = sqlx.queryForBeanSetMultiValueKeyedMap(param, beanClass, keyClass, keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <T> SetMultiValueMap<String, T> queryForBeanSetMultiValueCombinationKeyedMap(Param param, Class<T> beanClass,
			String separator, String... keyColumnNames) throws SQLException {

		Sqlx sqlx = getSqlx();
		SetMultiValueMap<String, T> map = sqlx.queryForBeanSetMultiValueCombinationKeyedMap(param, beanClass, separator,
				keyColumnNames);
		sqlx.close();

		return map;
	}

}
