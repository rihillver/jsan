package com.jsan.dao.handler.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.jsan.dao.handler.AbstractListHandler;

public class ObjectListHandler<T> extends AbstractListHandler<T> {

	private Class<T> type;

	public ObjectListHandler(Class<T> type) {

		this(null, type);
	}

	public ObjectListHandler(List<T> list, Class<T> type) {

		this.list = list;
		this.type = type;
	}

	@Override
	protected T handleRow(ResultSet rs) throws SQLException {

		return getObject(rs, 1, type, convertService);
	}
}
