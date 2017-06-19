package com.jsan.dao.handler.support.keyed.multivalue.set;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractSetMultiValueCombinationKeyedHandler;
import com.jsan.dao.map.SetMultiValueMap;

public class BeanSetMultiValueCombinationKeyedHandler<V> extends AbstractSetMultiValueCombinationKeyedHandler<V> {

	protected Class<V> beanClass;

	public BeanSetMultiValueCombinationKeyedHandler(SetMultiValueMap<String, V> multiValueMap, Class<V> beanClass,
			String separator, int... keyColumnIndexes) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public BeanSetMultiValueCombinationKeyedHandler(Class<V> beanClass, String separator, int... keyColumnIndexes) {

		this(null, beanClass, separator, keyColumnIndexes);
	}

	public BeanSetMultiValueCombinationKeyedHandler(SetMultiValueMap<String, V> multiValueMap, Class<V> beanClass,
			String separator, String... keyColumnNames) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnNames = keyColumnNames;
	}

	public BeanSetMultiValueCombinationKeyedHandler(Class<V> beanClass, String separator, String... keyColumnNames) {

		this(null, beanClass, separator, keyColumnNames);
	}

	@Override
	protected V createValue(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
