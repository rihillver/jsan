package com.jsan.dao.handler.support.key;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.jsan.dao.handler.AbstractHandler;

public class CombinationKeySetHandler extends AbstractHandler<Set<String>> {

	protected int[] keyColumnIndexes;
	protected String[] keyColumnNames;
	protected String separator;

	@Override
	public Set<String> handle(ResultSet rs) throws SQLException {

		return null;
	}

	protected String createKey(ResultSet rs) throws SQLException {

		StringBuilder sb = new StringBuilder();

		if (keyColumnNames == null) {
			for (int i = 0; i < keyColumnIndexes.length; i++) {
				if (i > 0 && separator != null) {
					sb.append(separator);
				}
				sb.append(getObject(rs, keyColumnIndexes[i], String.class, convertService));
			}
		} else {
			for (int i = 0; i < keyColumnNames.length; i++) {
				if (i > 0 && separator != null) {
					sb.append(separator);
				}
				sb.append(getObject(rs, keyColumnNames[i], String.class, convertService));
			}
		}

		return sb.toString();
	}

}
