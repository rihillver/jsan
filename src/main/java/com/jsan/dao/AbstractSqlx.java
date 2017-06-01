package com.jsan.dao;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jsan.convert.ConvertService;
import com.jsan.dao.handler.EnhancedResultSetHandler;
import com.jsan.dao.handler.ResultSetHandler;
import com.jsan.dao.handler.support.BeanHandler;
import com.jsan.dao.handler.support.BeanListHandler;
import com.jsan.dao.handler.support.MapHandler;
import com.jsan.dao.handler.support.MapListHandler;
import com.jsan.dao.handler.support.ObjectHandler;
import com.jsan.dao.handler.support.keyed.BeanCombinationKeyedHandler;
import com.jsan.dao.handler.support.keyed.BeanKeyedHandler;
import com.jsan.dao.handler.support.keyed.MapCombinationKeyedHandler;
import com.jsan.dao.handler.support.keyed.MapKeyedHandler;
import com.jsan.dao.handler.support.keyed.PairCombinationKeyedHandler;
import com.jsan.dao.handler.support.keyed.PairKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.list.BeanListMultiValueCombinationKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.list.BeanListMultiValueKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.list.MapListMultiValueCombinationKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.list.MapListMultiValueKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.list.PairListMultiValueCombinationKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.list.PairListMultiValueKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.set.BeanSetMultiValueCombinationKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.set.BeanSetMultiValueKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.set.MapSetMultiValueCombinationKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.set.MapSetMultiValueKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.set.PairSetMultiValueCombinationKeyedHandler;
import com.jsan.dao.handler.support.keyed.multivalue.set.PairSetMultiValueKeyedHandler;
import com.jsan.dao.map.ListMultiValueMap;
import com.jsan.dao.map.SetMultiValueMap;

/**
 * Sqlx 接口的抽象类。
 * <p>
 * 可通过构造方法的方式设置 ResultSet 来简化封装处理存储过程返回的 ResultSet 结果集的操作。
 *
 */

public abstract class AbstractSqlx implements Sqlx {

	protected abstract String getPageSqlProcessed(String sql, int pageSize, int pageNumber);

	protected static final String ORDER_BY_REGEX = "(?i)\\s+order\\s+by\\s+";

	protected enum Crud {
		INSERT, DELETE, UPDATE, QUERY
	}

	protected Connection connection;
	protected ResultSet resultSet;

	public AbstractSqlx() {

	}

	public AbstractSqlx(Connection connection) {

		this.connection = connection;
	}

	public AbstractSqlx(ResultSet resultSet) {

		this.resultSet = resultSet;
	}

	@Override
	public void setConnection(Connection connection) {

		this.connection = connection;
	}

	@Override
	public Connection getConnection() {

		return connection;
	}

	@Override
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	@Override
	public ResultSet getResultSet() {
		return resultSet;
	}

	@Override
	public void close() throws SQLException {

		close(connection);
	}

	protected void close(ResultSet rs) throws SQLException {

		if (rs != null) {
			rs.close();
		}
	}

	protected void close(Statement statement) throws SQLException {

		if (statement != null) {
			statement.close();
		}
	}

	protected void close(Connection connection) throws SQLException {

		if (connection != null && connection.getAutoCommit()) {
			// 如果使用事务的情况下，则不用在此关闭 Connection，由上级事务控制来关闭
			connection.close();
		}
	}

	protected PreparedStatement prepareStatement(String sql) throws SQLException {

		return connection.prepareStatement(sql);
	}

	protected PreparedStatement prepareStatementForInsert(String sql, String... columnNames) throws SQLException {

		if (columnNames != null && columnNames.length > 0) {
			return connection.prepareStatement(sql, columnNames); // for Oracle
		} else {
			return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		}
	}

