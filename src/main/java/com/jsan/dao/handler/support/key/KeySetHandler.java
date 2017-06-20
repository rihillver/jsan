package com.jsan.dao.handler.support.key;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.jsan.dao.handler.AbstractHandler;

public class KeySetHandler<K> extends AbstractHandler<Set<K>> {

	protected Set<K> set;

	protected Class<K> keyClass;
	protected int keyColumnIndex = 1; // 键默认为第1列
	protected String keyColumnName;

	public KeySetHandler(Class<K> keyClass) {

		this(null, keyClass);
	}

	public KeySetHandler(Set<K> set, Class<K> keyClass) {

		this.set = set;
		this.keyClass = keyClass;
	}

	public KeySetHandler(Class<K> keyClass, int keyColumnIndex) {

		this(null, keyClass, keyColumnIndex);
	}

	public KeySetHandler(Set<K> set, Class<K> keyClass, int keyColumnIndex) {

		this.set = set;
		this.keyClass = keyClass;
		this.keyColumnIndex = keyColumnIndex;
	}

	public KeySetHandler(Class<K> keyClass, String keyColumnName) {

		this(null, keyClass, keyColumnName);
	}

	public KeySetHandler(Set<K> set, Class<K> keyClass, String keyColumnName) {

		this.set = set;
		this.keyClass = keyClass;
		this.keyColumnName = keyColumnName;
	}

	@Override
	public Set<K> handle(ResultSet rs) throws SQLException {

		Set<K> result = createSet();

		while (rs.next()) {
			result.add(createKey(rs));
		}

		return result;
	}

	protected Set<K> createSet() {

		return set == null ? new LinkedHashSet<K>() : set;
	}

	protected K createKey(ResultSet rs) throws SQLException {

		return getKey(rs, keyClass, keyColumnIndex, keyColumnName);
	}

}
