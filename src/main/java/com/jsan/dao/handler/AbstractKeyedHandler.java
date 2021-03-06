package com.jsan.dao.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractKeyedHandler<K, V> extends AbstractHandler<Map<K, V>> {

	protected Map<K, V> map;

	@Override
	public Map<K, V> handle(ResultSet rs) throws SQLException {

		Map<K, V> result = createMap();

		while (rs.next()) {
			result.put(createKey(rs), createValue(rs));
		}

		return result;
	}

	protected Map<K, V> createMap() {

		return map == null ? new LinkedHashMap<K, V>() : map;
	}

	protected abstract K createKey(ResultSet rs) throws SQLException;

	protected abstract V createValue(ResultSet rs) throws SQLException;

}
