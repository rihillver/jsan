package com.jsan.mvc;

import java.lang.reflect.Type;
import java.util.Set;

import com.jsan.convert.ConvertService;
import com.jsan.convert.annotation.ConvertServiceRegister;
import com.jsan.convert.annotation.ConverterRegister;
import com.jsan.convert.annotation.DateTimePattern;
import com.jsan.convert.annotation.NumberPattern;
import com.jsan.mvc.annotation.FormConvert;
import com.jsan.mvc.annotation.JsonConvert;
import com.jsan.mvc.annotation.MethodValue;
import com.jsan.mvc.annotation.MultiValue;
import com.jsan.mvc.json.JsonParserConfigurator;

/**
 * 方法形参信息。
 *
 */

public class ParameterInfo {

	private String name; // 形参名
	private Class<?> type; // 形参类型
	private Type genericType; // 形参参数化类型
	private ConvertService convertService;
	private JsonParserConfigurator jsonParserConfigurator;

	private Set<String> multiValueSet;

	private FormConvert formConvert;
	private JsonConvert jsonConvert;
	private MethodValue methodValue;
	private MultiValue multiValue;
	private ConvertServiceRegister convertServiceRegister;
	private ConverterRegister converterRegister;
	private DateTimePattern dateTimePattern;
	private NumberPattern numberPattern;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Type getGenericType() {
		return genericType;
	}

	public void setGenericType(Type genericType) {
		this.genericType = genericType;
	}

	public ConvertService getConvertService() {
		return convertService;
	}

	public void setConvertService(ConvertService convertService) {
		this.convertService = convertService;
	}

	public FormConvert getFormConvert() {
		return formConvert;
	}

	public void setFormConvert(FormConvert formConvert) {
		this.formConvert = formConvert;
	}

	public JsonConvert getJsonConvert() {
		return jsonConvert;
	}

	public void setJsonConvert(JsonConvert jsonConvert) {
		this.jsonConvert = jsonConvert;
	}

	public MethodValue getMethodValue() {
		return methodValue;
	}

	public void setMethodValue(MethodValue methodValue) {
		this.methodValue = methodValue;
	}

	public ConvertServiceRegister getConvertServiceRegister() {
		return convertServiceRegister;
	}

	public void setConvertServiceRegister(ConvertServiceRegister convertServiceRegister) {
		this.convertServiceRegister = convertServiceRegister;
	}

	public ConverterRegister getConverterRegister() {
		return converterRegister;
	}

	public void setConverterRegister(ConverterRegister converterRegister) {
		this.converterRegister = converterRegister;
	}

	public DateTimePattern getDateTimePattern() {
		return dateTimePattern;
	}

	public void setDateTimePattern(DateTimePattern dateTimePattern) {
		this.dateTimePattern = dateTimePattern;
	}

	public NumberPattern getNumberPattern() {
		return numberPattern;
	}

	public void setNumberPattern(NumberPattern numberPattern) {
		this.numberPattern = numberPattern;
	}

	public Set<String> getMultiValueSet() {
		return multiValueSet;
	}

	public void setMultiValueSet(Set<String> multiValueSet) {
		this.multiValueSet = multiValueSet;
	}

	public MultiValue getMultiValue() {
		return multiValue;
	}

	public void setMultiValue(MultiValue multiValue) {
		this.multiValue = multiValue;
	}

	public JsonParserConfigurator getJsonParserConfigurator() {
		return jsonParserConfigurator;
	}

	public void setJsonParserConfigurator(JsonParserConfigurator jsonParserConfigurator) {
		this.jsonParserConfigurator = jsonParserConfigurator;
	}

}
