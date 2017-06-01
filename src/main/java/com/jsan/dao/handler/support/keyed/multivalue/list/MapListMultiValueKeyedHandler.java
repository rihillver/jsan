package com.jsan.dao.handler.support.keyed.multivalue.list;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractListMultiValueSingleKeyedHandler;
import com.jsan.dao.map.ListMultiValueMap;

public class MapListMultiValueKeyedHandler<K> extends AbstractListMultiValueSingleKeyedHandler<K, Map<String, Object>> {

	public MapListMultiValueKeyedHandler(ListMultiValueMap<K, Map<String, Object>> multiValueMap, Class<K> keyClass) {

		this.multiValueMap = multiValueMap;
		this.keyClass = keyClass;
	}

	public MapListMultiValueKeyedHandler(Class<K> keyClass) {

		this(null, keyClass);
	}

	public MapListMultiValueKeyedHandler(ListMultiValueMap<K, Map<String, Object>> multiValueMap, Class<K> keyClass,
			int keyColumnIndex) {

		this(multiValueMap, keyClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public MapListMultiValueKeyedHandler(Class<K> keyClass, int keyColumnIndex) {

		this(null, keyClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public MapListMultiValueKeyedHandler(ListMultiValueMap<K, Map<String, Object>> multiValueMap, Class<K> keyClass,
			String keyColumnName) {

		this(multiValueMap, keyClass);
		this.keyColumnName = keyColumnName;
	}

	public MapListMultiValueKeyedHandler(Class<K> keyClass, String keyColumnName) {

		this(null, keyClass);
		this.keyColumnName = keyColumnName;
	}

	@Override
	protected Map<String, Object> createValue(ResultSet rs) throws SQLException {

		return getMap(rs);
	}

}
