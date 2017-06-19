package com.jsan.dao.handler.support.keyed.multivalue.list;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractListMultiValueSingleKeyedHandler;
import com.jsan.dao.map.ListMultiValueMap;

public class BeanListMultiValueKeyedHandler<K, V> extends AbstractListMultiValueSingleKeyedHandler<K, V> {

	protected Class<V> beanClass;

	public BeanListMultiValueKeyedHandler(ListMultiValueMap<K, V> multiValueMap, Class<K> keyClass,
			Class<V> beanClass) {

		this.multiValueMap = multiValueMap;
		this.keyClass = keyClass;
		this.beanClass = beanClass;
	}

	public BeanListMultiValueKeyedHandler(Class<K> keyClass, Class<V> beanClass) {

		this(null, keyClass, beanClass);
	}

	public BeanListMultiValueKeyedHandler(ListMultiValueMap<K, V> multiValueMap, Class<K> keyClass, Class<V> beanClass,
			int keyColumnIndex) {

		this(multiValueMap, keyClass, beanClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public BeanListMultiValueKeyedHandler(Class<K> keyClass, Class<V> beanClass, int keyColumnIndex) {

		this(null, keyClass, beanClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public BeanListMultiValueKeyedHandler(ListMultiValueMap<K, V> multiValueMap, Class<K> keyClass, Class<V> beanClass,
			String keyColumnName) {

		this(multiValueMap, keyClass, beanClass);
		this.keyColumnName = keyColumnName;
	}

	public BeanListMultiValueKeyedHandler(Class<K> keyClass, Class<V> beanClass, String keyColumnName) {

		this(null, keyClass, beanClass);
		this.keyColumnName = keyColumnName;
	}

	@Override
	protected V createValue(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
