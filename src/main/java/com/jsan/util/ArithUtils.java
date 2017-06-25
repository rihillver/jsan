package com.jsan.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 简易的算术工具类。
 * <p>
 * 更专业的数学计算可参：Apache Commons Math。
 *
 */

public class ArithUtils {

	private static final int DEFAULT_DIVIDE_SCALE = 10;

	/**
	 * 精确加法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double add(double v1, double v2) {

		return add(Double.toString(v1), Double.toString(v2));
	}

	/**
	 * 精确加法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double add(String v1, String v2) {

		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);

		return b1.add(b2).doubleValue();
	}

	/**
	 * 精确减法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double subtract(double v1, double v2) {

		return subtract(Double.toString(v1), Double.toString(v2));
	}

	/**
	 * 精确减法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double subtract(String v1, String v2) {

		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);

		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 精确乘法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double multiply(double v1, double v2) {

		return multiply(Double.toString(v1), Double.toString(v2));
	}

	/**
	 * 精确乘法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double multiply(String v1, String v2) {

		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);

		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 精确除法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double divide(double v1, double v2) {

		return divide(v1, v2, DEFAULT_DIVIDE_SCALE, RoundingMode.HALF_UP);
	}

	/**
	 * 精确除法运算，当除不尽时指定小数位数进行四舍五入。
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return
	 */
	public static double divide(double v1, double v2, int scale) {

		return divide(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 精确除法运算，当除不尽时指定小数位数和舍入方式。
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @param round
	 * @return
	 */
	public static double divide(double v1, double v2, int scale, RoundingMode round) {

		return divide(Double.toString(v1), Double.toString(v2), scale, round);
	}

	/**
	 * 精确除法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double divide(String v1, String v2) {

		return divide(v1, v2, DEFAULT_DIVIDE_SCALE, RoundingMode.HALF_UP);
	}

	/**
	 * 精确除法运算，当除不尽时指定小数位数进行四舍五入。
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return
	 */
	public static double divide(String v1, String v2, int scale) {

		return divide(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 精确除法运算，当除不尽时指定小数位数和舍入方式。
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @param round
	 * @return
	 */
	public static double divide(String v1, String v2, int scale, RoundingMode round) {

		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);

		return b1.divide(b2, scale, round).doubleValue();
	}

	/**
	 * 指定小数位并精确四舍五入。
	 * 
	 * @param v
	 * @param scale
	 * @return
	 */
	public static double round(double v, int scale) {

		return round(v, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 指定小数位和舍入方式。
	 * 
	 * @param v
	 * @param scale
	 * @param round
	 * @return
	 */
	public static double round(double v, int scale, RoundingMode round) {

		return round(Double.toString(v), scale, round);
	}

	/**
	 * 指定小数位并精确四舍五入。
	 * 
	 * @param v
	 * @param scale
	 * @return
	 */
	public static double round(String v, int scale) {

		return round(v, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 指定小数位和舍入方式。
	 * 
	 * @param v
	 * @param scale
	 * @param round
	 * @return
	 */
	public static double round(String v, int scale, RoundingMode round) {

		BigDecimal b = new BigDecimal(v);

		return b.setScale(scale, round).doubleValue();
	}

}