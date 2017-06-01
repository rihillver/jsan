package com.jsan.dao.handler.support.keyed.multivalue;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractListMultiValueSingleKeyedHandler<K, V> extends AbstractListMultiValueKeyedHandler<K, V> {

	protected Class<K> keyClass;
	protected int keyColumnIndex = 1;
	protected String keyColumnName;

	@Override
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
