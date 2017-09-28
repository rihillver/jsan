package com.jsan.dao.handler;

import com.jsan.convert.ConvertService;
import com.jsan.dao.FieldNameHandler;
import com.jsan.dao.FieldValueHandler;

public interface EnhancedResultSetHandler<T> extends ResultSetHandler<T> {

	void setConvertService(ConvertService convertService);

	void setFieldValueHandler(FieldValueHandler fieldValueHandler);

	void setFieldNameHandler(FieldNameHandler fieldNameHandler);

	void setFieldCaseInsensitive(boolean fieldCaseInsensitive);

	void setFieldToLowerCase(boolean fieldToLowerCase);
	
	void setFieldInSnakeCase(boolean fieldInSnakeCase);

}
