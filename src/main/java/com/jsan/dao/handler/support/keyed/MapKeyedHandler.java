package com.jsan.dao.handler.support.keyed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.jsan.dao.handler.AbstractSingleKeyedHandler;

public class MapKeyedHandler<K> extends AbstractSingleKeyedHandler<K, Map<String, Object>> {

	public MapKeyedHandler(Class<K> keyClass) {

		this.keyClass = keyClass;
	}

	public MapKeyedHandler(Class<K> keyClass, int keyColumnIndex) {

		this(keyClass);
		this.keyColumnIndex = keyColumnIndex;
	}

	public MapKeyedHandler(Class<K> keyClass, String keyColumnName) {

		this(keyClass);
		this.keyColumnName = keyColumnName;
	}

	@Override
	protected Map<String, Object> createValue(ResultSet rs) throws SQLException {

		return getMap(rs);
	}

}
