package com.jsan.convert;

/**
 * 可递归接口。
 *
 */

public interface Recursiveable {

	void setConvertService(ConvertService convertService);

	ConvertService getConvertService();

	Converter lookupConverter(Class<?> type);

	Formatter lookupFormatter(Class<?> type);

}
