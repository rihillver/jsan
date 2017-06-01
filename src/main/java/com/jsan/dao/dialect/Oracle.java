package com.jsan.dao.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jsan.dao.AbstractSqlx;
import com.jsan.dao.Param;
import com.jsan.dao.handler.ResultSetHandler;
import com.jsan.dao.handler.support.ObjectHandler;

public class Oracle extends AbstractSqlx {

	public Oracle() {

	}

	public Oracle(Connection connection) {

		super(connection);
	}

	public Oracle(ResultSet resultSet) {

		super(resultSet);
	}

	@Override
	protected String getPageSqlProcessed(String sql, int pageSize, int pageNumber) {

		// String pageSql = "select * from ( select temp__table__a__.*, rownum
		// row__num__ from ( select * from blog ) temp__table__a__ where rownum
		// <= ? ) temp__table__b__ where temp__table__b__.row__num__ > ?";

		int startRow = pageSize * (pageNumber - 1); // 开始行数
		int endRow = pageSize * pageNumber; // 结束行数

		StringBuilder sb = new StringBuilder();
		sb.append("select * from ( select temp__table__a__.*, rownum row__num__ from ( ");
		sb.append(sql);
		sb.append(" ) temp__table__a__ where rownum <= ");
		sb.append(endRow);
		sb.append(" ) temp__table__b__ where temp__table__b__.row__num__ > ");
		sb.append(startRow);

		return sb.toString();
	}

	/**
	 * 重写方法，使适用于 Oracle 数据库。
	 * 
	 * @param param
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> T insert(Param param, Class<T> clazz) throws SQLException {

		handleParam(param, Crud.INSERT);

		// ========================================
		String[] autoIncrementKey = param.getAutoIncrementKey();
		if (autoIncrementKey == null) {
			autoIncrementKey = param.getPrimaryKey();
		}
		return executeInsert(param.getInitializedSql(), getHandlerEnhancedProcessed(param, new ObjectHandler<T>(clazz)),
				autoIncrementKey, param.getInitializedParams());
		// ========================================
	}

	/**
	 * 改进使适用于 Oracle 数据库的方法（非重写父类方法，使用时不使用 Sqlx 接口）。
	 * 
	 * @param sql
	 * @param resultSetHandler
	 * @param autoIncrementKey
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T> T executeInsertBatch(String sql, ResultSetHandler<T> resultSetHandler, String[] autoIncrementKey,
			List<Object[]> params) throws SQLException {

		showSql("executeInsertBatch", sql, null);

		T generatedKeys = null;
		PreparedStatement stmt = null;
		try {
			// ========================================
			stmt = prepareStatementForInsert(sql, autoIncrementKey);
			// ========================================
			for (Object[] objects : params) {
				fillStatement(stmt, objects);
				stmt.addBatch();
			}
			stmt.executeBatch();
			ResultSet rs = stmt.getGeneratedKeys();
			generatedKeys = resultSetHandler.handle(rs);

		} catch (SQLException e) {
			rethrow(e, sql, null);
		} finally {
			close(stmt);
		}

		return generatedKeys;
	}

	/**
	 * 改进使适用于 Oracle 数据库的方法（非重写父类方法，使用时不使用 Sqlx 接口）。
	 * 
	 * @param sql
	 * @param resultSetHandler
	 * @param autoIncrementKey
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T> T executeInsert(String sql, ResultSetHandler<T> resultSetHandler, String[] autoIncrementKey,
			Object... params) throws SQLException {

		showSql("executeInsert", sql, params);

		T generatedKeys = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			// ========================================
			stmt = prepareStatementForInsert(sql, autoIncrementKey);
			// ========================================
			fillStatement(stmt, params);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			generatedKeys = resultSetHandler.handle(rs);
		} catch (SQLException e) {
			rethrow(e, sql, params);
		} finally {
			try {
				close(rs);
			} finally {
				close(stmt);
			}
		}

		return generatedKeys;
	}

	@Override
	protected void handleParamByAssembleForInsert(Param param) {

		// insert into user (id,name,sex,birth) values
		// (user_auto.nextval,?,?,?);

		String table = getTableProcessed(param);

		Map<String, Object> paramMap = getParamMapProcessed(param);

		// 对于已定义了 autoIncrementValue 的情况下，则把自增键和值重新放入参数 map
		// ========================================
		String[] autoIncrementKey = param.getAutoIncrementKey();
		String[] autoIncrementValue = param.getAutoIncrementValue();
		if (autoIncrementValue != null) {
			for (int i = 0; i < autoIncrementKey.length; i++) {
				paramMap.put(autoIncrementKey[i], autoIncrementValue[i]);
			}
		}
		// ========================================

		List<Object> paramList = new ArrayList<Object>();

		StringBuilder fieldBuilder = new StringBuilder();
		StringBuilder placeholderBuilder = new StringBuilder();

		int i = 0;
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			if (i++ > 0) {
				fieldBuilder.append(",");
				placeholderBuilder.append(",");
			}
			String field = getFieldProcessed(entry.getKey(), param);
			fieldBuilder.append(field);

			// ========================================
			Object value = entry.getValue();
			// if ((value instanceof String) && ((String)
			// value).matches("(?i).+\\.nextval$")) { // 避免使用正则表达式
			if ((value instanceof String) && ((String) value).trim().toLowerCase().endsWith(".nextval")) {
				placeholderBuilder.append(value);
			} else {
				placeholderBuilder.append("?");
				paramList.add(value);
			}
			// ========================================
		}

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into ");
		sqlBuilder.append(table);
		sqlBuilder.append(" (");
		sqlBuilder.append(fieldBuilder.toString());
		sqlBuilder.append(") values (");
		sqlBuilder.append(placeholderBuilder.toString());
		sqlBuilder.append(")");

		param.setInitializedSql(sqlBuilder.toString());
		param.setInitializedParams(paramList.toArray());
	}

}
