package com.jsan.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jsan.convert.ConvertService;
import com.jsan.convert.annotation.ConvertServiceRegister;
import com.jsan.dao.annotation.Connecter;
import com.jsan.dao.annotation.FieldCaseInsensitive;
import com.jsan.dao.annotation.FieldHandlerRegister;
import com.jsan.dao.annotation.FieldToLowerCase;
import com.jsan.dao.annotation.FieldInSnakeCase;
import com.jsan.dao.annotation.FieldToCamelCase;
import com.jsan.dao.annotation.Table;
import com.jsan.dao.annotation.TablePrefix;
import com.jsan.dao.annotation.TableInSnakeCase;
import com.jsan.dao.handler.EnhancedResultSetHandler;
import com.jsan.dao.handler.ResultSetHandler;
import com.jsan.dao.map.ListMultiValueMap;
import com.jsan.dao.map.SetMultiValueMap;

public class SqlxModelBuilder implements SqlxModel {

	protected Connection connection;

	// 以下 9 个字段可以从注解定义

	protected Class<? extends Sqlx> sqlxClass;
	protected String dataSourceName;

	protected ConvertService convertService;
	protected FieldHandler fieldHandler;

	protected String[] primaryKey;

	protected String[] autoIncrementKey;
	protected String[] autoIncrementValue;

	protected String table;
	protected String tablePrefix;

	protected boolean fieldInSnakeCase;
	protected boolean tableInSnakeCase;
	protected boolean fieldToLowerCase;
	protected boolean fieldToCamelCase;
	protected boolean fieldCaseInsensitive;

	{
		initModelAnnotation();
	}

	private void initModelAnnotation() {

		Class<?> clazz = getClass();

		Connecter connecter = clazz.getAnnotation(Connecter.class);
		Table table = clazz.getAnnotation(Table.class);
		TablePrefix tablePrefix = clazz.getAnnotation(TablePrefix.class);
		FieldInSnakeCase fieldInSnakeCase = clazz.getAnnotation(FieldInSnakeCase.class);
		TableInSnakeCase tableInSnakeCase = clazz.getAnnotation(TableInSnakeCase.class);
		FieldToLowerCase fieldToLowerCase = clazz.getAnnotation(FieldToLowerCase.class);
		FieldToCamelCase fieldToCamelCase = clazz.getAnnotation(FieldToCamelCase.class);
		FieldCaseInsensitive fieldCaseInsensitive = clazz.getAnnotation(FieldCaseInsensitive.class);

		ConvertServiceRegister convertServiceRegister = clazz.getAnnotation(ConvertServiceRegister.class);
		FieldHandlerRegister fieldHandlerRegister = clazz.getAnnotation(FieldHandlerRegister.class);

		if (connecter != null) {
			if (!connecter.value().isEmpty()) {
				this.dataSourceName = connecter.value();
			}
			if (connecter.sqlx() != Sqlx.class) {
				this.sqlxClass = connecter.sqlx();
			}
		}

		if (table != null) {
			if (table.value().isEmpty()) {
				this.table = DaoFuncUtils.parseFirstCharToLowerCase(clazz.getSimpleName());
			} else {
				this.table = table.value();
			}
			if (table.key().length > 0) {
				this.primaryKey = table.key();
			}
			if (table.autoKey().length > 0) {
				this.autoIncrementKey = table.autoKey();
			}
			if (table.autoValue().length > 0) {
				this.autoIncrementValue = table.autoValue();
			}
		}

		if (tablePrefix != null) {
			if (!tablePrefix.value().isEmpty()) {
				this.tablePrefix = tablePrefix.value();
			}
		}

		if (fieldInSnakeCase != null) {
			this.fieldInSnakeCase = fieldInSnakeCase.value();
		}

		if (tableInSnakeCase != null) {
			this.tableInSnakeCase = tableInSnakeCase.value();
		}

		if (fieldToLowerCase != null) {
			this.fieldToLowerCase = fieldToLowerCase.value();
		}
		
		if (fieldToCamelCase != null) {
			this.fieldToCamelCase = fieldToCamelCase.value();
		}

		if (fieldCaseInsensitive != null) {
			this.fieldCaseInsensitive = fieldCaseInsensitive.value();
		}

		if (convertServiceRegister != null) {
			convertService = ModelConvertServiceCache.getConvertService(clazz, convertServiceRegister.value()); // 从缓存中获取
		}

		if (fieldHandlerRegister != null) {
			fieldHandler = FieldHandlerCache.getFieldHandler(clazz, fieldHandlerRegister.value()); // 从缓存中获取
		}

	}

