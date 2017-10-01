package com.jsan.convert.cache;

import java.lang.reflect.Method;
import java.util.Map;

import com.jsan.convert.ConvertService;

public class BeanConvertServiceContainer {

	private ConvertService firstConvertService;

	private Map<Method, ConvertService> writeMethodParameterConvertServiceMap;

	public ConvertService getFirstConvertService() {
		return firstConvertService;
	}

	public void setFirstConvertService(ConvertService firstConvertService) {
		this.firstConvertService = firstConvertService;
	}

	public Map<Method, ConvertService> getWriteMethodParameterConvertServiceMap() {
		return writeMethodParameterConvertServiceMap;
	}

	public void setWriteMethodParameterConvertServiceMap(
			Map<Method, ConvertService> writeMethodParameterConvertServiceMap) {
		this.writeMethodParameterConvertServiceMap = writeMethodParameterConvertServiceMap;
	}

	public ConvertService getWriteMethodParameterConvertService(Method method) {

		return writeMethodParameterConvertServiceMap.get(method);
	}

}
