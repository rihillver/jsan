package com.jsan.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MapModelBuilder extends SqlxModelBuilder implements MapModel {

	@Override
	public Map<String, Object> findById(Object... primaryValue) throws SQLException {

		Param param = createParam();
		param.setPrimaryValue(primaryValue);

		Sqlx sqlx = fetchSqlx();
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

		Sqlx sqlx = fetchSqlx();
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
	public int gainInsert(Map<String, Object> map, String... excludeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(map);
		param.setMarkFields(excludeFields);

		return insert(param, int.class);
	}

	@Override
	public int gainInsertInc(Map<String, Object> map, String... includeFields) throws SQLException {

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

//	@Override
//	public Page<Map<String, Object>> paginate(int pageSize, int pageNumber, String sql, Object... params)
//			throws SQLException {
//
//		Param param = createParam();
//		param.setInitializedSql(sql);
//		param.setInitializedParams(params);
//		param.setPageSize(pageSize);
//		param.setPageNumber(pageNumber);
//
//		return queryForMapPage(param);
//	}


}
