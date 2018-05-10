package com.jsan.dao.dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

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

	/**
	 * 由于 java.util.Date 对于 Oracle 和 SQLServer 的 JDBC 驱动可能需要转换成
	 * java.sql.Timestamp 才能插入，因此这里重写父类方法特别处理。
	 * <p>
	 * SQLServer 数据库的 Timestamp 数据类型是一种二进制的数据，不是时间格式，不能将显式值插入时间戳列，一般为系统自动生成。
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	protected Object getSqlParameterTypeCastProcessed(Object obj) {

		if (obj.getClass() == Date.class) {
			return new Timestamp(((Date) obj).getTime());
		}
		return obj;
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
