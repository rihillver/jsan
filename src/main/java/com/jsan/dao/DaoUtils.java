package com.jsan.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 编程式事务管理。
 *
 */

public class DaoUtils {

	public static boolean transaction(Atom atom) {

		return transaction(null, null, atom);
	}

	public static boolean transaction(String dataSourceName, Atom atom) {

		return transaction(dataSourceName, null, atom);
	}

	public static boolean transaction(int isolationLevel, Atom atom) {

		return transaction(null, isolationLevel, atom);
	}

	public static boolean transaction(String dataSourceName, Integer isolationLevel, Atom atom) {

		DaoConfig.setThreadLocalConnection(dataSourceName);
		Connection conn = DaoConfig.getThreadLocalConnection();

		try {
			conn.setAutoCommit(false);
			if (isolationLevel != null) {
				conn.setTransactionIsolation(isolationLevel);
			}
			boolean result = atom.run();
			if (result) {
				conn.commit();
			} else {
				conn.rollback();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException(e1);
			}
			return false;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				DaoConfig.removeThreadLocalConnection();
			}
		}
	}

	public static void transactionBegin() {

		transactionBegin(null, null);
	}

	public static void transactionBegin(String dataSourceName) {

		transactionBegin(dataSourceName, null);
	}

	public static void transactionBegin(int isolationLevel) {

		transactionBegin(null, isolationLevel);
	}

	public static void transactionBegin(String dataSourceName, Integer isolationLevel) {

		DaoConfig.setThreadLocalConnection(dataSourceName);
		Connection conn = DaoConfig.getThreadLocalConnection();

		try {
			conn.setAutoCommit(false);
			if (isolationLevel != null) {
				conn.setTransactionIsolation(isolationLevel);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void transactionCommit() throws SQLException {

		Connection conn = DaoConfig.getThreadLocalConnection();
		conn.commit();
		conn.close();
		DaoConfig.removeThreadLocalConnection();
	}

	public static void transactionRollback() {

		Connection conn = DaoConfig.getThreadLocalConnection();

		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				} finally {
					DaoConfig.removeThreadLocalConnection();
				}
			}
		}
	}

}
