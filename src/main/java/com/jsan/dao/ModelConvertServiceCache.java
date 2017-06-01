package com.jsan.dao;

import java.util.HashMap;
import java.util.Map;

import com.jsan.convert.ConvertFuncUtils;
import com.jsan.convert.ConvertService;
import com.jsan.convert.annotation.ConverterRegister;
import com.jsan.convert.annotation.DateTimePattern;
import com.jsan.convert.annotation.NumberPattern;

public class ModelConvertServiceCache {

	private static final Map<Class<?>, ConvertService> convertServiceMap = new HashMap<Class<?>, ConvertService>();

	public static ConvertService getConvertService(Class<?> beanClass,
			Class<? extends ConvertService> convertServiceClass) {

		ConvertService service = convertServiceMap.get(beanClass);

		if (service == null) {
			synchronized (convertServiceMap) {
				service = convertServiceMap.get(beanClass);
				if (service == null) {
					service = createConvertService(beanClass, convertServiceClass);
					convertServiceMap.put(beanClass, service);
				}
			}
		}

		return service;
	}

	private static ConvertService createConvertService(Class<?> beanClass,
			Class<? extends ConvertService> convertServiceClass) {

		ConvertService srvice = null;

		try {
			srvice = convertServiceClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ConverterRegister converterRegister = beanClass.getAnnotation(ConverterRegister.class);
		DateTimePattern dateTimePattern = beanClass.getAnnotation(DateTimePattern.class);
		NumberPattern numberPattern = beanClass.getAnnotation(NumberPattern.class);

		if (converterRegister != null || dateTimePattern != null || numberPattern != null) {
			if (srvice == null) {
				srvice = DaoConfig.createConvertService();
			}
			ConvertFuncUtils.registerConverterAndFormatter(srvice, converterRegister, dateTimePattern,
					numberPattern);
		} else {
			if (srvice == null) {
				srvice = DaoConfig.getConvertService();
			}
		}

		return srvice;
	}
}
