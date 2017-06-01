package com.jsan.convert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jsan.convert.annotation.ConverterRegister;
import com.jsan.convert.annotation.DateTimePattern;
import com.jsan.convert.annotation.NumberPattern;

public class ConvertFuncUtils {

	public static void registerConverterAndFormatter(ConvertService service, ConverterRegister register,
			DateTimePattern dateTimePattern, NumberPattern numberPattern) {

		registerConverter(service, register);
		registerFormatterByDateTime(service, dateTimePattern);
		registerFormatterByNumber(service, numberPattern);
	}

	public static void registerConverter(ConvertService service, ConverterRegister register) {

		if (register != null) {
			Class<? extends Converter>[] converterClasses = register.value();
			for (Class<? extends Converter> converterClass : converterClasses) {
				service.registerConverter(converterClass);
			}
		}
	}

	public static void registerFormatterByDateTime(ConvertService service, DateTimePattern dateTimePattern) {

		if (dateTimePattern != null) {
			String pattern = dateTimePattern.value();
			for (Class<? extends Formatter> formatterClass : service.getDateTimeDeclareFormatterClassSet()) {
				executeRegisterFormatter(service, formatterClass, pattern);
			}
		}
	}

	public static void registerFormatterByNumber(ConvertService service, NumberPattern numberPattern) {

		if (numberPattern != null) {
			String pattern = numberPattern.value();
			for (Class<? extends Formatter> formatterClass : service.getNumberDeclareFormatterClassSet()) {
				executeRegisterFormatter(service, formatterClass, pattern);
			}
		}
	}

	private static void executeRegisterFormatter(ConvertService service, Class<? extends Formatter> formatterClass,
			String pattern) {

		Formatter formatter = createFormatterInstance(formatterClass);
		formatter.setPattern(pattern);
		service.registerFormatter(formatter);
	}

	private static Formatter createFormatterInstance(Class<? extends Formatter> formatterClass) {

		try {
			return BeanProxyUtils.newInstance(formatterClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String parseFirstCharToLowerCase(String str) {

		if (str == null) {
			return null;
		}

		char firstChar = str.charAt(0);

		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}

		return str;
	}

	public static Properties getProperties(String path) throws IOException {

		InputStream inputStream = Object.class.getResourceAsStream(path);

		if (inputStream == null) {
			throw new IOException("failed to open the file: " + path);
		}

		Properties properties = new Properties();

		try {
			properties.load(inputStream);
		} catch (Exception e) {
			throw new IOException("failed to read the file: " + path);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return properties;
	}
}
