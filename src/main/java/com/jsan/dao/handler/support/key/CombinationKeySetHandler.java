package com.jsan.dao.handler.support.key;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.jsan.dao.handler.AbstractHandler;

public class CombinationKeySetHandler extends AbstractHandler<Set<String>> {

	protected Set<String> set;

	protected int[] keyColumnIndexes;
	protected String[] keyColumnNames;
	protected String separator;

	@Override
	public Set<String> handle(ResultSet rs) throws SQLException {

		Set<String> result = createSet();

		while (rs.next()) {
			result.add(createKey(rs));
		}

		return result;
	}

	protected Set<String> createSet() {

		return set == null ? new LinkedHashSet<String>() : set;
	}

	protected String createKey(ResultSet rs) throws SQLException {

		return getCombinationKey(rs, separator, keyColumnIndexes, keyColumnNames);
	}

}
