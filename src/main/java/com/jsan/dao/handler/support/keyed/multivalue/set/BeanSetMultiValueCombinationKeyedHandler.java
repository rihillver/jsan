package com.jsan.dao.handler.support.keyed.multivalue.set;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractSetMultiValueCombinationKeyedHandler;
import com.jsan.dao.map.SetMultiValueMap;

public class BeanSetMultiValueCombinationKeyedHandler<T> extends AbstractSetMultiValueCombinationKeyedHandler<T> {

	protected Class<T> beanClass;

	public BeanSetMultiValueCombinationKeyedHandler(SetMultiValueMap<String, T> multiValueMap, Class<T> beanClass,
			String separator, int... keyColumnIndexes) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public BeanSetMultiValueCombinationKeyedHandler(Class<T> beanClass, String separator, int... keyColumnIndexes) {

		this(null, beanClass, separator, keyColumnIndexes);
	}

	public BeanSetMultiValueCombinationKeyedHandler(SetMultiValueMap<String, T> multiValueMap, Class<T> beanClass,
			String separator, String... keyColumnNames) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnNames = keyColumnNames;
	}

	public BeanSetMultiValueCombinationKeyedHandler(Class<T> beanClass, String separator, String... keyColumnNames) {

		this(null, beanClass, separator, keyColumnNames);
	}

	@Override
	protected T createValue(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
