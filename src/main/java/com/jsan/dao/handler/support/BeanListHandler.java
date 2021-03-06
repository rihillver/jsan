package com.jsan.dao.handler.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.jsan.dao.handler.AbstractListHandler;

public class BeanListHandler<T> extends AbstractListHandler<T> {

	private Class<T> beanClass;

	public BeanListHandler(Class<T> beanClass) {

		this(null, beanClass);
	}

	public BeanListHandler(List<T> list, Class<T> beanClass) {

		this.list = list;
		this.beanClass = beanClass;
	}

	@Override
	protected T handleRow(ResultSet rs) throws SQLException {

		return getBean(rs, beanClass, convertService);
	}

}
