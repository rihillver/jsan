package com.jsan.dao.handler.support;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsan.dao.handler.AbstractHandler;

public class BeanHandler<T> extends AbstractHandler<T> {

	private Class<T> beanClass;

	public BeanHandler(Class<T> beanClass) {

		this.beanClass = beanClass;
	}

	@Override
	public T handle(ResultSet rs) throws SQLException {

		T bean = null;

		if (rs.next()) {
			bean = getBean(rs, beanClass, convertService);
		}

		return bean;
	}

}
