package com.jsan.dao.handler.support.key;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.jsan.dao.handler.AbstractHandler;

public class KeyListHandler<K> extends AbstractHandler<List<K>> {

	protected List<K> list;

	protected Class<K> keyClass;
	protected int keyColumnIndex = 1; // 键默认为第1列
	protected String keyColumnName;

	public KeyListHandler(Class<K> keyClass) {

		this(null, keyClass);
	}

	public KeyListHandler(List<K> list, Class<K> keyClass) {

		this.list = list;
		this.keyClass = keyClass;
	}

	public KeyListHandler(Class<K> keyClass, int keyColumnIndex) {

		this(null, keyClass, keyColumnIndex);
	}

	public KeyListHandler(List<K> list, Class<K> keyClass, int keyColumnIndex) {

		this.list = list;
		this.keyClass = keyClass;
		this.keyColumnIndex = keyColumnIndex;
	}

	public KeyListHandler(Class<K> keyClass, String keyColumnName) {

		this(null, keyClass, keyColumnName);
	}

	public KeyListHandler(List<K> list, Class<K> keyClass, String keyColumnName) {

		this.list = list;
		this.keyClass = keyClass;
		this.keyColumnName = keyColumnName;
	}

	@Override
	public List<K> handle(ResultSet rs) throws SQLException {

		List<K> result = createList();

		while (rs.next()) {
			result.add(createKey(rs));
		}

		return result;
	}

	protected List<K> createList() {

		return list == null ? new LinkedList<K>() : list;
	}

	protected K createKey(ResultSet rs) throws SQLException {

		return getKey(rs, keyClass, keyColumnIndex, keyColumnName);
	}

}
