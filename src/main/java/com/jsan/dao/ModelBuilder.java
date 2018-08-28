package com.jsan.dao;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jsan.convert.ConvertFuncUtils;
import com.jsan.convert.ConvertService;

/**
 * 该类及其子类均不推荐在单例下使用，非线程安全。
 *
 */

public class ModelBuilder extends MapModelBuilder implements Model {

	protected Map<String, Object> $paramMap;
	protected Map<String, Object> $orderByMap;

	protected Object[] $primaryValue;
	protected int $rowCount;

	@Override
	public Param createParam(String sql) {

		Param param = super.createParam(sql);

		param.setPrimaryValue($primaryValue);
		param.setOrderByMap($orderByMap);
		param.setRowCount($rowCount);

		return param;
	}

	// ==================================================

	@Override
	public Class<? extends Sqlx> fetchSqlxClass() {
		return $sqlxClass;
	}

	@Override
	public void giveSqlxClass(Class<? extends Sqlx> sqlxClass) {
		this.$sqlxClass = sqlxClass;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Class<? extends Page> fetchPageClass() {
		return $pageClass;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void givePageClass(Class<? extends Page> pageClass) {
		this.$pageClass = pageClass;
	}

	@Override
	public String fetchDataSourceName() {
		return $dataSourceName;
	}

	@Override
	public void giveDataSourceName(String dataSourceName) {
		this.$dataSourceName = dataSourceName;
	}

	@Override
	public ConvertService fetchConvertService() {
		return $convertService;
	}

	@Override
	public void giveConvertService(ConvertService convertService) {
		this.$convertService = convertService;
	}

	@Override
	public FieldNameHandler fetchFieldNameHandler() {
		return $fieldNameHandler;
	}

	@Override
	public void giveFieldNameHandler(FieldNameHandler fieldNameHandler) {
		this.$fieldNameHandler = fieldNameHandler;
	}

	@Override
	public FieldValueHandler fetchFieldValueHandler() {
		return $fieldValueHandler;
	}

	@Override
	public void giveFieldValueHandler(FieldValueHandler fieldValueHandler) {
		this.$fieldValueHandler = fieldValueHandler;
	}

	@Override
	public String[] fetchPrimaryKey() {
		return $primaryKey;
	}

	@Override
	public void givePrimaryKey(String[] primaryKey) {
		this.$primaryKey = primaryKey;
	}

	@Override
	public String[] fetchAutoIncrementKey() {
		return $autoIncrementKey;
	}

	@Override
	public void giveAutoIncrementKey(String[] autoIncrementKey) {
		this.$autoIncrementKey = autoIncrementKey;
	}

	@Override
	public String[] fetchAutoIncrementValue() {
		return $autoIncrementValue;
	}

	@Override
	public void giveAutoIncrementValue(String[] autoIncrementValue) {
		this.$autoIncrementValue = autoIncrementValue;
	}

	/**
	 * 这里需要覆盖父类的方法已达到更精确的表达。
	 * 
	 */
	@Override
	public String fetchTableName() {

		if ($tableName != null) {
			return $tableName;
		} else {
			String name = ConvertFuncUtils.parseFirstCharToLowerCase(getClass().getSimpleName()); // 转换为小驼峰命名规范
			if ($tableInSnakeCase) {
				name = ConvertFuncUtils.parseCamelCaseToSnakeCase(name); // 转换为下划线命名规范
			}
			return name;
		}
	}

	@Override
	public void giveTableName(String tableName) {
		this.$tableName = tableName;
	}

	@Override
	public boolean areFieldInSnakeCase() {
		return $fieldInSnakeCase;
	}

	@Override
	public void giveFieldInSnakeCase(boolean fieldInSnakeCase) {
		this.$fieldInSnakeCase = fieldInSnakeCase;
	}

	@Override
	public boolean areTableInSnakeCase() {
		return $tableInSnakeCase;
	}

	@Override
	public void giveTableInSnakeCase(boolean tableInSnakeCase) {
		this.$tableInSnakeCase = tableInSnakeCase;
	}

	@Override
	public boolean areFieldToLowerCase() {
		return $fieldToLowerCase;
	}

	@Override
	public void giveFieldToLowerCase(boolean fieldToLowerCase) {
		this.$fieldToLowerCase = fieldToLowerCase;
	}

	@Override
	public boolean areFieldCaseInsensitive() {
		return $fieldCaseInsensitive;
	}

	@Override
	public void giveFieldCaseInsensitive(boolean fieldCaseInsensitive) {
		this.$fieldCaseInsensitive = fieldCaseInsensitive;
	}

	// ==================================================

	@Override
	public Map<String, Object> fetchParamMap() {
		return $paramMap;
	}

	@Override
	public void giveParamMap(Map<String, Object> paramMap) {
		this.$paramMap = paramMap;
	}

	@Override
	public Map<String, Object> fetchOrderByMap() {
		return $orderByMap;
	}

	@Override
	public void giveOrderByMap(Map<String, Object> orderByMap) {
		this.$orderByMap = orderByMap;
	}

	@Override
	public void giveOrderBy(String field, Object order) {

		if ($orderByMap == null) {
			$orderByMap = new LinkedHashMap<String, Object>();
		}
		if ($orderByMap.containsKey(field)) {
			$orderByMap.remove(field);
		}
		$orderByMap.put(field, order);
	}

	@Override
	public Object[] fetchPrimaryValue() {
		return $primaryValue;
	}

	@Override
	public void givePrimaryValue(Object[] primaryValue) {
		this.$primaryValue = primaryValue;
	}

	@Override
	public int fetchRowCount() {
		return $rowCount;
	}

	@Override
	public void giveRowCount(int rowCount) {
		this.$rowCount = rowCount;
	}

	// ==================================================

	@Override
	public void set(String key, Object value) {

		if ($paramMap == null) {
			$paramMap = new LinkedHashMap<String, Object>();
		}
		$paramMap.put(key, value);
	}

	@Override
	public Map<String, Object> find() throws SQLException {

		Param param = createParam();
		return queryForMap(param);
	}

	@Override
	public boolean delete() throws SQLException {

		Param param = createParam();
		return delete(param) > 0;
	}

	@Override
	public boolean update() throws SQLException {

		Param param = createParam();
		return update(param) > 0;
	}

	@Override
	public boolean insert() throws SQLException {

		Param param = createParam();
		return insert(param) > 0;
	}

	@Override
	public int gainInsert() throws SQLException {

		Param param = createParam();
		return insert(param, int.class);
	}

}
