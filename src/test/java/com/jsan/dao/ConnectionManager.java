package com.jsan.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.jsan.spring.ContextUtils;

/**
 * 集成 Spring 时使其支持 Spring 声明式事务管理器的 ConnectionProvider 实现类测试。
 * <p>
 * 请在 Spring 配置文件中配置 AOP 和相应的事务管理器。
 *
 */

public class ConnectionManager implements ConnectionProvider {

	public static final String MYSQL = "mysql";
	public static final String ORACLE = "oracle";

	private static final String DATA_SOURCE = "DataSource";

	@Override
	public Connection getConnection(String dataSourceName) throws SQLException {

		return getConnectionBySpringTransaction(dataSourceName);
	}

	// ==================================================

	public static DataSource getDataSource() {

		return getDataSource(null);
	}

	public static DataSource getDataSource(String dataSourceName) {

		if (MYSQL.equals(dataSourceName)) {
			return ContextUtils.getBean(MYSQL + DATA_SOURCE, DataSource.class);
		} else if (ORACLE.equals(dataSourceName)) {
			return ContextUtils.getBean(ORACLE + DATA_SOURCE, DataSource.class);
		} else {
			return ContextUtils.getBean(MYSQL + DATA_SOURCE, DataSource.class);
		}
	}

	private static Connection getConnectionBySpringTransaction(String dataSourceName) {

		return DataSourceUtils.getConnection(getDataSource(dataSourceName));
	}

	public static Connection getConnectionByProxool() {

		return getConnectionByProxool(null);
	}

	public static Connection getConnectionByProxool(String dataSourceName) {

		try {
			return getDataSource(dataSourceName).getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static Connection getConnectionByJdbc() {

		return null;
	}

	public static Connection getConnectionByJdbc(String dataSourceName) {

		return null;
	}
}
