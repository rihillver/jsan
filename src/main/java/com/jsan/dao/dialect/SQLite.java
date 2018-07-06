package com.jsan.dao.dialect;

import java.sql.Connection;
import java.sql.ResultSet;

import com.jsan.dao.AbstractSqlx;

public class SQLite extends AbstractSqlx {

	public SQLite() {

	}

	public SQLite(Connection connection) {

		super(connection);
	}

	public SQLite(ResultSet resultSet) {

		super(resultSet);
	}

	@Override
	protected String getPageSqlProcessed(String sql, int pageSize, int startRow, int endRow) {

		// String pageSql = "select * from blog where bid>5 order by bid desc
		// limit ? offset ?";

		//int startRow = pageSize * (pageNumber - 1); // 开始行数

		return sql + " limit " + pageSize + " offset " + startRow;
	}

}
