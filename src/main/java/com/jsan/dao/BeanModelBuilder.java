package com.jsan.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsan.convert.BeanConvertUtils;
import com.jsan.convert.BeanProxyUtils;
import com.jsan.convert.ConvertFuncUtils;
import com.jsan.dao.handler.support.BeanListHandler;
import com.jsan.dao.handler.support.MapListHandler;
import com.jsan.dao.map.ListMultiValueMap;
import com.jsan.dao.map.SetMultiValueMap;

/**
 * 基于 Bean 的 Model 构建器。
 * <ol>
 * <li>参与执行增删改操作的 Bean 对象的相关转换基于其自身字段（仅含自身的所有字段，不含父类的任何字段）对应的 Getter 和 Setter
 * 操作。</li>
 * <li>执行查询操作返回的 Bean 对象结果集的相关转换则通过其 Getter 和 Setter
 * 方法取字段名（含父类的公共方法，不含自身的私有方法，对应的字段不一定真实存在）。</li>
 * <li>如果需要对 Bean 对象增加辅助的 Getter 和 Setter 方法时，当涉对象自身字段的相关操作时，务必通过其自身字段对应的 Getter
 * 和 Setter 方法进行取值和设置操作。</li>
 * </ol>
 * 
 */
public class BeanModelBuilder<B> extends SqlxModelBuilder implements BeanModel<B> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Class<B> beanClass = initModelBeanClass();

	@SuppressWarnings("unchecked")
	private Class<B> initModelBeanClass() {

		try {
			Type type = getClass().getGenericSuperclass();
			return (Class<B>) ((ParameterizedType) type).getActualTypeArguments()[0];
		} catch (Exception e) {
			logger.warn("Generic undefined");
			return (Class<B>) Object.class; // 应付B为?的时候可实例化，仅为了便于测试
		}
	}

	/**
	 * 基于 Bean 的字段对应的 Getter 进行转换。
	 * 
	 * @param bean
	 * @return
	 */
	protected Map<String, Object> getBeanMap(B bean) {

		if (BeanProxyUtils.isDaoBean(bean)) {
			Class<B> beanClass = BeanProxyUtils.getDaoBeanOriginalClass(bean);
			return BeanConvertUtils.convertBeanToMap(beanClass, bean, true, fieldInSnakeCase);
		} else {
			return BeanConvertUtils.convertBeanToMap(bean, true, fieldInSnakeCase);
		}
	}

	@Override
	public Class<B> fetchBeanClass() {

		return beanClass;
	}

	@Override
	public B findById(Object... primaryValue) throws SQLException {

		Param param = createParam();
		param.setPrimaryValue(primaryValue);

		Sqlx sqlx = fetchSqlx();
		B b = sqlx.queryForBean(param, fetchBeanClass());
		sqlx.close();

		return b;
	}

	@Override
	public B findByIds(String ids, Object... primaryValue) throws SQLException {

		String[] fields = ids.split(",");

		Param param = createParam();
		for (int i = 0; i < fields.length; i++) {
			param.set(fields[i], primaryValue[i]);
		}

		Sqlx sqlx = fetchSqlx();
		B b = sqlx.queryForBean(param, fetchBeanClass());
		sqlx.close();

		return b;
	}

	@Override
	public boolean delete(B bean) throws SQLException {

		Param param = createParam();
		String[] primaryKey = param.getPrimaryKey();
		Object[] primaryValue = new Object[primaryKey.length];
		for (int i = 0; i < primaryKey.length; i++) {
			primaryValue[i] = getBeanMap(bean).get(primaryKey[i]);
		}
		param.setPrimaryValue(primaryValue);

		return delete(param) > 0;
	}

	protected String[] getDaoBeanExcludeFields(B bean, String[] excludeFields) {

		Set<String> set = BeanProxyUtils.getDaoBeanExcludeFieldSet(bean);
		if (set == null) {
			return excludeFields;
		} else {
			Set<String> tmpSet = new LinkedHashSet<String>();
			for (String str : set) {
				tmpSet.add(fieldInSnakeCase ? ConvertFuncUtils.parseCamelCaseToSnakeCase(str) : str);
			}
			for (String field : excludeFields) {
				tmpSet.add(field);
			}
			return tmpSet.toArray(new String[tmpSet.size()]);
		}
	}

	@Override
	public boolean update(B bean, String... excludeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(getBeanMap(bean));
		param.setMarkFields(getDaoBeanExcludeFields(bean, excludeFields));

		return update(param) > 0;
	}

	@Override
	public boolean updateInc(B bean, String... includeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(getBeanMap(bean));
		param.setMarkFields(includeFields);
		param.setInclude(true);

		return update(param) > 0;
	}

	@Override
	public boolean insert(B bean, String... excludeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(getBeanMap(bean));
		param.setMarkFields(getDaoBeanExcludeFields(bean, excludeFields));

		return insert(param) > 0;
	}

	@Override
	public boolean insertInc(B bean, String... includeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(getBeanMap(bean));
		param.setMarkFields(includeFields);
		param.setInclude(true);

		return insert(param) > 0;
	}

	@Override
	public int gainInsert(B bean, String... excludeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(getBeanMap(bean));
		param.setMarkFields(getDaoBeanExcludeFields(bean, excludeFields));

		return insert(param, int.class);
	}

	@Override
	public int gainInsertInc(B bean, String... includeFields) throws SQLException {

		Param param = createParam();
		param.setParamMap(getBeanMap(bean));
		param.setMarkFields(includeFields);
		param.setInclude(true);

		return insert(param, int.class);
	}

	@Override
	public B queryFirst(String sql, Object... params) throws SQLException {

		Param param = createParam();
		param.setInitializedSql(sql);
		param.setInitializedParams(params);
		return queryForBean(param);
	}

	@Override
	public List<B> query(String sql, Object... params) throws SQLException {

		Param param = createParam();
		param.setInitializedSql(sql);
		param.setInitializedParams(params);
		return queryForBeanList(param);
	}

