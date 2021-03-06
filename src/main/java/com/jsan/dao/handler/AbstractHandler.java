package com.jsan.dao.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
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
import com.jsan.convert.ConvertFuncUtils;
import com.jsan.convert.ConvertService;
import com.jsan.convert.Converter;
import com.jsan.convert.Mold;
import com.jsan.convert.cache.BeanConvertServiceCache;
import com.jsan.convert.cache.BeanConvertServiceContainer;
import com.jsan.convert.cache.BeanInformationCache;
import com.jsan.dao.FieldNameHandler;
import com.jsan.dao.FieldValueHandler;
import com.jsan.dao.TypeCastHandler;
import com.jsan.dao.map.CaseInsensitiveMap;

public abstract class AbstractHandler<T> implements EnhancedResultSetHandler<T> {

	protected ConvertService convertService;
	protected TypeCastHandler typeCastHandler;
	protected FieldNameHandler fieldNameHandler;
	protected FieldValueHandler fieldValueHandler;
	protected boolean fieldCaseInsensitive;
	protected boolean fieldToLowerCase;
	protected boolean fieldInSnakeCase;

	@Override
	public void setConvertService(ConvertService convertService) {

		this.convertService = convertService;
	}

	@Override
	public void setTypeCastHandler(TypeCastHandler typeCastHandler) {

		this.typeCastHandler = typeCastHandler;
	}

	@Override
	public void setFieldNameHandler(FieldNameHandler fieldNameHandler) {

		this.fieldNameHandler = fieldNameHandler;
	}

	@Override
	public void setFieldValueHandler(FieldValueHandler fieldValueHandler) {

		this.fieldValueHandler = fieldValueHandler;
	}

	@Override
	public void setFieldCaseInsensitive(boolean fieldCaseInsensitive) {

		this.fieldCaseInsensitive = fieldCaseInsensitive;
	}

	@Override
	public void setFieldToLowerCase(boolean fieldToLowerCase) {

		this.fieldToLowerCase = fieldToLowerCase;
	}

	@Override
	public void setFieldInSnakeCase(boolean fieldInSnakeCase) {

		this.fieldInSnakeCase = fieldInSnakeCase;
	}

	@SuppressWarnings("unchecked")
	protected <O> O getObject(ResultSet rs, int columnIndex, Class<O> type, ConvertService service)
			throws SQLException {

		Object obj = null;

		Converter converter = service.lookupConverter(type);

		obj = rs.getObject(columnIndex);
		obj = typeCastHandle(obj);
		obj = fieldValueHandle(columnIndex, null, obj);
		obj = converter.convert(obj, type);

		return (O) obj; // 不能使用 type.cast(obj)，因为type 可能会是基本数据类型
	}

	@SuppressWarnings("unchecked")
	protected <O> O getObject(ResultSet rs, String columnName, Class<O> type, ConvertService service)
			throws SQLException {

		Object obj = null;

		Converter converter = service.lookupConverter(type);

		obj = rs.getObject(columnName);
		obj = typeCastHandle(obj);
		obj = fieldValueHandle(0, columnName, obj);
		obj = converter.convert(obj, type);

		return (O) obj; // 不能使用 type.cast(obj)，因为type 可能会是基本数据类型
	}

	protected Map<String, Object> getMap(ResultSet rs) throws SQLException {

		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		Map<String, Object> map = fieldCaseInsensitive ? new CaseInsensitiveMap<Object>(cols)
				: new LinkedHashMap<String, Object>(cols);

		for (int i = 1; i <= cols; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (columnName == null || columnName.length() == 0) {
				columnName = rsmd.getColumnName(i);
			}

			if (fieldToLowerCase) {
				columnName = columnName.toLowerCase();
			}

			Object obj = handleColumnValue(rs, rsmd, i);
			obj = typeCastHandle(obj);
			columnName = fieldNameHandle(i, columnName, obj);
			obj = fieldValueHandle(i, columnName, obj);
			map.put(columnName, obj);
		}

		return map;
	}

