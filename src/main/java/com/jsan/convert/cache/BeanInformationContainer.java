package com.jsan.convert.cache;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class BeanInformationContainer {

	private Set<String> fieldSet; // 所有字段名，仅含自身的所有字段，不含父类的任何字段
	private Map<String, Method> readMethodMap; // 所有 getter 方法，key
												// 为字段名，含父类的公共方法，不含自身的私有方法
	private Map<String, Method> writeMethodMap; // 所有 setter 方法，key
												// 为字段名，含父类的公共方法，不含自身的私有方法

	public Set<String> getFieldSet() {
		return fieldSet;
	}

	public void setFieldSet(Set<String> fieldSet) {
		this.fieldSet = fieldSet;
	}

	public Map<String, Method> getReadMethodMap() {
		return readMethodMap;
	}

	public void setReadMethodMap(Map<String, Method> readMethodMap) {
		this.readMethodMap = readMethodMap;
	}

	public Map<String, Method> getWriteMethodMap() {
		return writeMethodMap;
	}

	public void setWriteMethodMap(Map<String, Method> writeMethodMap) {
		this.writeMethodMap = writeMethodMap;
	}

}