	protected void fillStatement(PreparedStatement stmt, Object... params) throws SQLException {

		if (params == null || params.length == 0) {
			return;
		}

		ParameterMetaData pmd = null;
		try {
			pmd = stmt.getParameterMetaData();
		} catch (Exception e) {
			// 允许某些 JDBC 驱动不支持获取 ParameterMetaData
			// logging...
			// e.printStackTrace();
		}

		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) {
				stmt.setObject(i + 1, params[i]);
			} else {
				int sqlType = Types.VARCHAR;
				if (pmd != null) {
					try {
						sqlType = pmd.getParameterType(i + 1);
					} catch (Exception e) {
						// 异常的情况下则使用 Types.VARCHAR
						// logging...
						// e.printStackTrace();
					}
				}
				stmt.setNull(i + 1, sqlType);
			}
		}
	}

	protected ResultSet wrapResultSet(ResultSet rs) {

		return rs;
	}

	protected void rethrow(SQLException cause, String sql, Object[] params) throws SQLException {

		String causeMessage = cause.getMessage();

		if (causeMessage == null) {
			causeMessage = "";
		}

		StringBuilder msg = new StringBuilder(causeMessage);

		msg.append(" Query: ");
		msg.append(sql);
		msg.append(" Parameters: ");

		if (params == null) { // 批处理方法操作的时候传入的 params 为 null
			msg.append("[]");
		} else {
			msg.append(Arrays.deepToString(params));
		}

		SQLException e = new SQLException(msg.toString(), cause.getSQLState(), cause.getErrorCode());
		e.setNextException(cause);

		throw e;
	}

	protected void showSql(String type, String sql, Object[] params) {

		if (DaoConfig.isDebug()) {
			StringBuilder sb = new StringBuilder();
			sb.append(type);
			sb.append("\n");
			sb.append("----> ");
			sb.append(sql);
			sb.append("\n");
			sb.append("----> ");
			sb.append(params == null ? null : Arrays.toString(params));
			DaoConfig.printDebugMessage(sb.toString());
		}
	}

	@Override
	public int executeUpdate(String sql, Object... params) throws SQLException {

		showSql("executeUpdate", sql, params);

		int rows = 0;
		PreparedStatement stmt = null;

		try {
			stmt = prepareStatement(sql);
			fillStatement(stmt, params);
			rows = stmt.executeUpdate();
		} catch (SQLException e) {
			rethrow(e, sql, params);
		} finally {
			close(stmt);
		}

		return rows;
	}

	@Override
	public int[] executeUpdateBatch(String sql, List<Object[]> params) throws SQLException {

		showSql("executeUpdateBatch", sql, null);

		int[] rows = null;
		PreparedStatement stmt = null;
		try {
			stmt = prepareStatement(sql);
			for (Object[] objects : params) {
				fillStatement(stmt, objects);
				stmt.addBatch();
			}
			rows = stmt.executeBatch();

		} catch (SQLException e) {
			rethrow(e, sql, null);
		} finally {
			close(stmt);
		}

		return rows;
	}

	@Override
	public <T> T executeInsertBatch(String sql, ResultSetHandler<T> resultSetHandler, List<Object[]> params)
			throws SQLException {

		showSql("executeInsertBatch", sql, null);

		T generatedKeys = null;
		PreparedStatement stmt = null;
		try {
			stmt = prepareStatementForInsert(sql);
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

	@Override
	public <T> T executeInsert(String sql, ResultSetHandler<T> resultSetHandler, Object... params) throws SQLException {

		showSql("executeInsert", sql, params);

		T generatedKeys = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			stmt = prepareStatementForInsert(sql);
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
	public <T> T executeQuery(String sql, ResultSetHandler<T> resultSetHandler, Object... params) throws SQLException {

		if (resultSet != null) { // 当 resultSet 存在的时候直接处理 resultSet
			return resultSetHandler.handle(resultSet);
		}

		showSql("executeQuery", sql, params);

		T result = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			stmt = prepareStatement(sql);
			fillStatement(stmt, params);
			rs = wrapResultSet(stmt.executeQuery());
			result = resultSetHandler.handle(rs);
		} catch (SQLException e) {
			rethrow(e, sql, params);
		} finally {
			try {
				close(rs);
			} finally {
				close(stmt);
			}
		}

		return result;
	}

	protected String getTableProcessed(Param param) {

		String table = param.getTable();
		if (table == null) {
			return null;
		}

		if (param.isTableUnderlineNamed()) {
			table = DaoFuncUtils.parseToUnderlineString(table); // 转换为下划线命名规范
		}

		String tablePrefix = param.getTablePrefix();
		if (tablePrefix != null) {
			table = tablePrefix + table;
		}

		return table;
	}

	protected String getFieldProcessed(String field, Param param) {

		if (param.isFieldCaseInsensitive()) {
			return DaoFuncUtils.parseToUnderlineString(field);
		} else {
			return field;
		}
	}

	/**
	 * 该方法主要是除去自增字段，留下包含的字段，除去排除的字段。
	 * <p>
	 * <strong>这里对指定的字段名目前只能是表字段的实际字段名，是否要进行 下划线及驼峰的判断处理，后期再做决定。</strong>
	 * 
	 * @param param
	 * @return
	 */
	protected Map<String, Object> getParamMapProcessed(Param param) {

		Map<String, Object> map = new LinkedHashMap<String, Object>();

		Map<String, Object> paramMap = param.getParamMap();
		if (paramMap == null) {
			return map;
		}

		String[] autoIncrementKey = param.getAutoIncrementKey();
		String[] markFields = param.getMarkFields();
		boolean include = param.isInclude();

		if (include) { // 指定仅插入和更新字段的情况下
			if (markFields != null) {
				lable: for (String field : markFields) {
					if (autoIncrementKey != null) { // 如果存在自增键则除去
						for (String key : autoIncrementKey) {
							if (key.equals(field)) { // 是否要进行 下划线及驼峰的判断处理，后期再做决定
								continue lable;
							}
						}
					}
					map.put(field, paramMap.get(field));
				}
			}
		} else { // 普通情况下，如果有指定排除字段的则将其去掉
			lable: for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				if (autoIncrementKey != null) { // 如果存在自增键则除去
					for (String key : autoIncrementKey) {
						if (key.equals(entry.getKey())) { // 是否要进行下划线及驼峰的判断处理，后期再做决定
							continue lable;
						}
					}
				}
				if (markFields != null) {
					for (String field : markFields) {
						if (field.equals(entry.getKey())) { // 是否要进行下划线及驼峰的判断处理，后期再做决定
							continue lable;
						}
					}
				}
				map.put(entry.getKey(), entry.getValue());
			}
		}

		return map;
	}

	/**
	 * 拼装 insert 语句。
	 * <p>
	 * 需要拼装 insert 语句的环境主要有：insert、insertInc、getInsert、getInsertInc、以及 SqlxModel
	 * 的 inset() 和 getInsert()。
	 * 
	 * @param param
	 */
	protected void handleParamByAssembleForInsert(Param param) {

		// insert into user (name,sex,birth) values (?,?,?);

		String table = getTableProcessed(param);
		Map<String, Object> paramMap = getParamMapProcessed(param);

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
			placeholderBuilder.append("?");
			paramList.add(entry.getValue());
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

	/**
	 * 拼装 update 语句。
	 * <p>
	 * 需要拼装 insert 语句的环境主要有：update、updateInc 以及SqlxModel 的 update()。
	 * 
	 * @param param
	 */
	protected void handleParamByAssembleForUpdate(Param param) {

		// update user set sex=?,name=?,birth=? where id=?

		String table = getTableProcessed(param);
		Map<String, Object> paramMap = getParamMapProcessed(param);

		String[] primaryKey = param.getPrimaryKey();
		Object[] primaryValue = param.getPrimaryValue();
		if (primaryValue == null) {
			primaryValue = new Object[primaryKey.length];
		}
		int k = 0;
		for (String key : primaryKey) {
			Object primaryObject = param.getParamMap() == null ? null : param.getParamMap().get(key);
			if (primaryObject != null) {
				primaryValue[k++] = primaryObject; // 将主键字段的值保存
			}
			if (paramMap.containsKey(key)) {
				paramMap.remove(key); // 如果 paramMap 存在主键字段则除去
			}
		}

		Object[] params = new Object[paramMap.size() + primaryKey.length];

		StringBuilder fieldBuilder = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			if (i > 0) {
				fieldBuilder.append(",");
			}
			String field = getFieldProcessed(entry.getKey(), param);
			fieldBuilder.append(field);
			fieldBuilder.append("=?");
			params[i++] = entry.getValue();
		}
		StringBuilder conditionBuilder = new StringBuilder();

		for (int j = 0; j < primaryKey.length; j++) {
			if (j > 0) {
				conditionBuilder.append(" and ");
			}
			conditionBuilder.append(primaryKey[j]);
			conditionBuilder.append("=?");
			params[i++] = primaryValue[j];
		}

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update ");
		sqlBuilder.append(table);
		sqlBuilder.append(" set ");
		sqlBuilder.append(fieldBuilder.toString());
		sqlBuilder.append(" where ");
		sqlBuilder.append(conditionBuilder.toString());

		param.setInitializedSql(sqlBuilder.toString());
		param.setInitializedParams(params);

	}

	/**
	 * 拼装 query 语句。
	 * <p>
	 * 需要拼装 query 语句的环境主要有：findById、findByIds，其他的 find
	 * 方法都是找到主键值或其他条件后通过前面的两个再进行查询操作。
	 * 
	 * @param param
	 */
	protected void handleParamByAssembleForQuery(Param param) {

		// select * from user
		// select * from user where id=? and name=?

		String table = getTableProcessed(param);

		String sqlPrefix = "select * from " + table + " where ";

		handleParamByAssembleForDeleteAndQuery(sqlPrefix, param);
	}

	/**
	 * 拼装 delete 语句。
	 * <p>
	 * 需要拼装 delete 语句的环境主要有：deleteById、deleteByIds，其他的 delete
	 * 方法都是找到主键值或其他条件后通过前面的两个再进行删除操作。
	 * 
	 * @param param
	 */
	protected void handleParamByAssembleForDelete(Param param) {

		// delete from user where id=?
		// delete from user where sex=? and name=?

		String table = getTableProcessed(param);

		String sqlPrefix = "delete from " + table + " where ";

		handleParamByAssembleForDeleteAndQuery(sqlPrefix, param);
	}

	/**
	 * 拼装 delete 和 query 的条件语句（即 where 后面的条件语句）。
	 * 
	 * @param sqlPrefix
	 * @param param
	 */
	protected void handleParamByAssembleForDeleteAndQuery(String sqlPrefix, Param param) {

		Object[] params = null;

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(sqlPrefix);

		String[] primaryKey = param.getPrimaryKey();
		Object[] primaryValue = param.getPrimaryValue();
		Map<String, Object> paramMap = param.getParamMap();

		if (primaryKey != null && primaryValue != null) { // deleteById、findById
			params = new Object[primaryKey.length];
			for (int i = 0; i < primaryKey.length; i++) {
				if (i > 0) {
					sqlBuilder.append(" and ");
				}
				String field = getFieldProcessed(primaryKey[i], param);
				sqlBuilder.append(field);
				sqlBuilder.append("=?");
				params[i] = primaryValue[i];
			}
		} else if (paramMap != null && paramMap.size() > 0) { // deleteByIds、findByIds
			params = new Object[paramMap.size()];
			int i = 0;
			for (Map.Entry<String, Object> entry : param.getParamMap().entrySet()) {
				if (i > 0) {
					sqlBuilder.append(" and ");
				}
				String field = getFieldProcessed(entry.getKey(), param);
				sqlBuilder.append(field);
				sqlBuilder.append("=?");
				params[i++] = entry.getValue();
			}
		} else {
			// 抛出异常：该删除或查询操作没有指定主键值或条件参数值
			throw new RuntimeException("the delete or query statement doesn't specify a parameter value");
		}

		param.setInitializedSql(sqlBuilder.toString());
		param.setInitializedParams(params);
	}

	/**
	 * 对冒号(:xxx)和问号(?)('?')这些占位符进行替换校正，以及 order by 后面的字段处理。
	 * <p>
	 * 如果行数统计的 rowCountSql 如果设置有的情况，同样按照标准 sql 一样的校正流程，目前考虑到使用场景较少已注释掉。
	 * 
	 * @param param
	 * @param crud
	 */
	protected void handleParamByCorrect(Param param, Crud crud) {

		String sql = param.getSql();
		// ==================================================
		// String rowCountSql = param.getRowCountSql();
		// ==================================================

		// sql 语句前缀判断处理，无前缀的加上前缀
		sql = getSqlPrefixProcessed(sql, getTableProcessed(param), crud);
		// ==================================================
		// if (rowCountSql != null) {
		// rowCountSql = getSqlPrefixProcessed(rowCountSql,
		// getTableProcessed(param), crud);
		// }
		// ==================================================

		Map<String, Object> paramMap = param.getParamMap();
		List<Object> paramList = new ArrayList<Object>();
		List<Object> paramList0 = new ArrayList<Object>();

		// :xxx 形式的处理

		sql += " "; // 加上空格为了更好的进行 ':xxx' 形式的处理（处于最尾处时）
		// ==================================================
		// if (rowCountSql != null) {
		// rowCountSql += " ";
		// }
		// ==================================================

		Pattern pattern = Pattern.compile("'?:\\w+'?[\\s|\\)|,]");
		Matcher matcher = pattern.matcher(sql);

		while (matcher.find()) {
			String str = matcher.group();
			String name = str.replaceAll("'?:(\\w+)'?[\\s|\\)|,]", "$1");
			sql = sql.replaceFirst(":" + name, "?");
			// ==================================================
			// if (rowCountSql != null) {
			// rowCountSql = rowCountSql.replaceFirst(":" + name, "?");
			// }
			// ==================================================
			paramList.add(paramMap == null ? null : paramMap.get(name));
		}

		// ?、'?' 形式的处理

		pattern = Pattern.compile("'?\\?'?");
		matcher = pattern.matcher(sql);
		int i = 0;

		while (matcher.find()) {
			Object obj;
			if (paramList.size() > 0) {
				obj = paramList.get(i);
			} else {
				obj = paramMap == null ? null : paramMap.get(String.valueOf(i + 1));
			}
			if (matcher.group().equals("'?'")) {
				String str;
				if (obj != null) {
					str = obj.toString();
				} else {
					str = "null"; // 如果参数为 null ，则用字符串形式的 "null" 替换。
				}
				sql = sql.replaceFirst("'\\?'", str);
				// ==================================================
				// if (rowCountSql != null) {
				// rowCountSql = rowCountSql.replaceFirst("'\\?'", str);
				// }
				// ==================================================
			} else {
				paramList0.add(obj);
			}
			i++;
		}

		Object[] params = paramList0.toArray();

		// order by 的处理（处理下划线字段）
		sql = getOrderByProcessed(sql, param);
		// ==================================================
		// if (rowCountSql != null) {
		// rowCountSql = getOrderByProcessed(rowCountSql, param);
		// }
		// ==================================================

		param.setInitializedSql(sql);
		// ==================================================
		// if (rowCountSql != null) {
		// param.setInitializedRowCountSql(rowCountSql);
		// }
		// ==================================================
		param.setInitializedParams(params);
	}

	protected int getLastOrderByOffset(String sql) {

		int offset = -1;
		Pattern pattern = Pattern.compile(ORDER_BY_REGEX);
		Matcher matcher = pattern.matcher(sql);
		while (matcher.find()) {
			offset = matcher.start();
		}

		if (offset > sql.lastIndexOf(')')) {
			return offset;
		} else {
			return -1;
		}
	}

	protected String getOrderByProcessed(String sql, Param param) {

		Map<String, Boolean> orderByMap = param.getOrderByMap();
		if (orderByMap != null) {

			int offset = getLastOrderByOffset(sql);

			Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
			if (offset > 0) {
				String orderByStr = sql.substring(offset);
				sql = sql.substring(0, offset);
				orderByStr = orderByStr.replaceAll(ORDER_BY_REGEX, "");

				for (String str : orderByStr.split(",")) {
					str = str.trim();
					if (str.toLowerCase().contains(" desc")) {
						map.put(str.substring(0, str.indexOf(' ')), true);
					} else {
						int i = str.indexOf(' ');
						if (i > 0) {
							map.put(str.substring(0, str.indexOf(' ')), false);
						} else {
							map.put(str, false);
						}
					}
				}

				// 能不用正则表达式则尽量不用
				// for (String str : orderByStr.split(",")) {
				// str = str.trim();
				// if (str.matches("(?i)^\\w+\\s+asc$")) {
				// map.put(str.substring(0, str.indexOf(' ')), false);
				// } else if (str.matches("(?i)^\\w+\\s+desc$")) {
				// map.put(str.substring(0, str.indexOf(' ')), true);
				// } else {
				// map.put(str, false);
				// }
				// }

			}

			map.putAll(orderByMap);

			StringBuilder sb = new StringBuilder();
			sb.append(sql);
			sb.append(" order by ");

			int i = 0;
			for (Map.Entry<String, Boolean> entry : map.entrySet()) {
				if (i++ > 0) {
					sb.append(",");
				}
				String field = getFieldProcessed(entry.getKey(), param);
				sb.append(field);
				sb.append(" ");
				sb.append(entry.getValue() ? "desc" : "asc");
			}
			sql = sb.toString();
		}

		return sql;
	}

	protected String trimFirst(String str) {

		// 这里的 str 是不会为 null 的，所以不用判断 null

		int i = 0;
		while (str.charAt(i++) == ' ') {
		}

		return str.substring(i - 1);
	}

	protected String getSqlPrefixProcessed(String sql, String table, Crud crud) {

		String tmpSql = trimFirst(sql).substring(0, 6).toLowerCase(); // 去除空格后截取前6位，实际情况中sql语句必然是大于5位，否则将没有任何意义

		if (tmpSql.startsWith("where ")) {
			if (crud == Crud.QUERY) {
				sql = "select * from " + table + " " + sql;
			} else if (crud == Crud.DELETE) {
				sql = "delete from " + table + " " + sql;
			}
		} else if (tmpSql.startsWith("from ")) {
			if (crud == Crud.QUERY) {
				sql = "select * " + sql;
			} else if (crud == Crud.DELETE) {
				sql = "delete " + sql;
			}
		} else if (tmpSql.startsWith("order ")) { // order by
			if (crud == Crud.QUERY) {
				sql = "select * from " + table + " " + sql;
			}
		} else if (tmpSql.startsWith("set ")) {
			if (crud == Crud.UPDATE) {
				sql = "update " + table + " " + sql;
			}
		} else if (tmpSql.startsWith("(")) {
			if (crud == Crud.INSERT) {
				sql = "insert into " + table + " " + sql;
			}
		}

		// 避免使用正则表达式判断，上面的用法比使用正则表达式性能高 10 倍左右
		//
		// String whereRegex = "(?i)\\s*where\\s+.*";
		// String formRegex = "(?i)\\s*from\\s+.*";
		// String orderRegex = "(?i)\\s*order\\s+.*";
		// String setRegex = "(?i)\\s*set\\s+.*";
		// String bracketRegex = "(?i)\\s*\\(\\s+.*";
		//
		//
		// switch (crud) {
		// case QUERY:
		// if (sql.matches(whereRegex)) {
		// sql = "select * from " + table + " " + sql;
		// } else if (sql.matches(formRegex)) {
		// sql = "select * " + sql;
		// } else if(sql.matches(orderRegex)){
		// sql = "select * from " + table + " " + sql;
		// }
		// break;
		// case DELETE:
		// if (sql.matches(whereRegex)) {
		// sql = "delete from " + table + " " + sql;
		// } else if (sql.matches(formRegex)) {
		// sql = "delete " + sql;
		// }
		// break;
		// case UPDATE:
		// if (sql.matches(setRegex)) {
		// sql = "update " + table + " " + sql;
		// }
		// break;
		// case INSERT:
		// if (sql.matches(bracketRegex)) {
		// sql = "insert into " + table + " " + sql;
		// }
		// break;
		// default:
		// break;
		// }

		return sql;
	}

	/**
	 * 该方法内的相关判断使 sql = null 的情况不可能存在。
	 * <p>
	 * 当 conn.prepareStatement(null) 这种情况下可能导致 Java 虚拟机出现如下致命错误： A fatal error
	 * has been detected by the Java Runtime Environment ，因此不允许当 sql = null
	 * 的情况出现。
	 * 
	 * @param param
	 * @param crud
	 */
	protected void handleParam(Param param, Crud crud) {

		if (resultSet != null && crud == Crud.QUERY) { // resultSet不为null且同时又是查询操作的时后则不做接下去的判断处理
			return;
		}

		String initializedSql = param.getInitializedSql();
		if (initializedSql == null) {
			if (param.getSql() == null) {
				switch (crud) {
				case INSERT:
					handleParamByAssembleForInsert(param);
					break;
				case DELETE:
					handleParamByAssembleForDelete(param);
					break;
				case UPDATE:
					handleParamByAssembleForUpdate(param);
					break;
				case QUERY:
					handleParamByAssembleForQuery(param);
					break;
				}
			} else {
				handleParamByCorrect(param, crud);
			}
		} else {
			// ==================================================
			// 如果设置了 initializedRowCountSql 存在的情况，此处也无需对 initializedRowCountSql
			// 语句前缀作判断处理，因为如果使用 rowCountSql 的情况一定会是完整的查询语句的 sql
			// 语句前缀判断处理，无前缀的加上前缀
			// ==================================================
			initializedSql = getSqlPrefixProcessed(initializedSql, getTableProcessed(param), crud);
			param.setInitializedSql(initializedSql);
		}
	}

	@Override
	public int insert(Param param) throws SQLException {

		handleParam(param, Crud.INSERT);
		return executeUpdate(param.getInitializedSql(), param.getInitializedParams());
	}

	@Override
	public int delete(Param param) throws SQLException {

		handleParam(param, Crud.DELETE);
		return executeUpdate(param.getInitializedSql(), param.getInitializedParams());
	}

	@Override
	public int update(Param param) throws SQLException {

		handleParam(param, Crud.UPDATE);
		return executeUpdate(param.getInitializedSql(), param.getInitializedParams());
	}

	@Override
	public <T> T insert(Param param, Class<T> clazz) throws SQLException {

		handleParam(param, Crud.INSERT);
		return executeInsert(param.getInitializedSql(), getHandlerEnhancedProcessed(param, new ObjectHandler<T>(clazz)),
				param.getInitializedParams());
	}

	@Override
	public <T> T query(Param param, ResultSetHandler<T> resultSetHandler) throws SQLException {

		handleParam(param, Crud.QUERY);
		return executeQuery(param.getInitializedSql(), resultSetHandler, param.getInitializedParams());
	}

	/**
	 * 使用该方法时请留意不要忘记为 EnhancedResultSetHandler 实例设置
	 * convertService（必须）、fieldHandler、caseInsensitive、toLowerCase
	 * 这四个基本属性（如果有与默认值不一致的情况）。
	 * 
	 * @param param
	 * @param enhancedResultSetHandler
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> T queryEnhanced(Param param, EnhancedResultSetHandler<T> enhancedResultSetHandler) throws SQLException {

		handleParam(param, Crud.QUERY);
		return executeQuery(param.getInitializedSql(), getHandlerEnhancedProcessed(param, enhancedResultSetHandler),
				param.getInitializedParams());
	}

	@Override
	public <T> T queryForRowCount(Param param, Class<T> clazz) throws SQLException {

		handleParam(param, Crud.QUERY);
		String rowCountSql = getRowCountSqlProcessed(param);
		return executeQuery(rowCountSql, getHandlerEnhancedProcessed(param, new ObjectHandler<T>(clazz)),
				param.getInitializedParams());
	}

	protected <T> EnhancedResultSetHandler<T> getHandlerEnhancedProcessed(Param param,
			EnhancedResultSetHandler<T> extendedResultSetHandler) {

		ConvertService convertService = param.getConvertService();
		if (convertService == null) {
			convertService = DaoConfig.getConvertService();
		}
		extendedResultSetHandler.setConvertService(convertService);
		extendedResultSetHandler.setFieldHandler(param.getFieldHandler());
		;
		extendedResultSetHandler.setCaseInsensitive(param.isFieldCaseInsensitive());
		extendedResultSetHandler.setToLowerCase(param.isFieldToLowerCase());

		return extendedResultSetHandler;
	}

	protected String getRowCountSqlProcessed(Param param) {

		// ==================================================
		// 如果设置了 initializedRowCountSql 存在的情况，无需再做行数统计的拼装。
		// String sql = param.getInitializedRowCountSql();
		// if (sql != null) {
		// return sql;
		// }
		//
		// sql = param.getInitializedSql();
		// ==================================================

		String sql = param.getInitializedSql();

		int offset = getLastOrderByOffset(sql);
		if (offset > 0) {
			sql = sql.substring(0, offset);
		}

		sql = getRowCountSqlAssembleProcessed(sql);

		return sql;

	}

	protected String getRowCountSqlAssembleProcessed(String sql) {

		return "select count(*) from ( " + sql + " ) temp__table__";
	}

	@Override
	public Map<String, Object> queryForMap(Param param) throws SQLException {

		return queryEnhanced(param, new MapHandler());
	}

	@Override
	public List<Map<String, Object>> queryForMapList(Param param) throws SQLException {

		return queryEnhanced(param, new MapListHandler());
	}

	@Override
	public Page<Map<String, Object>> queryForMapPage(Param param) throws SQLException {

		Page<Map<String, Object>> page = new Page<Map<String, Object>>();
		handlePage(param, page, new MapListHandler());
		return page;
	}

	@Override
	public <T> T queryForBean(Param param, Class<T> beanClass) throws SQLException {

		return queryEnhanced(param, new BeanHandler<T>(beanClass));
	}

	@Override
	public <T> List<T> queryForBeanList(Param param, Class<T> beanClass) throws SQLException {

		return queryEnhanced(param, new BeanListHandler<T>(beanClass));
	}

	@Override
	public <T> Page<T> queryForBeanPage(Param param, Class<T> beanClass) throws SQLException {

		Page<T> page = new Page<T>();
		handlePage(param, page, new BeanListHandler<T>(beanClass));
		return page;
	}

	protected int getRowCountProcessed(Param param) throws SQLException {

		Integer rowCount = param.getRowCount();
		if (rowCount == null) {
			rowCount = queryForRowCount(param, int.class);
		}
		return rowCount;
	}

	protected <T> void handlePage(Param param, Page<T> page, EnhancedResultSetHandler<List<T>> enhancedResultSetHandler)
			throws SQLException {

		handleParam(param, Crud.QUERY);

		int rowCount = getRowCountProcessed(param);

		String sql = param.getInitializedSql();
		int pageCount = 0;
		int pageNumber = param.getPageNumber();
		int pageSize = param.getPageSize();
		if (pageSize > 0) {
			pageCount = (rowCount + pageSize - 1) / pageSize; // 计算总页数
			if (pageNumber > pageCount) {
				pageNumber = pageCount; // 当前页不能大于总页数（此处必须）
			}
			if (pageNumber < 1) {
				pageNumber = 1; // 当前页码不能小于1，至少为 1
			}
			sql = getPageSqlProcessed(sql, pageSize, pageNumber);
		}

		List<T> list = executeQuery(sql, getHandlerEnhancedProcessed(param, enhancedResultSetHandler),
				param.getInitializedParams());

		page.setList(list);
		page.setPageRowCount(list.size());
		page.setPageCount(pageCount);
		page.setRowCount(rowCount);
		page.setPageSize(pageSize);
		page.setPageNumber(pageNumber);

	}

	@Override
	public <K> Map<K, Map<String, Object>> queryForMapKeyedMap(Param param, Class<K> keyClass, String keyColumnName)
			throws SQLException {

		return queryEnhanced(param, new MapKeyedHandler<K>(keyClass, keyColumnName));
	}

	@Override
	public Map<String, Map<String, Object>> queryForMapCombinationKeyedMap(Param param, String separator,
			String... keyColumnNames) throws SQLException {

		return queryEnhanced(param, new MapCombinationKeyedHandler(separator, keyColumnNames));
	}

	@Override
	public <K> ListMultiValueMap<K, Map<String, Object>> queryForMapListMultiValueKeyedMap(Param param,
			Class<K> keyClass, String keyColumnName) throws SQLException {

		return queryEnhanced(param, new MapListMultiValueKeyedHandler<K>(keyClass, keyColumnName));
	}

	@Override
	public ListMultiValueMap<String, Map<String, Object>> queryForMapListMultiValueCombinationKeyedMap(Param param,
			String separator, String... keyColumnNames) throws SQLException {

		return queryEnhanced(param, new MapListMultiValueCombinationKeyedHandler(separator, keyColumnNames));
	}

	@Override
	public <K> SetMultiValueMap<K, Map<String, Object>> queryForMapSetMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName) throws SQLException {

		return queryEnhanced(param, new MapSetMultiValueKeyedHandler<K>(keyClass, keyColumnName));
	}

	@Override
	public SetMultiValueMap<String, Map<String, Object>> queryForMapSetMultiValueCombinationKeyedMap(Param param,
			String separator, String... keyColumnNames) throws SQLException {

		return queryEnhanced(param, new MapSetMultiValueCombinationKeyedHandler(separator, keyColumnNames));
	}

	@Override
	public <K, T> Map<K, T> queryForBeanKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass,
			String keyColumnName) throws SQLException {

		return queryEnhanced(param, new BeanKeyedHandler<K, T>(keyClass, beanClass, keyColumnName));
	}

	@Override
	public <T> Map<String, T> queryForBeanCombinationKeyedMap(Param param, Class<T> beanClass, String separator,
			String... keyColumnNames) throws SQLException {

		return queryEnhanced(param, new BeanCombinationKeyedHandler<T>(beanClass, separator, keyColumnNames));
	}

	@Override
	public <K, T> ListMultiValueMap<K, T> queryForBeanListMultiValueKeyedMap(Param param, Class<T> beanClass,
			Class<K> keyClass, String keyColumnName) throws SQLException {

		return queryEnhanced(param, new BeanListMultiValueKeyedHandler<K, T>(keyClass, beanClass, keyColumnName));
	}

	@Override
	public <T> ListMultiValueMap<String, T> queryForBeanListMultiValueCombinationKeyedMap(Param param,
			Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException {

		return queryEnhanced(param,
				new BeanListMultiValueCombinationKeyedHandler<T>(beanClass, separator, keyColumnNames));
	}

	@Override
	public <K, T> SetMultiValueMap<K, T> queryForBeanSetMultiValueKeyedMap(Param param, Class<T> beanClass,
			Class<K> keyClass, String keyColumnName) throws SQLException {

		return queryEnhanced(param, new BeanSetMultiValueKeyedHandler<K, T>(keyClass, beanClass, keyColumnName));
	}

	@Override
	public <T> SetMultiValueMap<String, T> queryForBeanSetMultiValueCombinationKeyedMap(Param param, Class<T> beanClass,
			String separator, String... keyColumnNames) throws SQLException {

		return queryEnhanced(param,
				new BeanSetMultiValueCombinationKeyedHandler<T>(beanClass, separator, keyColumnNames));
	}

	/**
	 * 若 keyColumnName 为 null 则取第一列的值，若 valueColumnName 为 null 则取第二列的值。
	 * 
	 * @param param
	 * @param keyClass
	 * @param keyColumnName
	 * @param valueClass
	 * @param valueColumnName
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <K, V> Map<K, V> queryForPairKeyedMap(Param param, Class<K> keyClass, String keyColumnName,
			Class<V> valueClass, String valueColumnName) throws SQLException {

		return queryEnhanced(param, new PairKeyedHandler<K, V>(keyClass, keyColumnName, valueClass, valueColumnName));
	}

	/**
	 * 若 separator 为 null 则无分隔符直接拼接 keyColumnNames。
	 * 
	 * @param param
	 * @param valueClass
	 * @param valueColumnName
	 * @param separator
	 * @param keyColumnNames
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <V> Map<String, V> queryForPairCombinationKeyedMap(Param param, Class<V> valueClass, String valueColumnName,
			String separator, String... keyColumnNames) throws SQLException {

		return queryEnhanced(param,
				new PairCombinationKeyedHandler<V>(valueClass, valueColumnName, separator, keyColumnNames));
	}

	@Override
	public <K, V> ListMultiValueMap<K, V> queryForPairListMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName, Class<V> valueClass, String valueColumnName) throws SQLException {

		return queryEnhanced(param,
				new PairListMultiValueKeyedHandler<K, V>(keyClass, keyColumnName, valueClass, valueColumnName));
	}

	@Override
	public <V> ListMultiValueMap<String, V> queryForPairListMultiValueCombinationKeyedMap(Param param,
			Class<V> valueClass, String valueColumnName, String separator, String... keyColumnNames)
			throws SQLException {

		return queryEnhanced(param, new PairListMultiValueCombinationKeyedHandler<V>(valueClass, valueColumnName,
				separator, keyColumnNames));
	}

	@Override
	public <K, V> SetMultiValueMap<K, V> queryForPairSetMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName, Class<V> valueClass, String valueColumnName) throws SQLException {

		return queryEnhanced(param,
				new PairSetMultiValueKeyedHandler<K, V>(keyClass, keyColumnName, valueClass, valueColumnName));
	}

	@Override
	public <V> SetMultiValueMap<String, V> queryForPairSetMultiValueCombinationKeyedMap(Param param,
			Class<V> valueClass, String valueColumnName, String separator, String... keyColumnNames)
			throws SQLException {

		return queryEnhanced(param, new PairSetMultiValueCombinationKeyedHandler<V>(valueClass, valueColumnName,
				separator, keyColumnNames));
	}
}