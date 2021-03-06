package com.jsan.convert.cache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class BeanInformationContainer {

	private Set<String> fieldSet; // 所有字段名，仅含自身的所有字段，不含父类的任何字段
	private Map<String, Field> fieldMap; // 所有字段，key为字段名，仅含自身的所有字段，不含父类的任何字段
	private Map<String, Method> readMethodMap; // 所有Getter方法，key为字段名，含父类的公共方法，不含自身的私有方法
	private Map<String, Method> readMethodMapBaseOnField; // 所有与自身字段相对应的Getter方法，key为字段名，不含父类的公共方法，不含自身的私有方法
	private Map<String, Method> writeMethodMap; // 所有Setter方法，key为字段名，含父类的公共方法，不含自身的私有方法
	private Map<String, Method> writeMethodMapBaseOnField; // 所有与自身字段相对应的Setter方法，key为字段名，不含父类的公共方法，不含自身的私有方法

	public Set<String> getFieldSet() {
		return fieldSet;
	}

	public void setFieldSet(Set<String> fieldSet) {
		this.fieldSet = fieldSet;
	}

	public Map<String, Field> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, Field> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map<String, Method> getReadMethodMap() {
		return readMethodMap;
	}

	public void setReadMethodMap(Map<String, Method> readMethodMap) {
		this.readMethodMap = readMethodMap;
	}

	public Map<String, Method> getReadMethodMapBaseOnField() {
		return readMethodMapBaseOnField;
	}

	public void setReadMethodMapBaseOnField(Map<String, Method> readMethodMapBaseOnField) {
		this.readMethodMapBaseOnField = readMethodMapBaseOnField;
	}

	public Map<String, Method> getWriteMethodMap() {
		return writeMethodMap;
	}

	public void setWriteMethodMap(Map<String, Method> writeMethodMap) {
		this.writeMethodMap = writeMethodMap;
	}

	public Map<String, Method> getWriteMethodMapBaseOnField() {
		return writeMethodMapBaseOnField;
	}

	public void setWriteMethodMapBaseOnField(Map<String, Method> writeMethodMapBaseOnField) {
		this.writeMethodMapBaseOnField = writeMethodMapBaseOnField;
	}

}
