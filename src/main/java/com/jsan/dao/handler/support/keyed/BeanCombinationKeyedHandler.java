package com.jsan.dao.handler.support.keyed;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.AbstractCombinationKeyedHandler;

public class BeanCombinationKeyedHandler<T> extends AbstractCombinationKeyedHandler<T> {

	protected Class<T> beanClass;

	public BeanCombinationKeyedHandler(Class<T> beanClass, String separator, int... keyColumnIndexes) {

		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public BeanCombinationKeyedHandler(Class<T> beanClass, String separator, String... keyColumnNames) {

		this.separator = separator;
		this.beanClass = beanClass;
		this.keyColumnNames = keyColumnNames;
	}

	@Override
	protected T createValue(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