	// ==================================================

	@Override
	public void transactionBegin() {

		DaoUtils.transactionBegin(dataSourceName);
	}

	@Override
	public void transactionBegin(int transactionIsolationLevel) {

		DaoUtils.transactionBegin(dataSourceName, transactionIsolationLevel);
	}

	@Override
	public void transactionCommit() throws SQLException {

		DaoUtils.transactionCommit();
	}

	@Override
	public void transactionRollback() {

		DaoUtils.transactionRollback();
	}

	/**
	 * 该方法一般情况下不建议使用。
	 * 
	 * @return
	 */
	@Override
	public Connection getConnection() {

		return connection;
	}

	/**
	 * 该方法一般情况下不建议使用，独立设置 Connection
	 * 的时候需要留意，不管其是否已关闭将不作任何判断，因此需要自行把握，一般用在你需要特殊的事务处理上，建议不要在单例模式下使用。
	 * <p>
	 * 在非事务即没有设置 connection.setAutoCommit(false)的情况下，connection 将可能会被第一个调用操作
	 * JDBC 的 Sqlx
	 * 方法执行完毕后关闭，因此使用该方法是应特别留意，因此建议不要在非事务的情况下使用该方法，普通的事务处理也尽量不要使用该方法。
	 * 
	 * @param connection
	 */
	@Override
	public void setConnection(Connection connection) {

		this.connection = connection;
	}

