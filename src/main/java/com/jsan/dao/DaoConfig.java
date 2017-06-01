package com.jsan.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jsan.convert.ConvertService;
import com.jsan.convert.Converter;
import com.jsan.convert.Formatter;
import com.jsan.convert.GeneralConvertService;

public class DaoConfig {

	private static final String DEFAULT_CONFIG_FILE = "/jsandao.properties"; // 默认配置文件

	// 请保持以下顺序，避免引用异常
	private static final Properties configProperties = createConfigProperties();
	private static final ConnectionProvider connectionProvider = createConnectionProvider();
	private static final Class<? extends Sqlx> sqlxClass = createSqlxClass();
	private static final Class<? extends ConvertService> convertServiceClass = createConvertServiceClass();

	private static final List<Class<? extends Converter>> customConverterList = createCustomConverterList();
	private static final List<Class<? extends Formatter>> customFormatterList = createCustomFormatterList();
	private static final ConvertService convertService = createConvertService();
	private static final boolean debug = createDebug();

	private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

	private static ConnectionProvider createConnectionProvider() {

		try {
			Class<?> connectionProviderClass = Class
					.forName(configProperties.getProperty(ConnectionProvider.class.getName()));
			return (ConnectionProvider) connectionProviderClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends Sqlx> createSqlxClass() {

		try {
			return (Class<? extends Sqlx>) Class.forName(configProperties.getProperty(Sqlx.class.getName()));
		} catch (Exception e) {
			return AnsiSql.class; // 默认
		}
	}

	private static Properties createConfigProperties() {

		try {
			return DaoFuncUtils.getProperties(DEFAULT_CONFIG_FILE);
		} catch (IOException e) {
			throw new RuntimeException("configProperties can not be null");
		}
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends Converter>> createCustomConverterList() {

		List<Class<? extends Converter>> list = new ArrayList<Class<? extends Converter>>();

		String[] customs = DaoFuncUtils.getStringArrayByProperties(configProperties, Converter.class.getName());
		if (customs != null) {
			for (String className : customs) {
				Class<?> clazz = null;
				try {
					clazz = Class.forName(className);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (clazz != null) {
					list.add((Class<? extends Converter>) clazz);
				}
			}
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends Formatter>> createCustomFormatterList() {

		List<Class<? extends Formatter>> list = new ArrayList<Class<? extends Formatter>>();

		String[] customs = DaoFuncUtils.getStringArrayByProperties(configProperties, Converter.class.getName());
		if (customs != null) {
			for (String className : customs) {
				Class<?> clazz = null;
				try {
					clazz = Class.forName(className);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (clazz != null) {
					list.add((Class<? extends Formatter>) clazz);
				}
			}
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends ConvertService> createConvertServiceClass() {

		try {
			return (Class<? extends ConvertService>) Class
					.forName(configProperties.getProperty(ConvertService.class.getName()));
		} catch (Exception e) {
			return GeneralConvertService.class; // 默认
		}
	}

	private static boolean createDebug() {

		try {
			Class.forName(configProperties.getProperty(Debug.class.getName()));
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 通过配置文件中指定的 ConvertService 的实现类，创建 ConvertService ，并注册和声明配置文件中的转换器和格式化器。
	 * 
	 * @return
	 */
	public static ConvertService createConvertService() {

		ConvertService convertService;
		try {
			convertService = convertServiceClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		for (Class<? extends Converter> converterClass : customConverterList) {
			convertService.registerConverter(converterClass);
		}

		for (Class<? extends Formatter> formatterClass : customFormatterList) {
			convertService.declareFormatterClass(formatterClass);
		}

		return convertService;
	}

	/**
	 * 返回通过配置文件中指定的 ConvertService 的实现类的实例，这里返回的是同一个实例，请确保不要对此 ConvertService
	 * 实例作任何修改。
	 * 
	 * @return
	 */
	public static ConvertService getConvertService() {

		return convertService;
	}

	public static boolean isDebug() {

		return debug;
	}

	public static void printDebugMessage(String message) {

		System.out.println("[dao] " + message);
	}

	public static Connection getConnection() {

		return getConnection(null);
	}

	public static Connection getConnection(String dataSourceName) {

		Connection conn = threadLocal.get();
		boolean flag = true;

		if (conn == null) {
			try {
				conn = connectionProvider.getConnection(dataSourceName);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			flag = false;
		}

		if (isDebug()) {
			printDebugMessage("dataSourceName -- (" + dataSourceName + (flag ? "/byThreadLocal" : "") + ") " + conn);
		}

		return conn;
	}

	public static Class<? extends Sqlx> getSqlxClass() {

		return sqlxClass;
	}

	public static Sqlx getSqlx() {

		try {
			return sqlxClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Sqlx getSqlx(String dataSourceName) {

		Sqlx sqlx = getSqlx();
		sqlx.setConnection(getConnection(dataSourceName));

		return sqlx;
	}

	public static void setThreadLocalConnection() {

		setThreadLocalConnection(null);
	}

	public static void setThreadLocalConnection(String dataSourceName) {

		threadLocal.remove(); // 必须先移除
		threadLocal.set(getConnection(dataSourceName));
	}

	public static Connection getThreadLocalConnection() {

		return threadLocal.get();
	}

	public static void removeThreadLocalConnection() {

		threadLocal.remove();
	}

}
