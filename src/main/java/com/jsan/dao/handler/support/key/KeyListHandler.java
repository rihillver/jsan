package com.jsan.dao.handler.support.key;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.jsan.dao.handler.AbstractHandler;

public class KeyListHandler<K> extends AbstractHandler<List<K>> {

	protected Class<K> keyClass;
	protected int keyColumnIndex = 1; // 键默认为第1列
	protected String keyColumnName;

	@Override
	public List<K> handle(ResultSet rs) throws SQLException {

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
