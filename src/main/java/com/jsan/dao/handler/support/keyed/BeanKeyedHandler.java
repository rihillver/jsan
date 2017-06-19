package com.jsan.dao.handler.support.keyed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.jsan.dao.handler.AbstractSingleKeyedHandler;

public class BeanKeyedHandler<K, V> extends AbstractSingleKeyedHandler<K, V> {

	protected Class<V> beanClass;

	public BeanKeyedHandler(Class<K> keyClass, Class<V> beanClass) {

		this(null, keyClass, beanClass);
	}

	public BeanKeyedHandler(Map<K, V> map, Class<K> keyClass, Class<V> beanClass) {

		this.map = map;
		this.keyClass = keyClass;
		this.beanClass = beanClass;
	}

	public BeanKeyedHandler(Class<K> keyClass, Class<V> beanClass, int keyColumnIndex) {

		this(keyClass, beanClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public BeanKeyedHandler(Map<K, V> map, Class<K> keyClass, Class<V> beanClass, int keyColumnIndex) {

		this(map, keyClass, beanClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public BeanKeyedHandler(Class<K> keyClass, Class<V> beanClass, String keyColumnName) {

		this(keyClass, beanClass);
		this.keyColumnName = keyColumnName;
	}

	public BeanKeyedHandler(Map<K, V> map, Class<K> keyClass, Class<V> beanClass, String keyColumnName) {

		this(map, keyClass, beanClass);
		this.keyColumnName = keyColumnName;
	}

	@Override
	protected V createValue(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
