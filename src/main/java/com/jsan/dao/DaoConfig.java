package com.jsan.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsan.convert.ConvertFuncUtils;
import com.jsan.convert.ConvertService;
import com.jsan.convert.Converter;
import com.jsan.convert.Formatter;
import com.jsan.convert.GeneralConvertService;
import com.jsan.convert.PropertiesConvertUtils;

public class DaoConfig {

	private static final Logger logger = LoggerFactory.getLogger(DaoConfig.class);

	private static final String DEFAULT_CONFIG_FILE = "/jsandao.properties"; // 默认配置文件

	// 请保持以下顺序，避免引用异常
	private static final Properties configProperties = createConfigProperties();
	private static final ConnectionProvider connectionProvider = createConnectionProvider();
	private static final Class<? extends Sqlx> sqlxClass = createSqlxClass();
	private static final Class<? extends ConvertService> convertServiceClass = createConvertServiceClass();

	private static final List<Class<? extends Converter>> customConverterList = createCustomConverterList();
	private static final List<Class<? extends Formatter>> customFormatterList = createCustomFormatterList();
	private static final ConvertService convertService = createConvertService();

	private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

	private static ConnectionProvider createConnectionProvider() {

		try {
			Class<?> connectionProviderClass = Class
					.forName(configProperties.getProperty(ConnectionProvider.class.getName()));
			ConnectionProvider provider = (ConnectionProvider) connectionProviderClass.newInstance();
			logger.info("Loaded ConnectionProvider: {}", provider.getClass().getName());
			return provider;
		} catch (Exception e) {
			String msg = "Cannot load the implementation class of ConnectionProvider";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends Sqlx> createSqlxClass() {

		Class<? extends Sqlx> sqlxClazz;

		try {
			sqlxClazz = (Class<? extends Sqlx>) Class.forName(configProperties.getProperty(Sqlx.class.getName()));
		} catch (Exception e) {
			sqlxClazz = AnsiSql.class; // 默认
		}

		logger.info("Loaded SqlxClass: {}", sqlxClazz.getName());

		return sqlxClazz;
	}

	private static Properties createConfigProperties() {

		try {
			Properties properties = PropertiesConvertUtils.loadProperties(DEFAULT_CONFIG_FILE);
			logger.info("Loaded ConfigProperties: {}", DEFAULT_CONFIG_FILE);
			return properties;
		} catch (IOException e) {
			String msg = "Cannot load the default configuration file: " + DEFAULT_CONFIG_FILE;
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends Converter>> createCustomConverterList() {

		List<Class<? extends Converter>> list = new ArrayList<Class<? extends Converter>>();

		String[] customs = ConvertFuncUtils.getStringArrayByProperties(configProperties, Converter.class.getName());
		if (customs != null) {
			for (String className : customs) {
				Class<?> clazz;
				try {
					clazz = Class.forName(className);
				} catch (Exception e) {
					String msg = "Cannot load the Converter class: " + className;
					logger.error(msg, e);
					throw new RuntimeException(msg, e);
				}
				list.add((Class<? extends Converter>) clazz);
			}
		}

		logger.info("Loaded CustomConverterList: {}", list);

		return list;
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends Formatter>> createCustomFormatterList() {

		List<Class<? extends Formatter>> list = new ArrayList<Class<? extends Formatter>>();

		String[] customs = ConvertFuncUtils.getStringArrayByProperties(configProperties, Converter.class.getName());
		if (customs != null) {
			for (String className : customs) {
				Class<?> clazz;
				try {
					clazz = Class.forName(className);
				} catch (Exception e) {
					String msg = "Cannot load the Formatter class: " + className;
					logger.error(msg, e);
					throw new RuntimeException(msg, e);
				}
				list.add((Class<? extends Formatter>) clazz);
			}
		}

		logger.info("Loaded CustomFormatterList: {}", list);

		return list;
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends ConvertService> createConvertServiceClass() {

		Class<? extends ConvertService> convertServiceClass;

		try {
			convertServiceClass = (Class<? extends ConvertService>) Class
					.forName(configProperties.getProperty(ConvertService.class.getName()));
		} catch (Exception e) {
			convertServiceClass = GeneralConvertService.class; // 默认
		}

		logger.info("Loaded ConvertServiceClass: {}", convertServiceClass.getName());

		return convertServiceClass;
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
				String msg = "Failed to get connection [" + dataSourceName + "]";
				logger.error(msg, e);
				throw new RuntimeException(msg, e);
			}
			flag = false;
		}

		logger.debug("dataSourceName -- ({}{}) {}", dataSourceName, (flag ? "/byThreadLocal" : ""), conn);

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
