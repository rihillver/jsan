package com.jsan.dao.handler;

import com.jsan.convert.ConvertService;
import com.jsan.dao.FieldNameHandler;
import com.jsan.dao.FieldValueHandler;
import com.jsan.dao.TypeCastHandler;

public interface EnhancedResultSetHandler<T> extends ResultSetHandler<T> {

	void setConvertService(ConvertService convertService);

	void setTypeCastHandler(TypeCastHandler typeCastHandler);
	
	void setFieldValueHandler(FieldValueHandler fieldValueHandler);

	void setFieldNameHandler(FieldNameHandler fieldNameHandler);

	void setFieldCaseInsensitive(boolean fieldCaseInsensitive);

	void setFieldToLowerCase(boolean fieldToLowerCase);
	
	void setFieldInSnakeCase(boolean fieldInSnakeCase);

}
