package com.jsan.dao.handler.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.jsan.dao.handler.AbstractListHandler;

public class MapListHandler extends AbstractListHandler<Map<String, Object>> {

	public MapListHandler() {

	}

	public MapListHandler(List<Map<String, Object>> list) {

		this.list = list;
	}

	@Override
	protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {

		return getMap(rs);
	}

}
