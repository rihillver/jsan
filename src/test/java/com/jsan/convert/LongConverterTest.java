package com.jsan.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.jsan.util.DateUtils;

public class LongConverterTest {

	@Test
	public void testFoo() {

		ConvertService convertService = new GeneralConvertService();
		Converter converter = convertService.lookupConverter(Long.class);

		long a = (Long) converter.convert(new Date(), Long.class);

		System.out.println(a);

		long b = (Long) converter.convert(new java.sql.Date(131325456365l), Long.TYPE);

		System.out.println(b);
		
		List<Date> list = new ArrayList<Date>();
		list.add(DateUtils.getOffsetDays(1));
		list.add(DateUtils.getOffsetDays(2));
		list.add(DateUtils.getOffsetDays(3));
		
		Long[] ls = (Long[]) convertService.lookupConverter(Long[].class).convert(list, Long[].class);
		
		System.out.println(Arrays.toString(ls));
	}

}
