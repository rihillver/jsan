package com.jsan.dao.handler.support.keyed.multivalue.set;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.jsan.dao.handler.support.keyed.multivalue.AbstractSetMultiValueCombinationKeyedHandler;
import com.jsan.dao.map.SetMultiValueMap;

public class MapSetMultiValueCombinationKeyedHandler
		extends AbstractSetMultiValueCombinationKeyedHandler<Map<String, Object>> {

	public MapSetMultiValueCombinationKeyedHandler(SetMultiValueMap<String, Map<String, Object>> multiValueMap,
			String separator, int... keyColumnIndexes) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public MapSetMultiValueCombinationKeyedHandler(String separator, int... keyColumnIndexes) {

		this(null, separator, keyColumnIndexes);
	}

	public MapSetMultiValueCombinationKeyedHandler(SetMultiValueMap<String, Map<String, Object>> multiValueMap,
			String separator, String... keyColumnNames) {

		this.multiValueMap = multiValueMap;
		this.separator = separator;
		this.keyColumnNames = keyColumnNames;
	}

	public MapSetMultiValueCombinationKeyedHandler(String separator, String... keyColumnNames) {

		this(null, separator, keyColumnNames);
	}

	@Override
	protected Map<String, Object> createValue(ResultSet rs) throws SQLException {

		return getMap(rs);
	}

}
