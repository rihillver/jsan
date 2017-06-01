package com.jsan.convert;

import java.util.Map;
import java.util.Set;

/**
 * 转换服务接口。
 *
 */

public interface ConvertService {

	ConvertService clone();

	void reinitializeConverterMap(Map<Class<?>, Converter> converterMap);

	void reinitializeFormatterMap(Map<Class<?>, Formatter> formatterMap);

	void reinitializeFormatterClassMap(Map<Class<?>, Class<? extends Formatter>> formatterClassMap);

	void registerConverter(Converter converter);

	void registerConverter(Class<? extends Converter> converterClass);

	Converter lookupConverter(Class<?> type);

	void declareFormatterClass(Class<? extends Formatter> formatterClass);

	void registerFormatter(Formatter formatter);

	void registerFormatter(Class<? extends Formatter> formatterClass);

	Formatter lookupFormatter(Class<?> type);

	Class<? extends Formatter> lookupDeclareFormatterClass(Class<?> type);

	Map<Class<?>, Class<? extends Formatter>> getDeclareFormatterClassMap();

	Set<Class<? extends Formatter>> getDateTimeDeclareFormatterClassSet();

	Set<Class<? extends Formatter>> getNumberDeclareFormatterClassSet();

}
