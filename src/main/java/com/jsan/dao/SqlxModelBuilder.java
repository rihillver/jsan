package com.jsan.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jsan.convert.ConvertFuncUtils;
import com.jsan.convert.ConvertService;
import com.jsan.convert.annotation.ConvertServiceRegister;
import com.jsan.dao.annotation.Connecter;
import com.jsan.dao.annotation.FieldCaseInsensitive;
import com.jsan.dao.annotation.FieldValueHandlerRegister;
import com.jsan.dao.annotation.PageRegister;
import com.jsan.dao.annotation.FieldInSnakeCase;
import com.jsan.dao.annotation.FieldNameHandlerRegister;
import com.jsan.dao.annotation.FieldToLowerCase;
import com.jsan.dao.annotation.Table;
import com.jsan.dao.annotation.TableInSnakeCase;
import com.jsan.dao.annotation.TypeCastHandlerRegister;
import com.jsan.dao.handler.EnhancedResultSetHandler;
import com.jsan.dao.handler.ResultSetHandler;
import com.jsan.dao.handler.support.BeanListHandler;
import com.jsan.dao.handler.support.MapListHandler;
import com.jsan.dao.map.ListMultiValueMap;
import com.jsan.dao.map.SetMultiValueMap;

public class SqlxModelBuilder implements SqlxModel {

	protected Connection $connection;

	// 以下14个字段可以从注解定义

	protected Class<? extends Sqlx> $sqlxClass;
	@SuppressWarnings("rawtypes")
	protected Class<? extends Page> $pageClass;
	protected String $dataSourceName;

	protected ConvertService $convertService;
	protected TypeCastHandler $typeCastHandler;
	protected FieldNameHandler $fieldNameHandler;
	protected FieldValueHandler $fieldValueHandler;

	protected String[] $primaryKey;

	protected String[] $autoIncrementKey;
	protected String[] $autoIncrementValue;

	protected String $tableName;

	protected boolean $fieldInSnakeCase;
	protected boolean $tableInSnakeCase;
	protected boolean $fieldToLowerCase;
	protected boolean $fieldCaseInsensitive;

	{
		initModelAnnotation();
	}

	private void initModelAnnotation() {

		Class<?> clazz = getClass();

		Connecter connecter = clazz.getAnnotation(Connecter.class);
		Table table = clazz.getAnnotation(Table.class);
		FieldInSnakeCase fieldInSnakeCase = clazz.getAnnotation(FieldInSnakeCase.class);
		TableInSnakeCase tableInSnakeCase = clazz.getAnnotation(TableInSnakeCase.class);
		FieldToLowerCase fieldToLowerCase = clazz.getAnnotation(FieldToLowerCase.class);
		FieldCaseInsensitive fieldCaseInsensitive = clazz.getAnnotation(FieldCaseInsensitive.class);

		PageRegister pageRegister = clazz.getAnnotation(PageRegister.class);
		ConvertServiceRegister convertServiceRegister = clazz.getAnnotation(ConvertServiceRegister.class);
		TypeCastHandlerRegister typeCastHandlerRegister = clazz.getAnnotation(TypeCastHandlerRegister.class);
		FieldNameHandlerRegister fieldNameHandlerRegister = clazz.getAnnotation(FieldNameHandlerRegister.class);
		FieldValueHandlerRegister fieldValueHandlerRegister = clazz.getAnnotation(FieldValueHandlerRegister.class);

		if (connecter != null) {
			if (!connecter.name().isEmpty()) {
				this.$dataSourceName = connecter.name();
			} else if (!connecter.value().isEmpty()) {
				this.$dataSourceName = connecter.value();
			}
			if (connecter.sqlx() != Sqlx.class) {
				this.$sqlxClass = connecter.sqlx();
			}
		}

		if (table != null) {
			if (!table.name().isEmpty()) {
				this.$tableName = table.name();
			} else if (!table.value().isEmpty()) {
				this.$tableName = table.value();
			}
			if (table.key().length > 0) {
				this.$primaryKey = table.key();
			}
			if (table.autoKey().length > 0) {
				this.$autoIncrementKey = table.autoKey();
			}
			if (table.autoValue().length > 0) {
				this.$autoIncrementValue = table.autoValue();
			}
		}

		if (fieldInSnakeCase != null) {
			this.$fieldInSnakeCase = fieldInSnakeCase.value();
		}

		if (tableInSnakeCase != null) {
			this.$tableInSnakeCase = tableInSnakeCase.value();
		}

		if (fieldToLowerCase != null) {
			this.$fieldToLowerCase = fieldToLowerCase.value();
		}

		if (fieldCaseInsensitive != null) {
			this.$fieldCaseInsensitive = fieldCaseInsensitive.value();
		}

		if (pageRegister != null) {
			$pageClass = pageRegister.value();
		} else {
			$pageClass = DaoConfig.getPageClass();
		}

		if (convertServiceRegister != null) {
			$convertService = ModelConvertServiceCache.getConvertService(clazz, convertServiceRegister.value()); // 从缓存中获取
		}

		if (typeCastHandlerRegister != null) {
			$typeCastHandler = TypeCastHandlerCache.getHandler(clazz, typeCastHandlerRegister.value()); // 从缓存中获取
		}
		
		if (fieldNameHandlerRegister != null) {
			$fieldNameHandler = FieldHandlerCache.getNameHandler(clazz, fieldNameHandlerRegister.value()); // 从缓存中获取
		}

		if (fieldValueHandlerRegister != null) {
			$fieldValueHandler = FieldHandlerCache.getValueHandler(clazz, fieldValueHandlerRegister.value()); // 从缓存中获取
		}

	}

