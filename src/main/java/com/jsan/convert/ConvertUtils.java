package com.jsan.convert;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

import com.jsan.convert.cache.DateFormatCache;
import com.jsan.convert.cache.NumberFormatCache;
import com.jsan.convert.support.BigDecimalConverter;
import com.jsan.convert.support.BigIntegerConverter;
import com.jsan.convert.support.BooleanConverter;
import com.jsan.convert.support.ByteConverter;
import com.jsan.convert.support.CharacterConverter;
import com.jsan.convert.support.DateConverter;
import com.jsan.convert.support.DoubleConverter;
import com.jsan.convert.support.FloatConverter;
import com.jsan.convert.support.IntegerConverter;
import com.jsan.convert.support.LongConverter;
import com.jsan.convert.support.ShortConverter;

/**
 * 转换工具类。
 *
 */

public class ConvertUtils {

	protected static final GeneralConvertService generalConvertService = new GeneralConvertService();
	protected static final JsonConvertService jsonConvertService = new JsonConvertService();
	protected static final SplitConvertService splitConvertService = new SplitConvertService();
	protected static final SplitTrimConvertService splitTrimConvertService = new SplitTrimConvertService();

	protected static final IntegerConverter integerConverter = new IntegerConverter();
	protected static final LongConverter longConverter = new LongConverter();
	protected static final FloatConverter floatConverter = new FloatConverter();
	protected static final DoubleConverter doubleConverter = new DoubleConverter();
	protected static final ShortConverter shortConverter = new ShortConverter();
	protected static final CharacterConverter characterConverter = new CharacterConverter();
	protected static final ByteConverter byteConverter = new ByteConverter();
	protected static final BooleanConverter booleanConverter = new BooleanConverter();

	protected static final BigDecimalConverter bigDecimalConverter = new BigDecimalConverter();
	protected static final BigIntegerConverter bigIntegerConverter = new BigIntegerConverter();
	protected static final DateConverter dateConverter = new DateConverter();

	/**
	 * 返回 GeneralConvertService 实例。
	 * 
	 * @return
	 */
	public static ConvertService getGeneralConvertService() {

		return generalConvertService;
	}

	/**
	 * 返回 JsonConvertService 实例。
	 * 
	 * @return
	 */
	public static ConvertService getJsonConvertService() {

		return jsonConvertService;
	}

	/**
	 * 返回 SplitConvertService 实例。
	 * 
	 * @return
	 */
	public static ConvertService getSplitConvertService() {

		return splitConvertService;
	}

	/**
	 * 返回 SplitTrimConvertService 实例。
	 * 
	 * @return
	 */
	public static ConvertService getSplitTrimConvertService() {

		return splitTrimConvertService;
	}

	// ==================================================
	
	/**
	 * 返回精确转换的Float（指定小数位和舍入方式）。
	 * 
	 * @param f
	 * @param scale
	 * @param round
	 * @return
	 */
	protected static Float getFloatRoundConvert(Float f, int scale, RoundingMode round) {

		if (f != null) {
			BigDecimal b = new BigDecimal(f);
			f = b.setScale(scale, round).floatValue();
		}

		return f;
	}

	/**
	 * 返回精确转换的Float（指定小数位和舍入方式）。
	 * 
	 * @param d
	 * @param scale
	 * @param round
	 * @return
	 */
	protected static Double getDoubleRoundConvert(Double d, int scale, RoundingMode round) {

		if (d != null) {
			BigDecimal b = new BigDecimal(d);
			d = b.setScale(scale, round).doubleValue();
		}

		return d;
	}

	/**
	 * 返回转换值（int）。
	 * 
	 * @param obj
	 * @return
	 */
	public static int toInt(Object obj) {

		return (int) integerConverter.convert(obj, int.class);
	}

	/**
	 * 返回转换值（long）。
	 * 
	 * @param obj
	 * @return
	 */
	public static long toLong(Object obj) {

		return (long) longConverter.convert(obj, long.class);
	}

	/**
	 * 返回转换值（float）。
	 * 
	 * @param obj
	 * @return
	 */
	public static float toFloat(Object obj) {

		return (float) floatConverter.convert(obj, float.class);
	}
	
