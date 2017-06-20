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

	public CombinationKeySetHandler(String separator, int... keyColumnIndexes) {

		this(null, separator, keyColumnIndexes);
	}

	public CombinationKeySetHandler(Set<String> set, String separator, int... keyColumnIndexes) {

		this.set = set;
		this.separator = separator;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public CombinationKeySetHandler(String separator, String... keyColumnNames) {

		this(null, separator, keyColumnNames);
	}

	public CombinationKeySetHandler(Set<String> set, String separator, String... keyColumnNames) {

		this.set = set;
		this.separator = separator;
		this.keyColumnNames = keyColumnNames;
	}

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
