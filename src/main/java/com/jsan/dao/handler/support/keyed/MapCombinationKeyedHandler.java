package com.jsan.dao.handler.support.keyed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.jsan.dao.handler.AbstractCombinationKeyedHandler;

public class MapCombinationKeyedHandler extends AbstractCombinationKeyedHandler<Map<String, Object>> {

	public MapCombinationKeyedHandler(String separator, int... keyColumnIndexes) {

		this.separator = separator;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public MapCombinationKeyedHandler(String separator, String... keyColumnNames) {

		this.separator = separator;
		this.keyColumnNames = keyColumnNames;
	}

	@Override
	protected Map<String, Object> createValue(ResultSet rs) throws SQLException {

		return getMap(rs);
	}

}
