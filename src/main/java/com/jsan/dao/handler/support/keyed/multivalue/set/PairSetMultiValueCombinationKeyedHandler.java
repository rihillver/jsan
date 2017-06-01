package com.jsan.dao.handler.support.keyed.multivalue.set;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractSetMultiValueCombinationKeyedHandler;
import com.jsan.dao.map.SetMultiValueMap;

public class PairSetMultiValueCombinationKeyedHandler<V> extends AbstractSetMultiValueCombinationKeyedHandler<V> {

	protected Class<V> valueClass;
	protected String valueColumnName;

	public PairSetMultiValueCombinationKeyedHandler(SetMultiValueMap<String, V> multiValueMap, Class<V> valueClass,
			String valueColumnName, String separator, int... keyColumnIndexes) {

		this.multiValueMap = multiValueMap;
		this.valueClass = valueClass;
		this.valueColumnName = valueColumnName;
		this.separator = separator;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public PairSetMultiValueCombinationKeyedHandler(Class<V> valueClass, String valueColumnName, String separator,
			int... keyColumnIndexes) {

		this(null, valueClass, valueColumnName, separator, keyColumnIndexes);
	}

	public PairSetMultiValueCombinationKeyedHandler(SetMultiValueMap<String, V> multiValueMap, Class<V> valueClass,
			String valueColumnName, String separator, String... keyColumnNames) {

		this.multiValueMap = multiValueMap;
		this.valueClass = valueClass;
		this.valueColumnName = valueColumnName;
		this.separator = separator;
		this.keyColumnNames = keyColumnNames;
	}

	public PairSetMultiValueCombinationKeyedHandler(Class<V> valueClass, String valueColumnName, String separator,
			String... keyColumnNames) {

		this(null, valueClass, valueColumnName, separator, keyColumnNames);
	}

	@Override
	protected V createValue(ResultSet rs) throws SQLException {

		return getObject(rs, valueColumnName, valueClass, convertService);
	}

}