	protected <B> B getBean(ResultSet rs, Class<B> beanClass, ConvertService service) throws SQLException {

		B bean = newInstance(beanClass);

		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();

		Map<String, Method> writeMethodMap = BeanInformationCache.getWriteMethodMap(beanClass);
		BeanConvertServiceContainer container = BeanConvertServiceCache.getConvertServiceContainer(Mold.DAO, beanClass,
				service);

		try {
			for (int i = 1; i <= cols; i++) {
				String columnName = rsmd.getColumnLabel(i);
				if (columnName == null || columnName.length() == 0) {
					columnName = rsmd.getColumnName(i);
				}

				if (fieldToLowerCase) {
					columnName = columnName.toLowerCase();
				}

				if (fieldInSnakeCase) {
					columnName = ConvertFuncUtils.parseSnakeCaseToCamelCase(columnName); // 如果列名含有下划线，则将其转为驼峰形式的命名规范，注意这里不会对首字母做大小写处理
				}

				Method method = writeMethodMap.get(columnName);
				if (method != null) {

					Object obj = handleColumnValue(rs, rsmd, i);
					obj = typeCastHandle(obj);
					columnName = fieldNameHandle(i, columnName, obj);
					obj = fieldValueHandle(i, columnName, obj);

					BeanConvertUtils.convertBeanElement(bean, beanClass, service, container, method, obj);
				}
			}
		} catch (Exception e) {
			throw new SQLException(e);
		}

		return bean;
	}

	protected <P> P getKey(ResultSet rs, Class<P> keyClass, int keyColumnIndex, String keyColumnName)
			throws SQLException {

		P k = null;

		if (keyColumnName == null) {
			k = getObject(rs, keyColumnIndex, keyClass, convertService);
		} else {
			k = getObject(rs, keyColumnName, keyClass, convertService);
		}

		return k;
	}

	protected String getCombinationKey(ResultSet rs, String separator, int[] keyColumnIndexes, String[] keyColumnNames)
			throws SQLException {

		StringBuilder sb = new StringBuilder();

		if (keyColumnNames == null) {
			for (int i = 0; i < keyColumnIndexes.length; i++) {
				if (i > 0 && separator != null) {
					sb.append(separator);
				}
				sb.append(getObject(rs, keyColumnIndexes[i], String.class, convertService));
			}
		} else {
			for (int i = 0; i < keyColumnNames.length; i++) {
				if (i > 0 && separator != null) {
					sb.append(separator);
				}
				sb.append(getObject(rs, keyColumnNames[i], String.class, convertService));
			}
		}

		return sb.toString();
	}

	protected <B> B newInstance(Class<B> type) throws SQLException {

		try {
			return BeanProxyUtils.newInstance(type);
		} catch (Exception e) {
			throw new SQLException("Cannot create instance: " + type.getName() + ": " + e.getMessage());
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
	 * 类型转换或字符编码转换处理器的相关操作。
	 * 
	 * @param obj
	 * @return
	 */
	protected Object typeCastHandle(Object obj) {

		if (typeCastHandler != null) {
			return typeCastHandler.handeOutput(obj);
		}
		return obj;
	}

	/**
	 * 字段名处理器的相关操作。
	 * 
	 * @param columnIndex
	 * @param columnName
	 * @param obj
	 * @return
	 */
	protected String fieldNameHandle(int columnIndex, String columnName, Object obj) {

		if (fieldNameHandler != null) {
			return fieldNameHandler.handle(columnIndex, columnName, obj);
		}
		return columnName;
	}

	/**
	 * 字段值处理器的相关操作。
	 * 
	 * @param columnIndex
	 * @param columnName
	 * @param obj
	 * @return
	 */
	protected Object fieldValueHandle(int columnIndex, String columnName, Object obj) {

		if (fieldValueHandler != null) {
			return fieldValueHandler.handle(columnIndex, columnName, obj);
		}
		return obj;
	}

}