	// ==================================================

	@Override
	public void transactionBegin() {

		DaoUtils.transactionBegin($dataSourceName);
	}

	@Override
	public void transactionBegin(int transactionIsolationLevel) {

		DaoUtils.transactionBegin($dataSourceName, transactionIsolationLevel);
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
	public Connection fetchConnection() {

		return $connection;
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
	public void giveConnection(Connection connection) {

		this.$connection = connection;
	}

	@Override
	public Sqlx fetchSqlx() {

		Sqlx sqlx = null;

		try {
			if ($sqlxClass == null) {
				sqlx = DaoConfig.getSqlxClass().newInstance();
			} else {
				sqlx = $sqlxClass.newInstance();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// if (connection != null && !connection.isClosed()) {
		// connection 已关闭不作判断，让使用过程中因 connection 已关闭的错误明显的呈现
		if ($connection != null) {
			sqlx.setConnection($connection);
		} else {
			sqlx.setConnection(DaoConfig.getConnection($dataSourceName));
		}
		
		sqlx.setTypeCastHandler($typeCastHandler); //注册 TypeCastHandler

		return sqlx;
	}

	@Override
	public String fetchTableName() {

		if ($tableName == null) {
			String name = ConvertFuncUtils.parseFirstCharToLowerCase(getClass().getSimpleName()); // 转换为小驼峰命名规范
			if ($tableInSnakeCase) {
				name = ConvertFuncUtils.parseCamelCaseToSnakeCase(name); // 转换为下划线命名规范
			}
			return name;
		}

		return $tableName;
	}

	@Override
	public Param createParam() {

		return createParam(null);
	}

	@Override
	public Param createParam(String sql) {

		Param param = new Param(sql);

		param.setConvertService($convertService);
		param.setTypeCastHandler($typeCastHandler);
		param.setFieldNameHandler($fieldNameHandler);
		param.setFieldValueHandler($fieldValueHandler);
		param.setPageClass($pageClass);
		param.setPrimaryKey($primaryKey);
		param.setAutoIncrementKey($autoIncrementKey);
		param.setAutoIncrementValue($autoIncrementValue);
		param.setTableName(fetchTableName());
		param.setTableInSnakeCase($tableInSnakeCase);
		param.setFieldInSnakeCase($fieldInSnakeCase);
		param.setFieldToLowerCase($fieldToLowerCase);
		param.setFieldCaseInsensitive($fieldCaseInsensitive);

		return param;
	}

	@Override
	public boolean deleteById(Object... primaryValue) throws SQLException {

		Param param = createParam();
		param.setPrimaryValue(primaryValue);

		Sqlx sqlx = fetchSqlx();
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

		Sqlx sqlx = fetchSqlx();
		int i = sqlx.delete(param);
		sqlx.close();

		return i > 0;
	}

	@Override
	public String createBeanDefinition() throws SQLException {

		Param param = new Param();
		param.setInitializedSql("select * from " + fetchTableName() + " where 1=2");
		List<RowMetaData> list = queryForRowMetaData(param);

		return DaoFuncUtils.createBeanDefinition(list, $fieldToLowerCase, $fieldInSnakeCase);
	}

	@Override
	public int executeUpdate(String sql, Object... params) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		int i = sqlx.executeUpdate(sql, params);
		sqlx.close();

		return i;
	}

	@Override
	public int[] executeUpdateBatch(String sql, List<Object[]> params) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		int[] is = sqlx.executeUpdateBatch(sql, params);
		sqlx.close();

		return is;
	}

	@Override
	public <T> T executeInsertBatch(String sql, ResultSetHandler<T> resultSetHandler, List<Object[]> params)
			throws SQLException {

		Sqlx sqlx = fetchSqlx();
		T t = sqlx.executeInsertBatch(sql, resultSetHandler, params);
		sqlx.close();

		return t;
	}

	@Override
	public <T> T executeInsert(String sql, ResultSetHandler<T> resultSetHandler, Object... params) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		T t = sqlx.executeInsert(sql, resultSetHandler, params);
		sqlx.close();

		return t;
	}

	@Override
	public <T> T executeQuery(String sql, ResultSetHandler<T> resultSetHandler, Object... params) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		T t = sqlx.executeQuery(sql, resultSetHandler, params);
		sqlx.close();

		return t;
	}

