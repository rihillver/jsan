package com.jsan.dao.handler.support.keyed.multivalue.list;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractListMultiValueCombinationKeyedHandler;
import com.jsan.dao.map.ListMultiValueMap;

public class PairListMultiValueCombinationKeyedHandler<V> extends AbstractListMultiValueCombinationKeyedHandler<V> {

	protected Class<V> valueClass;
	protected String valueColumnName;

	public PairListMultiValueCombinationKeyedHandler(ListMultiValueMap<String, V> multiValueMap, Class<V> valueClass,
			String valueColumnName, String separator, int... keyColumnIndexes) {

		this.multiValueMap = multiValueMap;
		this.valueClass = valueClass;
		this.valueColumnName = valueColumnName;
		this.separator = separator;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public PairListMultiValueCombinationKeyedHandler(Class<V> valueClass, String valueColumnName, String separator,
			int... keyColumnIndexes) {

		this(null, valueClass, valueColumnName, separator, keyColumnIndexes);
	}

	public PairListMultiValueCombinationKeyedHandler(ListMultiValueMap<String, V> multiValueMap, Class<V> valueClass,
			String valueColumnName, String separator, String... keyColumnNames) {

		this.multiValueMap = multiValueMap;
		this.valueClass = valueClass;
		this.valueColumnName = valueColumnName;
		this.separator = separator;
		this.keyColumnNames = keyColumnNames;
	}

	public PairListMultiValueCombinationKeyedHandler(Class<V> valueClass, String valueColumnName, String separator,
			String... keyColumnNames) {

		this(null, valueClass, valueColumnName, separator, keyColumnNames);
	}

	@Override
	protected V createValue(ResultSet rs) throws SQLException {

		return getObject(rs, valueColumnName, valueClass, convertService);
	}

}
