package com.jsan.dao.handler.support.keyed.multivalue.list;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractListMultiValueSingleKeyedHandler;
import com.jsan.dao.map.ListMultiValueMap;

/**
 * 键值对的 Map 的处理器，即 key 对应一个字段， value 也对应一个字段。
 *
 * @param <K>
 * @param <V>
 */

public class PairListMultiValueKeyedHandler<K, V> extends AbstractListMultiValueSingleKeyedHandler<K, V> {

	protected Class<V> valueClass;
	protected String valueColumnName;
	protected int valueColumnIndex = 2;

	public PairListMultiValueKeyedHandler(ListMultiValueMap<K, V> multiValueMap, Class<K> keyClass,
			Class<V> valueClass) {

		this.multiValueMap = multiValueMap;
		this.keyClass = keyClass;
		this.valueClass = valueClass;
	}

	public PairListMultiValueKeyedHandler(Class<K> keyClass, Class<V> valueClass) {

		this(null, keyClass, valueClass);
	}

	public PairListMultiValueKeyedHandler(ListMultiValueMap<K, V> multiValueMap, Class<K> keyClass,
			String keyColumnName, Class<V> valueClass, String valueColumnName) {

		this.multiValueMap = multiValueMap;
		this.keyClass = keyClass;
		this.keyColumnName = keyColumnName;
		this.valueClass = valueClass;
		this.valueColumnName = valueColumnName;
	}

	public PairListMultiValueKeyedHandler(Class<K> keyClass, String keyColumnName, Class<V> valueClass,
			String valueColumnName) {

		this(null, keyClass, keyColumnName, valueClass, valueColumnName);
	}

	@Override
	protected V createValue(ResultSet rs) throws SQLException {

		if (valueColumnName == null) {
			return getObject(rs, valueColumnIndex, valueClass, convertService);
		} else {
			return getObject(rs, valueColumnName, valueClass, convertService);
		}
	}

}
