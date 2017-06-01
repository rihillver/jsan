package com.jsan.dao.handler.support.keyed.multivalue;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractListMultiValueCombinationKeyedHandler<T>
		extends AbstractListMultiValueKeyedHandler<String, T> {

	protected int[] keyColumnIndexes;
	protected String[] keyColumnNames;
	protected String separator;

	/**
	 * 该方法的默认实现是将各个键通过指定的分隔符拼接成新的组合键，如果需要自定义组合方式，在具体的实现类中覆盖本方法来自定义实现即可。
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	@Override
	protected String createKey(ResultSet rs) throws SQLException {

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

}
