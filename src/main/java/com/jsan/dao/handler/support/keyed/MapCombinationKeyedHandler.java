package com.jsan.dao.handler.support.keyed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.jsan.dao.handler.AbstractCombinationKeyedHandler;

public class MapCombinationKeyedHandler extends AbstractCombinationKeyedHandler<Map<String, Object>> {

	public MapCombinationKeyedHandler(String separator, int... keyColumnIndexes) {

		this(null, separator, keyColumnIndexes);
	}

	public MapCombinationKeyedHandler(Map<String, Map<String, Object>> map, String separator, int... keyColumnIndexes) {

		this.map = map;
		this.separator = separator;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public MapCombinationKeyedHandler(String separator, String... keyColumnNames) {

		this(null, separator, keyColumnNames);
	}

	public MapCombinationKeyedHandler(Map<String, Map<String, Object>> map, String separator,
			String... keyColumnNames) {

		this.map = map;
		this.separator = separator;
		this.keyColumnNames = keyColumnNames;
	}

	@Override
	protected Map<String, Object> createValue(ResultSet rs) throws SQLException {

		return getMap(rs);
	}

}
