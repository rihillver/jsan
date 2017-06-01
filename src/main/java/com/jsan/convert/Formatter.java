package com.jsan.convert;

/**
 * 格式化器接口。
 *
 */

public interface Formatter {

	Formatter clone();

	void setPattern(String pattern);

	String getPattern();

	Object parse(String text) throws Exception;

	String print(Object object) throws Exception;

}
