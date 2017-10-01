package com.jsan.dao;

import java.util.List;
import java.util.Map;

import com.jsan.convert.ConvertFuncUtils;

public class DaoFuncUtils {

	public static String createBeanDefinition(List<RowMetaData> list) {

		return createBeanDefinition(list, false);
	}

	public static String createBeanDefinition(List<RowMetaData> list, boolean fieldToLowerCase) {

		return createBeanDefinition(list, fieldToLowerCase, false);
	}

	public static String createBeanDefinition(List<RowMetaData> list, boolean fieldToLowerCase,
			boolean fieldToCamelCase) {

		return createBeanDefinition(list, fieldToLowerCase, fieldToCamelCase, null);
	}

	public static String createBeanDefinition(List<RowMetaData> list, boolean fieldToLowerCase,
			boolean fieldToCamelCase, Map<String, String> fieldCommentMap) {

		StringBuilder sb = new StringBuilder();

		for (RowMetaData rmd : list) {
			String columnClassName = rmd.getColumnClassName();
			int i = columnClassName.lastIndexOf('.');
			if (i > -1) {
				columnClassName = columnClassName.substring(i + 1);
			}

			sb.append("private ");
			sb.append(columnClassName);
			sb.append(" ");

			String columnName = rmd.getColumnLabel();
			if (columnName == null || columnName.length() == 0) {
				columnName = rmd.getColumnName();
			}

			String fieldName = columnName;
			if (fieldToLowerCase) {
				fieldName = fieldName.toLowerCase();
			}
			if (fieldToCamelCase) {
				fieldName = ConvertFuncUtils.parseSnakeCaseToCamelCase(fieldName); // 转为驼峰形式
			}

			sb.append(fieldName);
			sb.append(";");

			if (fieldCommentMap != null) {
				String comment = fieldCommentMap.get(columnName);
				if (comment != null && comment.length() > 0) {
					sb.append(" // ");
					sb.append(comment);
				}
			}

			sb.append("\n");
		}

		return sb.toString();
	}

}
