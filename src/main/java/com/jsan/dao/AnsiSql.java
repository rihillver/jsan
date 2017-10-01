package com.jsan.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public class AnsiSql extends AbstractSqlx {

	public AnsiSql() {

	}

	public AnsiSql(Connection connection) {

		super(connection);
	}

	public AnsiSql(ResultSet resultSet) {

		super(resultSet);
	}

	@Override
	protected String getPageSqlProcessed(String sql, int pageSize, int pageNumber) {

		return sql;
	}

}
