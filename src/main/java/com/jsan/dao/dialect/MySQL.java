package com.jsan.dao.dialect;

import java.sql.Connection;
import java.sql.ResultSet;

import com.jsan.dao.AbstractSqlx;

public class MySQL extends AbstractSqlx {

	public MySQL() {

	}

	public MySQL(Connection connection) {

		super(connection);
	}

	public MySQL(ResultSet resultSet) {

		super(resultSet);
	}

	@Override
	protected String getPageSqlProcessed(String sql, int pageSize, int startRow, int endRow) {

		// String pageSql = "select * from `blog` where `bid`>5 order by `bid`
		// desc limit ?,?";

		//int startRow = pageSize * (pageNumber - 1); // 开始行数

		return sql + " limit " + startRow + "," + pageSize;
	}
}
