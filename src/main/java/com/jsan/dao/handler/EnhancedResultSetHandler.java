package com.jsan.dao.handler;

import com.jsan.convert.ConvertService;
import com.jsan.dao.FieldHandler;

public interface EnhancedResultSetHandler<T> extends ResultSetHandler<T> {

	void setConvertService(ConvertService convertService);

	void setFieldHandler(FieldHandler fieldHandler);

	void setCaseInsensitive(boolean caseInsensitive);

	void setToLowerCase(boolean toLowerCase);

}
