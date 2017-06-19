package com.jsan.dao.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractListHandler<T> extends AbstractHandler<List<T>> {

	protected List<T> list;

	@Override
	public List<T> handle(ResultSet rs) throws SQLException {

		List<T> list = createList();

		while (rs.next()) {
			list.add(handleRow(rs));
		}

		return list;
	}

	/**
	 * 默认使用 LinkedList 构建 List ，可作为 List 集合、双端队列、栈。
	 * 
	 * @return
	 */
	protected List<T> createList() {

		return list == null ? new LinkedList<T>() : list;
	}

	protected abstract T handleRow(ResultSet rs) throws SQLException;

}
