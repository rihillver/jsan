package com.jsan.dao.handler.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.jsan.dao.handler.AbstractHandler;

public class MapHandler extends AbstractHandler<Map<String, Object>> {

	@Override
	public Map<String, Object> handle(ResultSet rs) throws SQLException {

		Map<String, Object> map = null;

		if (rs.next()) {
			map = getMap(rs);
		}

		return map;
	}

}
