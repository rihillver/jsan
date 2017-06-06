package com.jsan.dao.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jsan.convert.BeanConvertUtils;
import com.jsan.convert.BeanProxyUtils;
import com.jsan.convert.ConvertService;
import com.jsan.convert.Converter;
import com.jsan.convert.Mold;
import com.jsan.dao.DaoFuncUtils;
import com.jsan.dao.FieldHandler;
import com.jsan.dao.map.CaseInsensitiveMap;

public abstract class AbstractHandler<T> implements EnhancedResultSetHandler<T> {

	protected ConvertService convertService;
	protected FieldHandler fieldHandler;
	protected boolean caseInsensitive;
	protected boolean toLowerCase;

	@Override
	public void setConvertService(ConvertService convertService) {

		this.convertService = convertService;
	}

	@Override
	public void setFieldHandler(FieldHandler fieldHandler) {

		this.fieldHandler = fieldHandler;
	}

	@Override
	public void setCaseInsensitive(boolean caseInsensitive) {

		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public void setToLowerCase(boolean toLowerCase) {

		this.toLowerCase = toLowerCase;
	}

	@SuppressWarnings("unchecked")
	protected <O> O getObject(ResultSet rs, int columnIndex, Class<O> type, ConvertService service)
			throws SQLException {

		Object obj = null;

		Converter converter = service.lookupConverter(type);

		obj = rs.getObject(columnIndex);
		obj = fieldHandle(null, obj);
		obj = converter.convert(obj, type);

		return (O) obj; // 不能使用 type.cast(obj)，因为type 可能会是基本数据类型
	}

	@SuppressWarnings("unchecked")
	protected <O> O getObject(ResultSet rs, String columnName, Class<O> type, ConvertService service)
			throws SQLException {

		Object obj = null;

		Converter converter = service.lookupConverter(type);

		obj = rs.getObject(columnName);
		obj = fieldHandle(columnName, obj);
		obj = converter.convert(obj, type);

		return (O) obj; // 不能使用 type.cast(obj)，因为type 可能会是基本数据类型
	}

	protected Map<String, Object> getMap(ResultSet rs) throws SQLException {

		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		Map<String, Object> map = caseInsensitive ? new CaseInsensitiveMap<Object>(cols)
				: new LinkedHashMap<String, Object>(cols);

		for (int i = 1; i <= cols; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (columnName == null || columnName.length() == 0) {
				columnName = rsmd.getColumnName(i);
			}

			if (toLowerCase) {
				columnName = columnName.toLowerCase();
			}

			columnName = DaoFuncUtils.parseToCamelCase(columnName); // 如果列名含有下划线，则将其转为驼峰形式的命名规范，注意这里不会对首字母做大小写处理

			Object obj = handleColumnValue(rs, rsmd, i);
			obj = fieldHandle(columnName, obj);
			map.put(columnName, obj);
		}

		return map;
	}

	protected <B> B getBean(ResultSet rs, Class<B> beanClass, ConvertService service) throws SQLException {

		B bean = newInstance(beanClass);

		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();

		try {
			for (int i = 1; i <= cols; i++) {
				String columnName = rsmd.getColumnLabel(i);
				if (columnName == null || columnName.length() == 0) {
					columnName = rsmd.getColumnName(i);
				}

				if (toLowerCase) {
					columnName = columnName.toLowerCase();
				}

				columnName = DaoFuncUtils.parseToCamelCase(columnName); // 如果列名含有下划线，则将其转为驼峰形式的命名规范，注意这里不会对首字母做大小写处理

				Object obj = handleColumnValue(rs, rsmd, i);
				obj = fieldHandle(columnName, obj);
				BeanConvertUtils.convertBeanElement(Mold.DAO, bean, beanClass, service, columnName, obj);
			}
		} catch (Exception e) {
			throw new SQLException(e);
		}

		return bean;
	}

	protected <B> B newInstance(Class<B> type) throws SQLException {

		try {
			return BeanProxyUtils.newInstance(type);
		} catch (Exception e) {
			throw new SQLException("Cannot create " + type.getName() + ": " + e.getMessage());
		}
	}

	/**
	 * 转换处理 Clob、 Nclob、 Blob 类型的数据。
	 * 
	 * @param rs
	 * @param rsmd
	 * @param i
	 * @return
	 * @throws SQLException
	 */
	protected Object handleColumnValue(ResultSet rs, ResultSetMetaData rsmd, int i) throws SQLException {

		Object obj = null;
		int columnType = rsmd.getColumnType(i);

		if (columnType < Types.BLOB) {
			obj = rs.getObject(i);
		} else if (columnType == Types.CLOB) {
			obj = handleClob(rs.getClob(i));
		} else if (columnType == Types.NCLOB) {
			obj = handleClob(rs.getNClob(i));
		} else if (columnType == Types.BLOB) {
			Blob blob = null;
			try {
				blob = rs.getBlob(i);
			} catch (Exception e) {
				obj = rs.getBytes(i); // SQLite 的 JDBC 驱动没有对应实现 Blob 的解决方式
			}
			if (blob != null) {
				obj = handleBlob(blob);
			}
		} else {
			obj = rs.getObject(i);
		}

		return obj;
	}

	/**
	 * 将 Clob 类型转换成 String 类型，最大读取到 2G-1。
	 * 
	 * <p>
	 * 由于使用 char[] 进行转换，限制于 int 的最大值为（2的31次方-1） ，因此只适用于转换大小不超过 2G-1 的数据。
	 * 
	 * @param clob
	 * @return
	 * @throws SQLException
	 */
	protected String handleClob(Clob clob) throws SQLException {

		if (clob == null) {
			return null;
		}

		Reader reader = null;
		try {
			reader = clob.getCharacterStream();
			if (reader == null) {
				return null;
			}
			char[] buffer = new char[(int) clob.length()];
			if (buffer.length == 0) {
				return null;
			}
			reader.read(buffer);
			return new String(buffer);
		} catch (Exception e) {
			throw new SQLException("Cannot handle Clob: " + e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new SQLException("Cannot close Reader: " + e.getMessage());
				}
			}
		}
	}

	/**
	 * 将 Blob 转换成 byte[] 类型，最大读取到 2G-1。
	 * 
	 * <p>
	 * 由于使用 byte[] 进行转换，限制于 int 的最大值为 2 的（2的31次方-1），因此只适用于转换大小不超过 2G-1 的数据。
	 * 
	 * @param blob
	 * @return
	 * @throws SQLException
	 */
	protected byte[] handleBlob(Blob blob) throws SQLException {

		if (blob == null) {
			return null;
		}

		InputStream is = null;
		try {
			is = blob.getBinaryStream();
			if (is == null) {
				return null;
			}
			byte[] bytes = new byte[(int) blob.length()];
			if (bytes.length == 0) {
				return null;
			}
			is.read(bytes);
			return bytes;
		} catch (Exception e) {
			throw new SQLException("Cannot handle Blob: " + e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new SQLException("Cannot close InputStream: " + e.getMessage());
				}
			}
		}
	}

	/**
	 * 字段处理器的相关操作。
	 * 
	 * @param obj
	 * @param columnName
	 * @return
	 */
	protected Object fieldHandle(String columnName, Object obj) {

		return fieldHandler == null ? obj : fieldHandler.handle(columnName, obj);
	}

}
