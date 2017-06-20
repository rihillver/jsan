package com.jsan.dao.handler.support.key;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.jsan.dao.handler.AbstractHandler;

public class CombinationKeyListHandler extends AbstractHandler<List<String>> {

	protected List<String> list;

	protected int[] keyColumnIndexes;
	protected String[] keyColumnNames;
	protected String separator;

	public CombinationKeyListHandler(String separator, int... keyColumnIndexes) {

		this(null, separator, keyColumnIndexes);
	}

	public CombinationKeyListHandler(List<String> list, String separator, int... keyColumnIndexes) {

		this.list = list;
		this.separator = separator;
		this.keyColumnIndexes = keyColumnIndexes;
	}

	public CombinationKeyListHandler(String separator, String... keyColumnNames) {

		this(null, separator, keyColumnNames);
	}

	public CombinationKeyListHandler(List<String> list, String separator, String... keyColumnNames) {

		this.list = list;
		this.separator = separator;
		this.keyColumnNames = keyColumnNames;
	}

	@Override
	public List<String> handle(ResultSet rs) throws SQLException {

		List<String> result = createList();

		while (rs.next()) {
			result.add(createKey(rs));
		}

		return result;
	}

	protected List<String> createList() {

		return list == null ? new LinkedList<String>() : list;
	}

	protected String createKey(ResultSet rs) throws SQLException {

		return getCombinationKey(rs, separator, keyColumnIndexes, keyColumnNames);
	}

}