	@Override
	public Sqlx getSqlx() {

		Sqlx sqlx = null;

		try {
			if (sqlxClass == null) {
				sqlx = DaoConfig.getSqlxClass().newInstance();
			} else {
				sqlx = sqlxClass.newInstance();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// if (connection != null && !connection.isClosed()) {
		// connection 已关闭不作判断，让使用过程中因 connection 已关闭的错误明显的呈现
		if (connection != null) {
			sqlx.setConnection(connection);
		} else {
			sqlx.setConnection(DaoConfig.getConnection(dataSourceName));
		}

		return sqlx;
	}

	@Override
	public Param createParam() {

		return createParam(null);
	}

	@Override
	public Param createParam(String sql) {

		return createParam(sql, 0, 0);
	}

	@Override
	public Param createParam(int pageSize, int pageNumber) {

		return createParam(null, pageSize, pageNumber);
	}

	@Override
	public Param createParam(String sql, int pageSize, int pageNumber) {

		Param param = new Param(sql, pageSize, pageNumber);

		param.setConvertService(convertService);
		param.setFieldHandler(fieldHandler);
		param.setPrimaryKey(primaryKey);
		param.setAutoIncrementKey(autoIncrementKey);
		param.setAutoIncrementValue(autoIncrementValue);
		param.setTable(table);
		param.setTablePrefix(tablePrefix);
		param.setTableInSnakeCase(tableInSnakeCase);
		param.setFieldInSnakeCase(fieldInSnakeCase);
		param.setFieldToLowerCase(fieldToLowerCase);
		param.setFieldToCamelCase(fieldToCamelCase);
		param.setFieldCaseInsensitive(fieldCaseInsensitive);

		return param;
	}

	@Override
	public boolean deleteById(Object... primaryValue) throws SQLException {

		Param param = createParam();
		param.setPrimaryValue(primaryValue);

		Sqlx sqlx = getSqlx();
		int i = sqlx.delete(param);
		sqlx.close();

		return i > 0;
	}

	@Override
	public boolean deleteByIds(String ids, Object... primaryValue) throws SQLException {

		String[] fields = ids.split(",");

		Param param = createParam();
		for (int i = 0; i < fields.length; i++) {
			param.set(fields[i], primaryValue[i]);
		}

		Sqlx sqlx = getSqlx();
		int i = sqlx.delete(param);
		sqlx.close();

		return i > 0;
	}

	@Override
	public String buildBeanFieldDefinition() throws SQLException {

		Param param = new Param();
		param.setInitializedSql("select * from " + table + " where 1=2");
		List<RowMetaData> list = queryForRowMetaData(param);

		return DaoFuncUtils.buildBeanFieldDefinition(list, fieldToLowerCase);
	}

	@Override
	public int executeUpdate(String sql, Object... params) throws SQLException {

		Sqlx sqlx = getSqlx();
		int i = sqlx.executeUpdate(sql, params);
		sqlx.close();

		return i;
	}

	@Override
	public int[] executeUpdateBatch(String sql, List<Object[]> params) throws SQLException {

		Sqlx sqlx = getSqlx();
		int[] is = sqlx.executeUpdateBatch(sql, params);
		sqlx.close();

		return is;
	}

	@Override
	public <T> T executeInsertBatch(String sql, ResultSetHandler<T> resultSetHandler, List<Object[]> params)
			throws SQLException {

		Sqlx sqlx = getSqlx();
		T t = sqlx.executeInsertBatch(sql, resultSetHandler, params);
		sqlx.close();

		return t;
	}

	@Override
	public <T> T executeInsert(String sql, ResultSetHandler<T> resultSetHandler, Object... params) throws SQLException {

		Sqlx sqlx = getSqlx();
		T t = sqlx.executeInsert(sql, resultSetHandler, params);
		sqlx.close();

		return t;
	}

	@Override
	public <T> T executeQuery(String sql, ResultSetHandler<T> resultSetHandler, Object... params) throws SQLException {

		Sqlx sqlx = getSqlx();
		T t = sqlx.executeQuery(sql, resultSetHandler, params);
		sqlx.close();

		return t;
	}

	@Override
	public int insert(Param param) throws SQLException {

		Sqlx sqlx = getSqlx();
		int i = sqlx.insert(param);
		sqlx.close();

		return i;
	}

	@Override
	public int delete(Param param) throws SQLException {

		Sqlx sqlx = getSqlx();
		int i = sqlx.delete(param);
		sqlx.close();

		return i;
	}

	@Override
	public int update(Param param) throws SQLException {

		Sqlx sqlx = getSqlx();
		int i = sqlx.update(param);
		sqlx.close();

		return i;
	}

	@Override
	public <T> T insert(Param param, Class<T> clazz) throws SQLException {

		Sqlx sqlx = getSqlx();
		T t = sqlx.insert(param, clazz);
		sqlx.close();

		return t;
	}

	@Override
	public int insert(String sql, Object... params) throws SQLException {

		Param param = createParam();
		param.setInitializedSql(sql);
		param.setInitializedParams(params);
		return insert(param);
	}

	@Override
	public int delete(String sql, Object... params) throws SQLException {

		Param param = createParam();
		param.setInitializedSql(sql);
		param.setInitializedParams(params);
		return delete(param);
	}

	@Override
	public int update(String sql, Object... params) throws SQLException {

		Param param = createParam();
		param.setInitializedSql(sql);
		param.setInitializedParams(params);
		return update(param);
	}

	@Override
	public <T> T query(Param param, ResultSetHandler<T> resultSetHandler) throws SQLException {

		Sqlx sqlx = getSqlx();
		T t = sqlx.query(param, resultSetHandler);
		sqlx.close();

		return t;
	}

	@Override
	public <T> T queryEnhanced(Param param, EnhancedResultSetHandler<T> enhancedResultSetHandler) throws SQLException {

		Sqlx sqlx = getSqlx();
		T t = sqlx.queryEnhanced(param, enhancedResultSetHandler);
		sqlx.close();

		return t;
	}

	@Override
	public <T> T queryForRowCount(Param param, Class<T> clazz) throws SQLException {

		Sqlx sqlx = getSqlx();
		T t = sqlx.queryForRowCount(param, clazz);
		sqlx.close();

		return t;
	}

	@Override
	public List<RowMetaData> queryForRowMetaData(Param param) throws SQLException {

		Sqlx sqlx = getSqlx();
		List<RowMetaData> list = sqlx.queryForRowMetaData(param);
		sqlx.close();

		return list;
	}

	@Override
	public <K, V> Map<K, V> queryForPairKeyedMap(Param param, Class<K> keyClass, String keyColumnName,
			Class<V> valueClass, String valueColumnName) throws SQLException {

		Sqlx sqlx = getSqlx();
		Map<K, V> map = sqlx.queryForPairKeyedMap(param, keyClass, keyColumnName, valueClass, valueColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <V> Map<String, V> queryForPairCombinationKeyedMap(Param param, Class<V> valueClass, String valueColumnName,
			String separator, String... keyColumnNames) throws SQLException {

		Sqlx sqlx = getSqlx();
		Map<String, V> map = sqlx.queryForPairCombinationKeyedMap(param, valueClass, valueColumnName, separator,
				keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K, V> ListMultiValueMap<K, V> queryForPairListMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName, Class<V> valueClass, String valueColumnName) throws SQLException {

		Sqlx sqlx = getSqlx();
		ListMultiValueMap<K, V> map = sqlx.queryForPairListMultiValueKeyedMap(param, keyClass, keyColumnName,
				valueClass, valueColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <V> ListMultiValueMap<String, V> queryForPairListMultiValueCombinationKeyedMap(Param param,
			Class<V> valueClass, String valueColumnName, String separator, String... keyColumnNames)
			throws SQLException {

		Sqlx sqlx = getSqlx();
		ListMultiValueMap<String, V> map = sqlx.queryForPairListMultiValueCombinationKeyedMap(param, valueClass,
				valueColumnName, separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K, V> SetMultiValueMap<K, V> queryForPairSetMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName, Class<V> valueClass, String valueColumnName) throws SQLException {

		Sqlx sqlx = getSqlx();
		SetMultiValueMap<K, V> map = sqlx.queryForPairSetMultiValueKeyedMap(param, keyClass, keyColumnName, valueClass,
				valueColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <V> SetMultiValueMap<String, V> queryForPairSetMultiValueCombinationKeyedMap(Param param,
			Class<V> valueClass, String valueColumnName, String separator, String... keyColumnNames)
			throws SQLException {

		Sqlx sqlx = getSqlx();
		SetMultiValueMap<String, V> map = sqlx.queryForPairSetMultiValueCombinationKeyedMap(param, valueClass,
				valueColumnName, separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K> List<K> queryForKeyList(Param param, Class<K> keyClass, String keyColumnName) throws SQLException {

		Sqlx sqlx = getSqlx();
		List<K> list = sqlx.queryForKeyList(param, keyClass, keyColumnName);
		sqlx.close();

		return list;
	}

	@Override
	public <K> Set<K> queryForKeySet(Param param, Class<K> keyClass, String keyColumnName) throws SQLException {

		Sqlx sqlx = getSqlx();
		Set<K> set = sqlx.queryForKeySet(param, keyClass, keyColumnName);
		sqlx.close();

		return set;
	}

	@Override
	public List<String> queryForCombinationKeyList(Param param, String separator, String... keyColumnNames)
			throws SQLException {

		Sqlx sqlx = getSqlx();
		List<String> list = sqlx.queryForCombinationKeyList(param, separator, keyColumnNames);
		sqlx.close();

		return list;
	}

	@Override
	public Set<String> queryForCombinationKeySet(Param param, String separator, String... keyColumnNames)
			throws SQLException {

		Sqlx sqlx = getSqlx();
		Set<String> set = sqlx.queryForCombinationKeySet(param, separator, keyColumnNames);
		sqlx.close();

		return set;
	}

}
