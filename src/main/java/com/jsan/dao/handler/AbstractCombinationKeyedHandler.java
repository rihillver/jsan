package com.jsan.dao.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractCombinationKeyedHandler<V> extends AbstractKeyedHandler<String, V> {

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

		return getCombinationKey(rs, separator, keyColumnIndexes, keyColumnNames);
	}

}
