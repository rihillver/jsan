package com.jsan.dao.handler.support.keyed.multivalue.set;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractSetMultiValueSingleKeyedHandler;
import com.jsan.dao.map.SetMultiValueMap;

public class BeanSetMultiValueKeyedHandler<K, V> extends AbstractSetMultiValueSingleKeyedHandler<K, V> {

	protected Class<V> beanClass;

	public BeanSetMultiValueKeyedHandler(SetMultiValueMap<K, V> multiValueMap, Class<K> keyClass, Class<V> beanClass) {

		this.multiValueMap = multiValueMap;
		this.keyClass = keyClass;
		this.beanClass = beanClass;
	}

	public BeanSetMultiValueKeyedHandler(Class<K> keyClass, Class<V> beanClass) {

		this(null, keyClass, beanClass);
	}

	public BeanSetMultiValueKeyedHandler(SetMultiValueMap<K, V> multiValueMap, Class<K> keyClass, Class<V> beanClass,
			int keyColumnIndex) {

		this(multiValueMap, keyClass, beanClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public BeanSetMultiValueKeyedHandler(Class<K> keyClass, Class<V> beanClass, int keyColumnIndex) {

		this(null, keyClass, beanClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public BeanSetMultiValueKeyedHandler(SetMultiValueMap<K, V> multiValueMap, Class<K> keyClass, Class<V> beanClass,
			String keyColumnName) {

		this(multiValueMap, keyClass, beanClass);
		this.keyColumnName = keyColumnName;
	}

	public BeanSetMultiValueKeyedHandler(Class<K> keyClass, Class<V> beanClass, String keyColumnName) {

		this(null, keyClass, beanClass);
		this.keyColumnName = keyColumnName;
	}

	@Override
	protected V createValue(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
