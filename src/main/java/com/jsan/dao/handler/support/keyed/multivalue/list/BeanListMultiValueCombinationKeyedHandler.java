package com.jsan.dao.handler.support.keyed.multivalue.list;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractListMultiValueCombinationKeyedHandler;
import com.jsan.dao.map.ListMultiValueMap;

public class BeanListMultiValueCombinationKeyedHandler<T> extends AbstractListMultiValueCombinationKeyedHandler<T> {

	protected Class<T> beanClass;

	public BeanListMultiValueCombinationKeyedHandler(ListMultiValueMap<String, T> multiValueMap, Class<T> beanClass,
			String separator, int... keyColumnIndexes) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public BeanListMultiValueCombinationKeyedHandler(Class<T> beanClass, String separator, int... keyColumnIndexes) {

		this(null, beanClass, separator, keyColumnIndexes);
	}

	public BeanListMultiValueCombinationKeyedHandler(ListMultiValueMap<String, T> multiValueMap, Class<T> beanClass,
			String separator, String... keyColumnNames) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnNames = keyColumnNames;
	}

	public BeanListMultiValueCombinationKeyedHandler(Class<T> beanClass, String separator, String... keyColumnNames) {

		this(null, beanClass, separator, keyColumnNames);
	}

	@Override
	protected T createValue(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
