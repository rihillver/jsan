package com.jsan.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ConnectionProvider 接口。
 * <p>
 * 请确保 ConnectionProvider 的实现类在单例下是线程安全的。
 *
 */

public interface ConnectionProvider {

	/**
	 * 允许 dataSourceName 为 null，当 dataSourceName 为 null 时请通过默认数据源返回 Connection。
	 * 
	 * @param dataSourceName
	 * @return
	 */
	Connection getConnection(String dataSourceName) throws SQLException;
}
