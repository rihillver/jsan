package com.jsan.convert;

import java.math.RoundingMode;

import org.junit.Test;

public class ConvertUtilsTest {

	@Test
	public void foo() {

		System.out.println(ConvertUtils.toIntFormat(15, "000000")); // 000015
																	// （不足六位，前面补0）
		System.out.println(ConvertUtils.toDoubleFormat(123.24, "000000.0000")); // 000123.2400

		System.out.println(ConvertUtils.getDoubleFormat(1234.125, "#,###.00")); // 1,234.12
																				// （银行家舍入法）
		System.out.println(ConvertUtils.getDoubleFormat(1234.135, "#,###.00")); // 1,234.14
																				// （银行家舍入法）

		System.out.println(ConvertUtils.getDoubleFormat(1234.125, "#,###.00", RoundingMode.HALF_UP)); // 1,234.13
																										// （四舍五入）
		System.out.println(ConvertUtils.getDoubleFormat(1234.125, "#,###.00", RoundingMode.HALF_DOWN)); // 1,234.12
																										// （五舍六入）

		System.out.println(ConvertUtils.getDoubleFormat(12, "0.####")); // 12
		System.out.println(ConvertUtils.getDoubleFormat(12.3, "0.####")); // 12.3
		System.out.println(ConvertUtils.getDoubleFormat(12.345, "0.####")); // 12.345
		System.out.println(ConvertUtils.getDoubleFormat(12.3, "0.00##")); // 12.30
		System.out.println(ConvertUtils.getDoubleFormat(12.345, "0.00##")); // 12.345
		System.out.println(ConvertUtils.getDoubleFormat(12.345, "0.0000")); // 12.3450

		System.out.println(ConvertUtils.getDoubleFormat(0.12345, "0.0000%")); // 12.3450%
																				// （百分比）
		System.out.println(ConvertUtils.getDoubleFormat(0.12345, "0.####%")); // 12.345%
																				// （百分比）
		System.out.println(ConvertUtils.getDoubleFormat(0.012345, "0.0000\u2030")); // 12.3450‰
																					// （千分比）
		System.out.println(ConvertUtils.getDoubleFormat(0.012345, "0.####\u2030")); // 12.345‰
																					// （千分比）

		System.out.println(ConvertUtils.getDoubleFormat(1234567.12, "\u00A4#,###.00")); // ￥1,234,567.12
																						// （默认语言环境下的货币符号）

		// getFormat() 详细使用可参见 String.format() 及 java.util.Formatter.format()

		System.out.println(ConvertUtils.getFormat(12.3556, "%010.2f")); // 0000012.35
																		// （总共保留10个位数，不足补0，四舍五入取2位小数位）
		System.out.println(ConvertUtils.getFormat(12.3556, "%010.0f")); // 0000000012
																		// （总共保留10个位数，不足补0，舍去小数位）
		System.out.println(ConvertUtils.getFormat(6789012.3556, "%,.0f")); // 6,789,012
																			// （加上加千分位分隔符，舍去小数位）
		System.out.println(ConvertUtils.getFormat(123556, "%012d")); // 000000123556
		System.out.println(ConvertUtils.getFormat(123556, "%,d")); // 123,556
		System.out.println(ConvertUtils.getFormat(123556, "%,09d")); // 00123,556

		// 位数不足，从后面补0（方法1）
		int len1 = 10;
		String str1 = "123";
		str1 = str1 + String.format("%1$0" + (len1 - str1.length()) + "d", 0);
		System.out.println(str1); // 1230000000

		// 位数不足，从后面补0（方法2）
		int len2 = 10;
		String str2 = "123";
		StringBuilder sb = new StringBuilder();
		sb.append(str2);
		for (int i = 0; i < (len2 - str2.length()); i++) {
			sb.append(0);
		}
		str2 = sb.toString();
		System.out.println(str2); // 1230000000

		// 整数的四舍五入（精确到万位）
		int i = 175000;
		int j = Math.round(i / 10000f);
		i = j * 10000;
		System.out.println(i); // 180000

		// 整数的舍去（万位以下为0）
		int m = 175000;
		m = m / 10000 * 10000;
		System.out.println(m); // 170000

	}

}
