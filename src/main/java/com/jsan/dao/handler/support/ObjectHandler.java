package com.jsan.dao.handler.support;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.AbstractHandler;

public class ObjectHandler<T> extends AbstractHandler<T> {

	private Class<T> type;

	public ObjectHandler(Class<T> type) {

		this.type = type;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T handle(ResultSet rs) throws SQLException {

		Object obj = null;

		if (rs.next()) {
			obj = getObject(rs, 1, type, convertService);
		}

		return (T) obj;
	}

}