	@Override
	public int insert(Param param) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		int i = sqlx.insert(param);
		sqlx.close();

		return i;
	}

	@Override
	public int delete(Param param) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		int i = sqlx.delete(param);
		sqlx.close();

		return i;
	}

	@Override
	public int update(Param param) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		int i = sqlx.update(param);
		sqlx.close();

		return i;
	}

	@Override
	public <T> T insert(Param param, Class<T> clazz) throws SQLException {

		Sqlx sqlx = fetchSqlx();
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

		Sqlx sqlx = fetchSqlx();
		T t = sqlx.query(param, resultSetHandler);
		sqlx.close();

		return t;
	}

	@Override
	public <T> T queryEnhanced(Param param, EnhancedResultSetHandler<T> enhancedResultSetHandler) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		T t = sqlx.queryEnhanced(param, enhancedResultSetHandler);
		sqlx.close();

		return t;
	}
	
	@Override
	public <T> T queryForObject(Param param, Class<T> clazz) throws SQLException {
		
		Sqlx sqlx = fetchSqlx();
		T t = sqlx.queryForObject(param, clazz);
		sqlx.close();
		
		return t;
	}

	@Override
	public <T> T queryForRowCount(Param param, Class<T> clazz) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		T t = sqlx.queryForRowCount(param, clazz);
		sqlx.close();

		return t;
	}

	@Override
	public List<RowMetaData> queryForRowMetaData(Param param) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		List<RowMetaData> list = sqlx.queryForRowMetaData(param);
		sqlx.close();

		return list;
	}

	@Override
	public <K, V> Map<K, V> queryForPairKeyedMap(Param param, Class<K> keyClass, String keyColumnName,
			Class<V> valueClass, String valueColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Map<K, V> map = sqlx.queryForPairKeyedMap(param, keyClass, keyColumnName, valueClass, valueColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <V> Map<String, V> queryForPairCombinationKeyedMap(Param param, Class<V> valueClass, String valueColumnName,
			String separator, String... keyColumnNames) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Map<String, V> map = sqlx.queryForPairCombinationKeyedMap(param, valueClass, valueColumnName, separator,
				keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K, V> ListMultiValueMap<K, V> queryForPairListMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName, Class<V> valueClass, String valueColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		ListMultiValueMap<K, V> map = sqlx.queryForPairListMultiValueKeyedMap(param, keyClass, keyColumnName,
				valueClass, valueColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <V> ListMultiValueMap<String, V> queryForPairListMultiValueCombinationKeyedMap(Param param,
			Class<V> valueClass, String valueColumnName, String separator, String... keyColumnNames)
			throws SQLException {

		Sqlx sqlx = fetchSqlx();
		ListMultiValueMap<String, V> map = sqlx.queryForPairListMultiValueCombinationKeyedMap(param, valueClass,
				valueColumnName, separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K, V> SetMultiValueMap<K, V> queryForPairSetMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName, Class<V> valueClass, String valueColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		SetMultiValueMap<K, V> map = sqlx.queryForPairSetMultiValueKeyedMap(param, keyClass, keyColumnName, valueClass,
				valueColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <V> SetMultiValueMap<String, V> queryForPairSetMultiValueCombinationKeyedMap(Param param,
			Class<V> valueClass, String valueColumnName, String separator, String... keyColumnNames)
			throws SQLException {

		Sqlx sqlx = fetchSqlx();
		SetMultiValueMap<String, V> map = sqlx.queryForPairSetMultiValueCombinationKeyedMap(param, valueClass,
				valueColumnName, separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K> List<K> queryForKeyList(Param param, Class<K> keyClass, String keyColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		List<K> list = sqlx.queryForKeyList(param, keyClass, keyColumnName);
		sqlx.close();

		return list;
	}

	@Override
	public <K> Set<K> queryForKeySet(Param param, Class<K> keyClass, String keyColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Set<K> set = sqlx.queryForKeySet(param, keyClass, keyColumnName);
		sqlx.close();

		return set;
	}

	@Override
	public List<String> queryForCombinationKeyList(Param param, String separator, String... keyColumnNames)
			throws SQLException {

		Sqlx sqlx = fetchSqlx();
		List<String> list = sqlx.queryForCombinationKeyList(param, separator, keyColumnNames);
		sqlx.close();

		return list;
	}

	@Override
	public Set<String> queryForCombinationKeySet(Param param, String separator, String... keyColumnNames)
			throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Set<String> set = sqlx.queryForCombinationKeySet(param, separator, keyColumnNames);
		sqlx.close();

		return set;
	}

	@Override
	public Map<String, Object> queryForMap(Param param) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Map<String, Object> map = sqlx.queryForMap(param);
		sqlx.close();

		return map;
	}

	@Override
	public List<Map<String, Object>> queryForMapList(Param param) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		List<Map<String, Object>> list = sqlx.queryForMapList(param);
		sqlx.close();

		return list;
	}

	@Override
	public Page<Map<String, Object>> queryForMapPage(Param param) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Page<Map<String, Object>> page = sqlx.queryForMapPage(param);
		sqlx.close();

		return page;
	}

	@Override
	public Page<Map<String, Object>> queryForMapPageEnhanced(Param param, MapListHandler mapListHandler)
			throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Page<Map<String, Object>> page = sqlx.queryForMapPageEnhanced(param, mapListHandler);
		sqlx.close();

		return page;
	}

	@Override
	public <K> Map<K, Map<String, Object>> queryForMapKeyedMap(Param param, Class<K> keyClass, String keyColumnName)
			throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Map<K, Map<String, Object>> map = sqlx.queryForMapKeyedMap(param, keyClass, keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public Map<String, Map<String, Object>> queryForMapCombinationKeyedMap(Param param, String separator,
			String... keyColumnNames) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Map<String, Map<String, Object>> map = sqlx.queryForMapCombinationKeyedMap(param, separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K> ListMultiValueMap<K, Map<String, Object>> queryForMapListMultiValueKeyedMap(Param param,
			Class<K> keyClass, String keyColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		ListMultiValueMap<K, Map<String, Object>> map = sqlx.queryForMapListMultiValueKeyedMap(param, keyClass,
				keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public ListMultiValueMap<String, Map<String, Object>> queryForMapListMultiValueCombinationKeyedMap(Param param,
			String separator, String... keyColumnNames) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		ListMultiValueMap<String, Map<String, Object>> map = sqlx.queryForMapListMultiValueCombinationKeyedMap(param,
				separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K> SetMultiValueMap<K, Map<String, Object>> queryForMapSetMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		SetMultiValueMap<K, Map<String, Object>> map = sqlx.queryForMapSetMultiValueKeyedMap(param, keyClass,
				keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public SetMultiValueMap<String, Map<String, Object>> queryForMapSetMultiValueCombinationKeyedMap(Param param,
			String separator, String... keyColumnNames) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		SetMultiValueMap<String, Map<String, Object>> map = sqlx.queryForMapSetMultiValueCombinationKeyedMap(param,
				separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <T> T queriForBean(Param param, Class<T> beanClass) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		T t = sqlx.queryForBean(param, beanClass);
		sqlx.close();

		return t;
	}

	@Override
	public <T> List<T> queriForBeanList(Param param, Class<T> beanClass) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		List<T> list = sqlx.queryForBeanList(param, beanClass);
		sqlx.close();

		return list;
	}

	@Override
	public <T> Page<T> queriForBeanPage(Param param, Class<T> beanClass) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Page<T> page = sqlx.queryForBeanPage(param, beanClass);
		sqlx.close();

		return page;
	}

	@Override
	public <T> Page<T> queriForBeanPageEnhanced(Param param, BeanListHandler<T> beanListHandler) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Page<T> page = sqlx.queryForBeanPageEnhanced(param, beanListHandler);
		sqlx.close();

		return page;
	}

	@Override
	public <K, T> Map<K, T> queriForBeanKeyedMap(Param param, Class<T> beanClass, Class<K> keyClass,
			String keyColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Map<K, T> map = sqlx.queryForBeanKeyedMap(param, beanClass, keyClass, keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <T> Map<String, T> queriForBeanCombinationKeyedMap(Param param, Class<T> beanClass, String separator,
			String... keyColumnNames) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Map<String, T> map = sqlx.queryForBeanCombinationKeyedMap(param, beanClass, separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K, T> ListMultiValueMap<K, T> queriForBeanListMultiValueKeyedMap(Param param, Class<T> beanClass,
			Class<K> keyClass, String keyColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		ListMultiValueMap<K, T> map = sqlx.queryForBeanListMultiValueKeyedMap(param, beanClass, keyClass,
				keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <T> ListMultiValueMap<String, T> queriForBeanListMultiValueCombinationKeyedMap(Param param,
			Class<T> beanClass, String separator, String... keyColumnNames) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		ListMultiValueMap<String, T> map = sqlx.queryForBeanListMultiValueCombinationKeyedMap(param, beanClass,
				separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K, T> SetMultiValueMap<K, T> queriForBeanSetMultiValueKeyedMap(Param param, Class<T> beanClass,
			Class<K> keyClass, String keyColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		SetMultiValueMap<K, T> map = sqlx.queryForBeanSetMultiValueKeyedMap(param, beanClass, keyClass, keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public <T> SetMultiValueMap<String, T> queriForBeanSetMultiValueCombinationKeyedMap(Param param, Class<T> beanClass,
			String separator, String... keyColumnNames) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		SetMultiValueMap<String, T> map = sqlx.queryForBeanSetMultiValueCombinationKeyedMap(param, beanClass, separator,
				keyColumnNames);
		sqlx.close();

		return map;
	}

}