//	@Override
//	public Page<B> paginate(int pageSize, int pageNumber, String sql, Object... params) throws SQLException {
//
//		Param param = createParam();
//		param.setInitializedSql(sql);
//		param.setInitializedParams(params);
//		param.setPageSize(pageSize);
//		param.setPageNumber(pageNumber);
//
//		return queryForBeanPage(param);
//	}

	@Override
	public B queryForBean(Param param) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		B b = sqlx.queryForBean(param, fetchBeanClass());
		sqlx.close();

		return b;
	}

	@Override
	public List<B> queryForBeanList(Param param) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		List<B> list = sqlx.queryForBeanList(param, fetchBeanClass());
		sqlx.close();

		return list;
	}

	@Override
	public Page<B> queryForBeanPage(Param param) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Page<B> page = sqlx.queryForBeanPage(param, fetchBeanClass());
		sqlx.close();

		return page;
	}

	@Override
	public Page<B> queryForBeanPageEnhanced(Param param, BeanListHandler<B> beanListHandler) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Page<B> page = sqlx.queryForBeanPageEnhanced(param, beanListHandler);
		sqlx.close();

		return page;
	}

	@Override
	public <K> Map<K, B> queryForBeanKeyedMap(Param param, Class<K> keyClass, String keyColumnName)
			throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Map<K, B> map = sqlx.queryForBeanKeyedMap(param, fetchBeanClass(), keyClass, keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public Map<String, B> queryForBeanCombinationKeyedMap(Param param, String separator, String... keyColumnNames)
			throws SQLException {

		Sqlx sqlx = fetchSqlx();
		Map<String, B> map = sqlx.queryForBeanCombinationKeyedMap(param, fetchBeanClass(), separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K> ListMultiValueMap<K, B> queryForBeanListMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		ListMultiValueMap<K, B> map = sqlx.queryForBeanListMultiValueKeyedMap(param, fetchBeanClass(), keyClass,
				keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public ListMultiValueMap<String, B> queryForBeanListMultiValueCombinationKeyedMap(Param param, String separator,
			String... keyColumnNames) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		ListMultiValueMap<String, B> map = sqlx.queryForBeanListMultiValueCombinationKeyedMap(param, fetchBeanClass(),
				separator, keyColumnNames);
		sqlx.close();

		return map;
	}

	@Override
	public <K> SetMultiValueMap<K, B> queryForBeanSetMultiValueKeyedMap(Param param, Class<K> keyClass,
			String keyColumnName) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		SetMultiValueMap<K, B> map = sqlx.queryForBeanSetMultiValueKeyedMap(param, fetchBeanClass(), keyClass,
				keyColumnName);
		sqlx.close();

		return map;
	}

	@Override
	public SetMultiValueMap<String, B> queryForBeanSetMultiValueCombinationKeyedMap(Param param, String separator,
			String... keyColumnNames) throws SQLException {

		Sqlx sqlx = fetchSqlx();
		SetMultiValueMap<String, B> map = sqlx.queryForBeanSetMultiValueCombinationKeyedMap(param, fetchBeanClass(),
				separator, keyColumnNames);
		sqlx.close();

		return map;
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

}