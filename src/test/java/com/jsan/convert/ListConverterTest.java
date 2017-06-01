package com.jsan.convert;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ListConverterTest {

	public void foo(List<List<Double>> list) {

	}

	public <T> void bar(Map<?, List<? extends Number>[]> map) {

	}

	@Test
	@SuppressWarnings("unchecked")
	public void test() throws SecurityException, NoSuchMethodException {

		Method method = ListConverterTest.class.getDeclaredMethod("foo", List.class);

		Type type = method.getGenericParameterTypes()[0];

		System.out.println("foo() - parameter type : " + type);

		System.out.println("bar() - parameter type : "
				+ ListConverterTest.class.getDeclaredMethod("bar", Map.class).getGenericParameterTypes()[0]);

		ConvertService convertService = new GeneralConvertService();
		// convertService.registerConverter(ListConverter.class);
		Converter converter = convertService.lookupConverter(List.class);

		Set<Double[]> set = new LinkedHashSet<Double[]>();
		set.add(new Double[] { 123.01, 123.02, 123.03 });
		set.add(new Double[] { 111.01, 111.02, 111.03 });
		set.add(new Double[] { 222.01, 222.02, 222.03 });
		set.add(new Double[] { 333.01, 333.02, 333.03 });

		String[][] strs = { { "125.15", "125.2", "a", "324" }, { "324.55", "3265.414", "632.25" },
				{ "234", "234.696", "234", "3546", "5656", "2343" } };

		List<List<Double>> list = (List<List<Double>>) converter.convert(strs, type);

		for (List<Double> str : list) {
			System.out.println(str);
		}

	}

}
