package com.jsan.dao.handler.support.key;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.jsan.dao.handler.AbstractHandler;

public class KeySetHandler<K> extends AbstractHandler<Set<K>> {

	protected Class<K> keyClass;
	protected int keyColumnIndex = 1; // 键默认为第1列
	protected String keyColumnName;

	@Override
	public Set<K> handle(ResultSet rs) throws SQLException {

		return null;
	}

	protected K createKey(ResultSet rs) throws SQLException {

		K k = null;

		if (keyColumnName == null) {
			k = getObject(rs, keyColumnIndex, keyClass, convertService);
		} else {
			k = getObject(rs, keyColumnName, keyClass, convertService);
		}

		return k;
	}

}
