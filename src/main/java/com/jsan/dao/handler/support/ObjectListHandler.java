package com.jsan.dao.handler.support;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.AbstractListHandler;

public class ObjectListHandler<T> extends AbstractListHandler<T> {

	private Class<T> type;

	public ObjectListHandler(Class<T> type) {

		this.type = type;
	}

	@Override
	protected T handleRow(ResultSet rs) throws SQLException {

		return getObject(rs, 1, type, convertService);
	}
}
