package com.jsan.dao.handler.support.keyed.multivalue;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.AbstractHandler;
import com.jsan.dao.map.LinkedHashSetMultiValueMap;
import com.jsan.dao.map.SetMultiValueMap;

public abstract class AbstractSetMultiValueKeyedHandler<K, V> extends AbstractHandler<SetMultiValueMap<K, V>> {

	protected SetMultiValueMap<K, V> multiValueMap;

	@Override
	public SetMultiValueMap<K, V> handle(ResultSet rs) throws SQLException {

		SetMultiValueMap<K, V> result = createMap();

		while (rs.next()) {
			result.add(createKey(rs), createValue(rs));
		}

		return result;
	}

	protected SetMultiValueMap<K, V> createMap() {

		return multiValueMap == null ? new LinkedHashSetMultiValueMap<K, V>() : multiValueMap;
	}

	protected abstract K createKey(ResultSet rs) throws SQLException;

	protected abstract V createValue(ResultSet rs) throws SQLException;

}
