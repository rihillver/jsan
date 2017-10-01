package com.jsan.dao.handler.support.keyed.multivalue.set;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractSetMultiValueSingleKeyedHandler;
import com.jsan.dao.map.SetMultiValueMap;

public class MapSetMultiValueKeyedHandler<K> extends AbstractSetMultiValueSingleKeyedHandler<K, Map<String, Object>> {

	public MapSetMultiValueKeyedHandler(SetMultiValueMap<K, Map<String, Object>> multiValueMap, Class<K> keyClass) {

		this.multiValueMap = multiValueMap;
		this.keyClass = keyClass;
	}

	public MapSetMultiValueKeyedHandler(Class<K> keyClass) {

		this(null, keyClass);
	}

	public MapSetMultiValueKeyedHandler(SetMultiValueMap<K, Map<String, Object>> multiValueMap, Class<K> keyClass,
			int keyColumnIndex) {

		this(multiValueMap, keyClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public MapSetMultiValueKeyedHandler(Class<K> keyClass, int keyColumnIndex) {

		this(null, keyClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public MapSetMultiValueKeyedHandler(SetMultiValueMap<K, Map<String, Object>> multiValueMap, Class<K> keyClass,
			String keyColumnName) {

		this(multiValueMap, keyClass);
		this.keyColumnName = keyColumnName;
	}

	public MapSetMultiValueKeyedHandler(Class<K> keyClass, String keyColumnName) {

		this(null, keyClass);
		this.keyColumnName = keyColumnName;
	}

	@Override
	protected Map<String, Object> createValue(ResultSet rs) throws SQLException {

		return getMap(rs);
	}

}
