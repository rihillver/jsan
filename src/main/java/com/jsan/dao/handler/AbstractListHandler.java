package com.jsan.dao.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractListHandler<T> extends AbstractHandler<List<T>> {

	/**
	 * 使用 LinkedList 构建 List ，可作为 List 集合、双端队列、栈。
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<T> handle(ResultSet rs) throws SQLException {

		List<T> list = new LinkedList<T>();

		while (rs.next()) {
			list.add(handleRow(rs));
		}

		return list;
	}

	protected abstract T handleRow(ResultSet rs) throws SQLException;

}