	/**
	 * 返回转换值（float），指定小数位，按 RoundingMode.HALF_UP 四舍五入。
	 * 
	 * @param obj
	 * @param scale
	 * @return
	 */
	public static float toFloatRound(Object obj, int scale) {

		return getFloatRoundConvert(toFloat(obj), scale, RoundingMode.HALF_UP);
	}
	
	/**
	 * 返回转换值（float），指定小数位和舍入方式。
	 * 
	 * @param obj
	 * @param scale
	 * @param round
	 * @return
	 */
	public static float toFloatRound(Object obj, int scale, RoundingMode round) {
		
		return getFloatRoundConvert(toFloat(obj), scale, round);
	}

	/**
	 * 返回转换值（double）。
	 * 
	 * @param obj
	 * @return
	 */
	public static double toDouble(Object obj) {

		return (double) doubleConverter.convert(obj, double.class);
	}
	
	/**
	 * 返回转换值（double），指定小数位，按 RoundingMode.HALF_UP 四舍五入。
	 * 
	 * @param obj
	 * @param scale
	 * @return
	 */
	public static double toDoubleRound(Object obj, int scale) {

		return getDoubleRoundConvert(toDouble(obj), scale, RoundingMode.HALF_UP);
	}

	/**
	 * 返回转换值（double），指定小数位和舍入方式。
	 * 
	 * @param obj
	 * @param scale
	 * @param round
	 * @return
	 */
	public static double toDoubleRound(Object obj, int scale, RoundingMode round) {

		return getDoubleRoundConvert(toDouble(obj), scale, round);
	}

	/**
	 * 返回转换值（short）。
	 * 
	 * @param obj
	 * @return
	 */
	public static short toShort(Object obj) {

		return (short) shortConverter.convert(obj, short.class);
	}

	/**
	 * 返回转换值（char）。
	 * 
	 * @param obj
	 * @return
	 */
	public static char toChar(Object obj) {

		return (char) characterConverter.convert(obj, char.class);
	}

	/**
	 * 返回转换值（byte）。
	 * 
	 * @param obj
	 * @return
	 */
	public static byte toByte(Object obj) {

		return (byte) byteConverter.convert(obj, byte.class);
	}

	/**
	 * 返回转换值（boolean）。
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean toBoolean(Object obj) {

		return (boolean) booleanConverter.convert(obj, boolean.class);
	}

	// ==================================================

	/**
	 * 从 NumberFormatCache 中获取相应的 NumberFormat 实例。
	 * 
	 * @param pattern
	 * @return
	 */
	protected static NumberFormat getNumberFormat(String pattern) {

		return getNumberFormat(pattern, null);
	}

