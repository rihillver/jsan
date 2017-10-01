package com.jsan.convert;

import java.lang.reflect.Method;

import org.junit.Test;

public class EnumConverterTest {

	public enum Color {

		RED, GREEN, BLANK, YELLOW, C
	}

	public enum Color2 {

		RED, GREEN, BLANK, YELLOW, C
	}

	public Color foo(Enum<Color> enum1) {

		return null;
	}

	@Test
	public void bar() throws NoSuchMethodException, SecurityException {

		// Enum<?> c = Color.YELLOW;

		Method method = EnumConverterTest.class.getDeclaredMethod("foo", Enum.class);

		System.out.println(method.getGenericReturnType());
		System.out.println(method.getGenericParameterTypes()[0]);

		System.out.println();

		// Converter converter = new EnumConverter();

		ConvertService convertService = new GeneralConvertService();

		// convertService.register(EnumConverter.class);

		Converter converter = convertService.lookupConverter(Color.class);

		Color enum1 = (Color) converter.convert(Color2.RED, method.getGenericParameterTypes()[0]);

		System.out.println(enum1);

		// System.out.println(Enum.valueOf(Color.class, "RED"));

	}

}
