package com.jsan.dao.handler.support.keyed.multivalue.list;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractListMultiValueCombinationKeyedHandler;
import com.jsan.dao.map.ListMultiValueMap;

public class BeanListMultiValueCombinationKeyedHandler<V> extends AbstractListMultiValueCombinationKeyedHandler<V> {

	protected Class<V> beanClass;

	public BeanListMultiValueCombinationKeyedHandler(ListMultiValueMap<String, V> multiValueMap, Class<V> beanClass,
			String separator, int... keyColumnIndexes) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public BeanListMultiValueCombinationKeyedHandler(Class<V> beanClass, String separator, int... keyColumnIndexes) {

		this(null, beanClass, separator, keyColumnIndexes);
	}

	public BeanListMultiValueCombinationKeyedHandler(ListMultiValueMap<String, V> multiValueMap, Class<V> beanClass,
			String separator, String... keyColumnNames) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnNames = keyColumnNames;
	}

	public BeanListMultiValueCombinationKeyedHandler(Class<V> beanClass, String separator, String... keyColumnNames) {

		this(null, beanClass, separator, keyColumnNames);
	}

	@Override
	protected V createValue(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
