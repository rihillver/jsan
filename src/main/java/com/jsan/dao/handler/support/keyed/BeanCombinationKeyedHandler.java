package com.jsan.dao.handler.support.keyed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.jsan.dao.handler.AbstractCombinationKeyedHandler;

public class BeanCombinationKeyedHandler<V> extends AbstractCombinationKeyedHandler<V> {

	protected Class<V> beanClass;

	public BeanCombinationKeyedHandler(Class<V> beanClass, String separator, int... keyColumnIndexes) {

		this(null, beanClass, separator, keyColumnIndexes);
	}

	public BeanCombinationKeyedHandler(Map<String, V> map, Class<V> beanClass, String separator,
			int... keyColumnIndexes) {

		this.map = map;
		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public BeanCombinationKeyedHandler(Class<V> beanClass, String separator, String... keyColumnNames) {

		this(null, beanClass, separator, keyColumnNames);
	}

	public BeanCombinationKeyedHandler(Map<String, V> map, Class<V> beanClass, String separator,
			String... keyColumnNames) {

		this.map = map;
		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnNames = keyColumnNames;
	}

	@Override
	protected V createValue(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
