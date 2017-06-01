package com.jsan.dao.handler.support.keyed;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.AbstractSingleKeyedHandler;

public class BeanKeyedHandler<K, T> extends AbstractSingleKeyedHandler<K, T> {

	protected Class<T> beanClass;

	public BeanKeyedHandler(Class<K> keyClass, Class<T> beanClass) {

		this.keyClass = keyClass;
		this.beanClass = beanClass;
	}

	public BeanKeyedHandler(Class<K> keyClass, Class<T> beanClass, int keyColumnIndex) {

		this(keyClass, beanClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public BeanKeyedHandler(Class<K> keyClass, Class<T> beanClass, String keyColumnName) {

		this(keyClass, beanClass);
		this.keyColumnName = keyColumnName;
	}

	@Override
	protected T createValue(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
