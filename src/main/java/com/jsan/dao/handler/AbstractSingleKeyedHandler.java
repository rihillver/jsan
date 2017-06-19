package com.jsan.dao.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractSingleKeyedHandler<K, V> extends AbstractKeyedHandler<K, V> {

	protected Class<K> keyClass;
	protected int keyColumnIndex = 1; // 键默认为第1列
	protected String keyColumnName;

	@Override
	protected K createKey(ResultSet rs) throws SQLException {

		return getKey(rs, keyClass, keyColumnIndex, keyColumnName);
	}

}
