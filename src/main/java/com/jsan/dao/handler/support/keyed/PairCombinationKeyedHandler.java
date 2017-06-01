package com.jsan.dao.handler.support.keyed;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.AbstractCombinationKeyedHandler;

public class PairCombinationKeyedHandler<V> extends AbstractCombinationKeyedHandler<V> {

	protected Class<V> valueClass;
	protected String valueColumnName;

	public PairCombinationKeyedHandler(Class<V> valueClass, String valueColumnName, String separator,
			int... keyColumnIndexes) {

		this.valueClass = valueClass;
		this.valueColumnName = valueColumnName;
		this.separator = separator;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public PairCombinationKeyedHandler(Class<V> valueClass, String valueColumnName, String separator,
			String... keyColumnNames) {

		this.valueClass = valueClass;
		this.valueColumnName = valueColumnName;
		this.separator = separator;
		this.keyColumnNames = keyColumnNames;
	}

	@Override
	protected V createValue(ResultSet rs) throws SQLException {

		return getObject(rs, valueColumnName, valueClass, convertService);
	}

}