	/**
	 * 从 NumberFormatCache 中获取相应的 NumberFormat 实例。
	 * 
	 * @param pattern
	 * @param roundingMode
	 * @return
	 */
	protected static NumberFormat getNumberFormat(String pattern, RoundingMode roundingMode) {

		NumberFormat numberFormat;

		if (roundingMode == null) {
			numberFormat = NumberFormatCache.getNumberFormat(pattern); // 线程多实例匹配缓存
		} else {
			numberFormat = NumberFormatCache.getNumberFormat(); // 线程单实例缓存
			if (numberFormat instanceof DecimalFormat) {
				((DecimalFormat) numberFormat).applyPattern(pattern);
			}
			numberFormat.setRoundingMode(roundingMode);
		}

		return numberFormat;
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（int）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String toIntFormat(Object obj, String pattern) {

		return getNumberFormat(pattern).format(toInt(obj));
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（long）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String toLongFormat(Object obj, String pattern) {

		return getNumberFormat(pattern).format(toLong(obj));
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（float）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String toFloatFormat(Object obj, String pattern) {

		return getNumberFormat(pattern).format(toFloat(obj));
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（float）后格式化输出（指定舍入模式）。
	 * 
	 * @param obj
	 * @param pattern
	 * @param roundingMode
	 * @return
	 */
	public static String toFloatFormat(Object obj, String pattern, RoundingMode roundingMode) {

		return getNumberFormat(pattern, roundingMode).format(toFloat(obj));
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（double）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String toDoubleFormat(Object obj, String pattern) {

		return getNumberFormat(pattern).format(toDouble(obj));
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（double）后格式化输出（指定舍入模式）。
	 * 
	 * @param obj
	 * @param pattern
	 * @param roundingMode
	 * @return
	 */
	public static String toDoubleFormat(Object obj, String pattern, RoundingMode roundingMode) {

		return getNumberFormat(pattern, roundingMode).format(toDouble(obj));
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（short）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String toShortFormat(Object obj, String pattern) {

		return getNumberFormat(pattern).format(toShort(obj));
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（byte）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String toByteFormat(Object obj, String pattern) {

		return getNumberFormat(pattern).format(toByte(obj));
	}

	// ==================================================

	/**
	 * 返回转换值（Integer）。
	 * 
	 * @param obj
	 * @return
	 */
	public static Integer getInteger(Object obj) {

		return (Integer) integerConverter.convert(obj, Integer.class);
	}

	/**
	 * 返回转换值（Integer），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static Integer getInteger(Object obj, int def) {

		Integer integer = getInteger(obj);
		return integer == null ? def : integer;
	}

	/**
	 * 返回转换值（Long）。
	 * 
	 * @param obj
	 * @return
	 */
	public static Long getLong(Object obj) {

		return (Long) longConverter.convert(obj, Long.class);
	}

	/**
	 * 返回转换值（Long），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static Long getLong(Object obj, long def) {

		Long long1 = getLong(obj);
		return long1 == null ? def : long1;
	}

	/**
	 * 返回转换值（Float）。
	 * 
	 * @param obj
	 * @return
	 */
	public static Float getFloat(Object obj) {

		return (Float) floatConverter.convert(obj, Float.class);
	}

	/**
	 * 返回转换值（Float），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static Float getFloat(Object obj, float def) {

		Float f = getFloat(obj);
		return f == null ? def : f;
	}

	/**
	 * 返回转换值（Float），指定小数位，按 RoundingMode.HALF_UP 四舍五入。
	 * 
	 * @param obj
	 * @param scale
	 * @return
	 */
	public static Float getFloatRound(Object obj, int scale) {

		return getFloatRoundConvert(getFloat(obj), scale, RoundingMode.HALF_UP);
	}

	/**
	 * 返回转换值（Float），指定小数位，按 RoundingMode.HALF_UP 四舍五入，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param scale
	 * @param def
	 * @return
	 */
	public static Float getFloatRound(Object obj, int scale, float def) {

		Float f = getFloatRound(obj, scale);
		return f == null ? def : f;
	}

	/**
	 * 返回转换值（Float），指定小数位和舍入方式。
	 * 
	 * @param obj
	 * @param scale
	 * @param round
	 * @return
	 */
	public static Float getFloatRound(Object obj, int scale, RoundingMode round) {

		return getFloatRoundConvert(getFloat(obj), scale, round);
	}

	/**
	 * 返回转换值（Float），指定小数位和舍入方式，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param scale
	 * @param round
	 * @param def
	 * @return
	 */
	public static Float getFloatRound(Object obj, int scale, RoundingMode round, float def) {

		Float f = getFloatRound(obj, scale, round);
		return f == null ? def : f;
	}

	/**
	 * 返回转换值（Double）。
	 * 
	 * @param obj
	 * @return
	 */
	public static Double getDouble(Object obj) {

		return (Double) doubleConverter.convert(obj, Double.class);
	}

	/**
	 * 返回转换值（Double），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static Double getDouble(Object obj, double def) {

		Double double1 = getDouble(obj);
		return double1 == null ? def : double1;
	}

	/**
	 * 返回转换值（Double），指定小数位，按 RoundingMode.HALF_UP 四舍五入。
	 * 
	 * @param obj
	 * @param scale
	 * @return
	 */
	public static Double getDoubleRound(Object obj, int scale) {

		return getDoubleRoundConvert(getDouble(obj), scale, RoundingMode.HALF_UP);
	}

	/**
	 * 返回转换值（Double），指定小数位，按 RoundingMode.HALF_UP 四舍五入，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param scale
	 * @param def
	 * @return
	 */
	public static Double getDoubleRound(Object obj, int scale, double def) {

		Double f = getDoubleRound(obj, scale);
		return f == null ? def : f;
	}

	/**
	 * 返回转换值（Double），指定小数位和舍入方式。
	 * 
	 * @param obj
	 * @param scale
	 * @param round
	 * @return
	 */
	public static Double getDoubleRound(Object obj, int scale, RoundingMode round) {

		return getDoubleRoundConvert(getDouble(obj), scale, round);
	}

	/**
	 * 返回转换值（Double），指定小数位和舍入方式，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param scale
	 * @param round
	 * @param def
	 * @return
	 */
	public static Double getDoubleRound(Object obj, int scale, RoundingMode round, double def) {

		Double f = getDoubleRound(obj, scale, round);
		return f == null ? def : f;
	}

	/**
	 * 返回转换值（Short）。
	 * 
	 * @param obj
	 * @return
	 */
	public static Short getShort(Object obj) {

		return (Short) shortConverter.convert(obj, Short.class);
	}

	/**
	 * 返回转换值（Short），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static Short getShort(Object obj, short def) {

		Short short1 = getShort(obj);
		return short1 == null ? def : short1;
	}

	/**
	 * 返回转换值（Character）值。
	 * 
	 * @param obj
	 * @return
	 */
	public static Character getCharacter(Object obj) {

		return (Character) characterConverter.convert(obj, Character.class);
	}

	/**
	 * 返回转换值（Character），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static Character getCharacter(Object obj, char def) {

		Character character = getCharacter(obj);
		return character == null ? def : character;
	}

	/**
	 * 返回转换值（Byte）。
	 * 
	 * @param obj
	 * @return
	 */
	public static Byte getByte(Object obj) {

		return (Byte) byteConverter.convert(obj, Byte.class);
	}

	/**
	 * 返回转换值（Byte），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static Byte getByte(Object obj, byte def) {

		Byte byte1 = getByte(obj);
		return byte1 == null ? def : byte1;
	}

	/**
	 * 返回转换值（Boolean）。
	 * 
	 * @param obj
	 * @return
	 */
	public static Boolean getBoolean(Object obj) {

		return (Boolean) booleanConverter.convert(obj, Boolean.class);
	}

	/**
	 * 返回转换值（Boolean），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static Boolean getBoolean(Object obj, boolean def) {

		Boolean boolean1 = getBoolean(obj);
		return boolean1 == null ? def : boolean1;
	}

	/**
	 * 返回转换值（BigDecimal）。
	 * 
	 * @param obj
	 * @return
	 */
	public static BigDecimal getBigDecimal(Object obj) {

		return (BigDecimal) bigDecimalConverter.convert(obj, BigDecimal.class);
	}

	/**
	 * 返回转换值（BigDecimal），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static BigDecimal getBigDecimal(Object obj, BigDecimal def) {

		BigDecimal bigDecimal = getBigDecimal(obj);
		return bigDecimal == null ? def : bigDecimal;
	}

	/**
	 * 返回转换值（BigInteger）。
	 * 
	 * @param obj
	 * @return
	 */
	public static BigInteger getBigInteger(Object obj) {

		return (BigInteger) bigIntegerConverter.convert(obj, BigInteger.class);
	}

	/**
	 * 返回转换值（BigInteger），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static BigInteger getBigInteger(Object obj, BigInteger def) {

		BigInteger bigInteger = getBigInteger(obj);
		return bigInteger == null ? def : bigInteger;
	}

	// ==================================================

	/**
	 * 返回转换值（String），将对象转换成指定类型（Integer）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String getIntegerFormat(Object obj, String pattern) {

		Integer integer = getInteger(obj);
		return integer == null ? null : getNumberFormat(pattern).format(integer);
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（Long）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String getLongFormat(Object obj, String pattern) {

		Long long1 = getLong(obj);
		return long1 == null ? null : getNumberFormat(pattern).format(long1);
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（Float）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String getFloatFormat(Object obj, String pattern) {

		Float float1 = getFloat(obj);
		return float1 == null ? null : getNumberFormat(pattern).format(float1);
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（Float）后格式化输出（指定舍入模式）。
	 * 
	 * @param obj
	 * @param pattern
	 * @param roundingMode
	 * @return
	 */
	public static String getFloatFormat(Object obj, String pattern, RoundingMode roundingMode) {

		Float float1 = getFloat(obj);
		return float1 == null ? null : getNumberFormat(pattern, roundingMode).format(float1);
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（Double）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String getDoubleFormat(Object obj, String pattern) {

		Double double1 = getDouble(obj);
		return double1 == null ? null : getNumberFormat(pattern).format(double1);
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（Double）后格式化输出（指定舍入模式）。
	 * 
	 * @param obj
	 * @param pattern
	 * @param roundingMode
	 * @return
	 */
	public static String getDoubleFormat(Object obj, String pattern, RoundingMode roundingMode) {

		Double double1 = getDouble(obj);
		return double1 == null ? null : getNumberFormat(pattern, roundingMode).format(double1);
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（Short）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String getShortFormat(Object obj, String pattern) {

		Short short1 = getShort(obj);
		return short1 == null ? null : getNumberFormat(pattern).format(short1);
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（Byte）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String getByteFormat(Object obj, String pattern) {

		Byte byte1 = getByte(obj);
		return byte1 == null ? null : getNumberFormat(pattern).format(byte1);
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（BigDecimal）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String getBigDecimalFormat(Object obj, String pattern) {

		BigDecimal bigDecimal = getBigDecimal(obj);
		return bigDecimal == null ? null : getNumberFormat(pattern).format(bigDecimal);
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（BigDecimal）后格式化输出（指定舍入模式）。
	 * 
	 * @param obj
	 * @param pattern
	 * @param roundingMode
	 * @return
	 */
	public static String getBigDecimalFormat(Object obj, String pattern, RoundingMode roundingMode) {

		BigDecimal bigDecimal = getBigDecimal(obj);
		return bigDecimal == null ? null : getNumberFormat(pattern, roundingMode).format(bigDecimal);
	}

	/**
	 * 返回转换值（String），将对象转换成指定类型（BigInteger）后格式化输出。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String getBigIntegerFormat(Object obj, String pattern) {

		BigInteger bigInteger = getBigInteger(obj);
		return bigInteger == null ? null : getNumberFormat(pattern).format(bigInteger);
	}

	// ==================================================

	/**
	 * 返回转换值（String）。
	 * 
	 * @param obj
	 * @return
	 */
	public static String getString(Object obj) {

		return obj == null ? null : obj.toString();
	}

	/**
	 * 返回转换值（String），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static String getString(Object obj, String def) {

		String str = getString(obj);
		return str == null ? def : str;
	}

	/**
	 * 返回转换值（String），并截取指定的长度 。
	 * 
	 * @param obj
	 * @param length
	 * @return
	 */
	public static String getString(Object obj, int length) {

		return getString(obj, 0, length);
	}

	/**
	 * 返回转换值（String），并截取指定的索引范围长度 。
	 * 
	 * @param obj
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public static String getString(Object obj, int beginIndex, int endIndex) {

		String str = getString(obj);

		if (str != null) {
			if (str.length() > beginIndex) {
				if (str.length() > endIndex) {
					str = str.substring(beginIndex, endIndex);
				} else {
					str = str.substring(beginIndex);
				}
			}
		}

		return str;
	}

	/**
	 * 转换 html 标签。
	 * 
	 * @param str
	 * @return
	 */
	protected static String getHtmlEscape(String str) {

		if (str != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\'':
					sb.append("&#39;");
					break;
				default:
					sb.append(c);
				}
			}
			str = sb.toString();
		}

		return str;
	}

	/**
	 * 返回转换值（String），并转换 html 标签。
	 * 
	 * @param obj
	 * @return
	 */
	public static String getHtml(Object obj) {

		String str = getString(obj);
		str = getHtmlEscape(str);

		return str;
	}

	/**
	 * 返回转换值（String），并转换 html 标签，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static String getHtml(Object obj, String def) {

		String str = getHtml(obj);
		return str == null ? def : str;
	}

	/**
	 * 返回转换值（String），并转换 html 标签，并截取指定的长度 。
	 * 
	 * @param obj
	 * @param length
	 * @return
	 */
	public static String getHtml(Object obj, int length) {

		return getHtml(obj, 0, length);
	}

	/**
	 * 返回转换值（String），并转换 html 标签，并截取指定的索引范围长度 。
	 * 
	 * @param obj
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public static String getHtml(Object obj, int beginIndex, int endIndex) {

		String str = getString(obj);

		if (str != null) {
			if (str.length() > beginIndex) {
				if (str.length() > endIndex) {
					str = str.substring(beginIndex, endIndex);
				} else {
					str = str.substring(beginIndex);
				}
			}
		}
		str = getHtmlEscape(str);

		return str;
	}

	/**
	 * 返回转换值（String），并转换 html 标签，并且将 "\r\n" 或 "\n" 或 "\r" 换行符转换成 "&lt;br&gt;" 。
	 * 
	 * @param obj
	 * @return
	 */
	public static String getHtmlLine(Object obj) {

		String str = getHtml(obj);
		if (str != null) {
			str = str.replaceAll("\r\n|\n|\r", "<br>");
		}
		return str;
	}

	/**
	 * 返回转换值（String），并转换 html 标签，并且将 "\r\n" 或 "\n" 或 "\r" 换行符转换成 "&lt;br&gt;"
	 * ，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @return
	 */
	public static String getHtmlLine(Object obj, String def) {

		String str = getHtmlLine(obj);
		return str == null ? def : str;
	}

	/**
	 * 转换 unicode 。
	 * 
	 * @param str
	 * @param excludeASCII
	 * @return
	 */
	protected static String getUnicodeConvert(String str, boolean excludeASCII) {

		if (str != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (excludeASCII && c < 128) {
					sb.append(c);
				} else {
					sb.append(String.format("\\u%04x", (int) c));
				}
			}
			str = sb.toString();
		}

		return str;
	}

	/**
	 * 返回转换值（String），并转换成 unicode 。
	 * 
	 * @param obj
	 * @return
	 */
	public static String getUnicode(Object obj) {

		String str = getString(obj);
		str = getUnicodeConvert(str, false);

		return str;
	}

	/**
	 * 返回转换值（String），并转换成 unicode ，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static String getUnicode(Object obj, String def) {

		String str = getUnicode(obj);
		return str == null ? def : str;
	}

	/**
	 * 返回转换值（String），并转换成 unicode （不包括 ASCII 编码）。
	 * 
	 * @param obj
	 * @return
	 */
	public static String getUnicodeExcludeASCII(Object obj) {

		String str = getString(obj);
		str = getUnicodeConvert(str, true);

		return str;
	}

	/**
	 * 返回转换值（String），并转换成 unicode （不包括 ASCII 编码），如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static String getUnicodeExcludeASCII(Object obj, String def) {

		String str = getUnicodeExcludeASCII(obj);
		return str == null ? def : str;
	}
	
	/**
	 * 返回转换值（String），将显式的 Unicode 形式的字符串恢复成原始的字符串。
	 * 
	 * @param obj
	 * @return
	 */
	public static String getUnicodeStringRecover(Object obj) {

		String str = getString(obj);

		if (str != null && str.contains("\\u")) {
			StringBuilder sb = new StringBuilder();
			String[] hexs = str.split("\\\\u");
			for (int i = 1; i < hexs.length; i++) {
				if (hexs[i].length() > 4) {
					int c = Integer.parseInt(hexs[i].substring(0, 4), 16);
					sb.append((char) c);
					sb.append(hexs[i].substring(4));
				} else {
					int c = Integer.parseInt(hexs[i], 16);
					sb.append((char) c);
				}
			}
			str = sb.toString();
		}

		return str;
	}

	/**
	 * 返回转换值（String），将显式的 Unicode 形式的字符串恢复成原始的字符串，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static String getUnicodeStringRecover(Object obj, String def) {

		String str = getUnicodeStringRecover(obj);
		return str == null ? def : str;
	}

	/**
	 * 返回转换值（byte[]）。
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] getBytes(Object obj) {

		return getBytes(obj, null);
	}

	/**
	 * 返回转换值（byte[]），并指定字符集编码（参数 charset 可以为 null）。
	 * 
	 * @param obj
	 * @param charset
	 *            该参数为 null 时调用 getBytes()
	 * @return
	 */
	public static byte[] getBytes(Object obj, String charset) {

		byte[] bs = null;

		if (obj != null) {
			if (obj instanceof byte[]) {
				bs = (byte[]) obj;
			} else {
				String str = getString(obj);
				if (str != null) {
					try {
						if (charset == null) {
							bs = str.getBytes();
						} else {
							bs = str.getBytes(charset);
						}
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}

		return bs;
	}

	/**
	 * 返回转换值（String），并转换字符集编码（参数 inCharset 可以为 null）。
	 * 
	 * @param obj
	 * @param inCharset
	 *            该参数为 null 时调用 getBytes()
	 * @param outCharset
	 *            必须指定字符集
	 * @return
	 */
	public static String getCharsetConvert(Object obj, String inCharset, String outCharset) {

		String str = null;
		byte[] bs = getBytes(obj, inCharset);

		if (bs != null) {
			try {
				str = new String(bs, outCharset);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

		return str;
	}

	/**
	 * 返回转换值（String），将对象按指定的格式字符串格式化输出。
	 * 
	 * @param obj
	 * @param format
	 * @return
	 */
	public static String getFormat(Object obj, String format) {

		return obj == null ? null : String.format(format, obj);
	}

	/**
	 * 从 DateFormatCache 中获取相应的 NumberFormat 实例。
	 * 
	 * @param pattern
	 * @return
	 */
	protected static DateFormat getDateFormat(String pattern) {

		return DateFormatCache.getDateFormat(pattern);
	}

	/**
	 * 转换 Date 。
	 * 
	 * @param obj
	 * @return
	 */
	public static Date getDate(Object obj) {

		return (Date) dateConverter.convert(obj, Date.class);
	}

	/**
	 * 转换 Date ，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static Date getDate(Object obj, Date def) {

		Date date = getDate(obj);
		return date == null ? def : date;
	}

	/**
	 * 返回转换值（Date），按指定的时间格式转换。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static Date getDate(Object obj, String pattern) {

		Date date = getDate(obj);

		if (date == null && obj != null) {
			try {
				date = getDateFormat(pattern).parse(obj.toString());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		return date;
	}

	/**
	 * 返回转换值（Date），按指定的时间格式转换，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param pattern
	 * @param def
	 * @return
	 */
	public static Date getDate(Object obj, String pattern, Date def) {

		Date date = getDate(obj, pattern);
		return date == null ? def : date;
	}

	/**
	 * 返回转换值（String），日期格式： yyyy-MM-dd 。
	 * 
	 * @param obj
	 * @return
	 */
	public static String getDateString(Object obj) {

		return getDateString(obj, null);
	}

	/**
	 * 返回转换值（String），日期格式： yyyy-MM-dd ，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static String getDateString(Object obj, String def) {

		return getDateTimeFormat(obj, "yyyy-MM-dd", def);
	}

	/**
	 * 返回转换值（String），日期时间格式： yyyy-MM-dd HH:mm:ss 。
	 * 
	 * @param obj
	 * @return
	 */
	public static String getDateTimeString(Object obj) {

		return getDateTimeString(obj, null);
	}

	/**
	 * 返回转换值（String），日期时间格式： yyyy-MM-dd HH:mm:ss ，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static String getDateTimeString(Object obj, String def) {

		return getDateTimeFormat(obj, "yyyy-MM-dd HH:mm:ss", def);
	}

	/**
	 * 返回转换值（String），并自定义 Date 输出格式。
	 * 
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String getDateTimeFormat(Object obj, String pattern) {

		Date date = getDate(obj);
		return date == null ? null : getDateFormat(pattern).format(date);
	}

	/**
	 * 返回转换值（String），并自定义 Date 输出格式，如果值为 null ，则返回指定的值。
	 * 
	 * @param obj
	 * @param pattern
	 * @param def
	 * @return
	 */
	public static String getDateTimeFormat(Object obj, String pattern, String def) {

		String str = getDateTimeFormat(obj, pattern);
		return str == null ? def : str;
	}

}
