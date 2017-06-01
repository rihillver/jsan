package com.jsan.dao.handler.support.keyed.multivalue.list;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractListMultiValueCombinationKeyedHandler;
import com.jsan.dao.map.ListMultiValueMap;

public class MapListMultiValueCombinationKeyedHandler
		extends AbstractListMultiValueCombinationKeyedHandler<Map<String, Object>> {

	public MapListMultiValueCombinationKeyedHandler(ListMultiValueMap<String, Map<String, Object>> multiValueMap,
			String separator, int... keyColumnIndexes) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public MapListMultiValueCombinationKeyedHandler(String separator, int... keyColumnIndexes) {

		this(null, separator, keyColumnIndexes);
	}

	public MapListMultiValueCombinationKeyedHandler(ListMultiValueMap<String, Map<String, Object>> multiValueMap,
			String separator, String... keyColumnNames) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.keyColumnNames = keyColumnNames;
	}

	public MapListMultiValueCombinationKeyedHandler(String separator, String... keyColumnNames) {

		this(null, separator, keyColumnNames);
	}

	@Override
	protected Map<String, Object> createValue(ResultSet rs) throws SQLException {

		return getMap(rs);
	}

}
