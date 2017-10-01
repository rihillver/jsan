package com.jsan.dao.dialect;

import java.sql.Connection;
import java.sql.ResultSet;

import com.jsan.dao.AbstractSqlx;

public class SQLServer extends AbstractSqlx {

	public SQLServer() {

	}

	public SQLServer(Connection connection) {

		super(connection);
	}

	public SQLServer(ResultSet resultSet) {

		super(resultSet);
	}

	@Override
	protected String getPageSqlProcessed(String sql, int pageSize, int pageNumber) {

		// String pageSql = "select * from (select row_number() over(order by
		// bid) row__num__, * from blog) temp__table__ where row__num__>? and
		// row__num__<=?";

		int startRow = pageSize * (pageNumber - 1); // 开始行数
		int endRow = pageSize * pageNumber; // 结束行数

		String orderByStr;
		int offset = getLastOrderByOffset(sql);
		if (offset > 0) {
			orderByStr = sql.substring(offset);
			sql = sql.substring(0, offset);
		} else {
			orderByStr = "order by rand()";
		}

		sql = sql.replaceFirst("(?i)select", "select row_number() over( " + orderByStr + " ) row__num__,");

		StringBuilder sb = new StringBuilder();
		sb.append("select * from ( ");
		sb.append(sql);
		sb.append(" ) temp__table__ where row__num__ > ");
		sb.append(startRow);
		sb.append(" and row__num__ <= ");
		sb.append(endRow);

		return sb.toString();
	}
}
