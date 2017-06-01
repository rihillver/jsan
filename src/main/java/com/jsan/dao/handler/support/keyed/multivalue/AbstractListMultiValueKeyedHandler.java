package com.jsan.dao.handler.support.keyed.multivalue;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.AbstractHandler;
import com.jsan.dao.map.LinkedListMultiValueMap;
import com.jsan.dao.map.ListMultiValueMap;

public abstract class AbstractListMultiValueKeyedHandler<K, V> extends AbstractHandler<ListMultiValueMap<K, V>> {

	protected ListMultiValueMap<K, V> multiValueMap;

	@Override
	public ListMultiValueMap<K, V> handle(ResultSet rs) throws SQLException {

		ListMultiValueMap<K, V> result = createMap();

		while (rs.next()) {
			result.add(createKey(rs), createValue(rs));
		}

		return result;
	}

	protected ListMultiValueMap<K, V> createMap() {

		return multiValueMap == null ? new LinkedListMultiValueMap<K, V>() : multiValueMap;
	}

	protected abstract K createKey(ResultSet rs) throws SQLException;

	protected abstract V createValue(ResultSet rs) throws SQLException;

}
