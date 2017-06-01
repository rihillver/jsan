package com.jsan.dao.handler.support.keyed;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.AbstractSingleKeyedHandler;

/**
 * 键值对的 Map 的处理器，即 key 对应一个字段， value 也对应一个字段。
 *
 * @param <K>
 * @param <V>
 */

public class PairKeyedHandler<K, V> extends AbstractSingleKeyedHandler<K, V> {

	protected Class<V> valueClass;
	protected String valueColumnName;
	protected int valueColumnIndex = 2;

	public PairKeyedHandler(Class<K> keyClass, Class<V> valueClass) {

		this.keyClass = keyClass;
		this.valueClass = valueClass;
	}

	public PairKeyedHandler(Class<K> keyClass, String keyColumnName, Class<V> valueClass, String valueColumnName) {

		this.keyClass = keyClass;
		this.valueClass = valueClass;
		this.keyColumnName = keyColumnName;
		this.valueColumnName = valueColumnName;
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
