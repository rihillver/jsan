package com.jsan.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

/**
 * 对于 Map 的转换包装类。
 *
 */

public class ConvertWrapper {

	private Map<String, Object> map;

	public ConvertWrapper(Map<String, Object> map) {

		this.map = map;
	}

	/**
	 * 返回被包装的 Map。
	 * 
	 * @return
	 */
	public Map<String, Object> getMap() {

		return map;
	}

	/**
	 * 返回被包装的 Map 是否为 null。
	 * 
	 * @return
	 */
	public boolean isMapNull() {

		return map == null;
	}

	/**
	 * 返回被包装的 Map 是否为空。
	 * 
	 * @return
	 */
	public boolean isMapEmpty() {

		return map == null || map.size() == 0;
	}

	// ==================================================

	public Object get(String obj) {

		return map.get(obj);
	}

	public Object get(String obj, Object def) {

		Object object = get(obj);
		return object == null ? def : object;
	}

	// ==================================================

	public int toInt(String obj) {

		return ConvertUtils.toInt(get(obj));
	}

	public long toLong(String obj) {

		return ConvertUtils.toLong(get(obj));
	}

	public float toFloat(String obj) {

		return ConvertUtils.toFloat(get(obj));
	}

	public double toDouble(String obj) {

		return ConvertUtils.toDouble(get(obj));
	}

	public short toShort(String obj) {

		return ConvertUtils.toShort(get(obj));
	}

	public char toChar(String obj) {

		return ConvertUtils.toChar(get(obj));
	}

	public byte toByte(String obj) {

		return ConvertUtils.toByte(get(obj));
	}

	public boolean toBoolean(String obj) {

		return ConvertUtils.toBoolean(get(obj));
	}

	// ==================================================

	public String toIntFormat(String obj, String pattern) {

		return ConvertUtils.toIntFormat(get(obj), pattern);
	}

	public String toLongFormat(String obj, String pattern) {

		return ConvertUtils.toLongFormat(get(obj), pattern);
	}

	public String toFloatFormat(String obj, String pattern) {

		return ConvertUtils.toFloatFormat(get(obj), pattern);
	}

	public String toFloatFormat(String obj, String pattern, RoundingMode roundingMode) {

		return ConvertUtils.toFloatFormat(get(obj), pattern, roundingMode);
	}

	public String toDoubleFormat(String obj, String pattern) {

		return ConvertUtils.toDoubleFormat(get(obj), pattern);
	}

	public String toDoubleFormat(String obj, String pattern, RoundingMode roundingMode) {

		return ConvertUtils.toDoubleFormat(get(obj), pattern, roundingMode);
	}

	public String toShortFormat(String obj, String pattern) {

		return ConvertUtils.toShortFormat(get(obj), pattern);
	}

	public String toByteFormat(String obj, String pattern) {

		return ConvertUtils.toByteFormat(get(obj), pattern);
	}

	// ==================================================

	public Integer getInteger(String obj) {

		return ConvertUtils.getInteger(get(obj));
	}

	public Integer getInteger(String obj, int def) {

		return ConvertUtils.getInteger(get(obj), def);
	}

	public Long getLong(String obj) {

		return ConvertUtils.getLong(get(obj));
	}

	public Long getLong(String obj, long def) {

		return ConvertUtils.getLong(get(obj), def);
	}

	public Float getFloat(String obj) {

		return ConvertUtils.getFloat(get(obj));
	}

	public Float getFloat(String obj, float def) {

		return ConvertUtils.getFloat(get(obj), def);
	}

	public Double getDouble(String obj) {

		return ConvertUtils.getDouble(get(obj));
	}

	public Double getDouble(String obj, double def) {

		return ConvertUtils.getDouble(get(obj), def);
	}

	public Short getShort(String obj) {

		return ConvertUtils.getShort(get(obj));
	}

	public Short getShort(String obj, short def) {

		return ConvertUtils.getShort(get(obj), def);
	}

	public Character getCharacter(String obj) {

		return ConvertUtils.getCharacter(get(obj));
	}

	public Character getCharacter(String obj, char def) {

		return ConvertUtils.getCharacter(get(obj), def);
	}

	public Byte getByte(String obj) {

		return ConvertUtils.getByte(get(obj));
	}

	public Byte getByte(String obj, byte def) {

		return ConvertUtils.getByte(get(obj), def);
	}

	public Boolean getBoolean(String obj) {

		return ConvertUtils.getBoolean(get(obj));
	}

	public Boolean getBoolean(String obj, boolean def) {

		return ConvertUtils.getBoolean(get(obj), def);
	}

	public BigDecimal getBigDecimal(String obj) {

		return ConvertUtils.getBigDecimal(get(obj));
	}

	public BigDecimal getBigDecimal(String obj, BigDecimal def) {

		return ConvertUtils.getBigDecimal(get(obj), def);
	}

	public BigInteger getBigInteger(String obj) {

		return ConvertUtils.getBigInteger(get(obj));
	}

	public BigInteger getBigInteger(String obj, BigInteger def) {

		return ConvertUtils.getBigInteger(get(obj), def);
	}

	// ==================================================

	public String getIntegerFormat(String obj, String pattern) {

		return ConvertUtils.getIntegerFormat(get(obj), pattern);
	}

	public String getLongFormat(String obj, String pattern) {

		return ConvertUtils.getLongFormat(get(obj), pattern);
	}

	public String getFloatFormat(String obj, String pattern) {

		return ConvertUtils.getFloatFormat(get(obj), pattern);
	}

	public String getFloatFormat(String obj, String pattern, RoundingMode roundingMode) {

		return ConvertUtils.getFloatFormat(get(obj), pattern, roundingMode);
	}

	public String getDoubleFormat(String obj, String pattern) {

		return ConvertUtils.getDoubleFormat(get(obj), pattern);
	}

	public String getDoubleFormat(String obj, String pattern, RoundingMode roundingMode) {

		return ConvertUtils.getDoubleFormat(get(obj), pattern, roundingMode);
	}

	public String getShortFormat(String obj, String pattern) {

		return ConvertUtils.getShortFormat(get(obj), pattern);
	}

	public String getByteFormat(String obj, String pattern) {

		return ConvertUtils.getByteFormat(get(obj), pattern);
	}

	public String getBigDecimalFormat(String obj, String pattern) {

		return ConvertUtils.getBigDecimalFormat(get(obj), pattern);
	}

	public String getBigDecimalFormat(String obj, String pattern, RoundingMode roundingMode) {

		return ConvertUtils.getBigDecimalFormat(get(obj), pattern, roundingMode);
	}

	public String getBigIntegerFormat(String obj, String pattern) {

		return ConvertUtils.getBigIntegerFormat(get(obj), pattern);
	}

	// ==================================================

	public String getString(String obj) {

		return ConvertUtils.getString(get(obj));
	}

	public String getString(String obj, String def) {

		return ConvertUtils.getString(get(obj), def);
	}

	public String getString(String obj, int length) {

		return ConvertUtils.getString(get(obj), length);
	}

	public String getString(String obj, int beginIndex, int endIndex) {

		return ConvertUtils.getString(get(obj), beginIndex, endIndex);
	}

	public String getHtml(String obj) {

		return ConvertUtils.getHtml(get(obj));
	}

	public String getHtml(String obj, String def) {

		return ConvertUtils.getHtml(get(obj), def);
	}

	public String getHtml(String obj, int length) {

		return ConvertUtils.getHtml(get(obj), length);
	}

	public String getHtml(String obj, int beginIndex, int endIndex) {

		return ConvertUtils.getHtml(get(obj), beginIndex, endIndex);
	}

	public String getHtmlLine(String obj) {

		return ConvertUtils.getHtmlLine(get(obj));
	}

	public String getHtmlLine(String obj, String def) {

		return ConvertUtils.getHtmlLine(get(obj), def);
	}

	public String getUnicode(String obj) {

		return ConvertUtils.getUnicode(get(obj));
	}

	public String getUnicode(String obj, String def) {

		return ConvertUtils.getUnicode(get(obj), def);
	}

	public String getUnicodeExcludeASCII(String obj) {

		return ConvertUtils.getUnicodeExcludeASCII(get(obj));
	}

	public String getUnicodeExcludeASCII(String obj, String def) {

		return ConvertUtils.getUnicodeExcludeASCII(get(obj), def);
	}

	public byte[] getBytes(String obj) {

		return ConvertUtils.getBytes(get(obj));
	}

	public byte[] getBytes(String obj, String charset) {

		return ConvertUtils.getBytes(get(obj), charset);
	}

	public String getCharsetConvert(String obj, String inCharset, String outCharset) {

		return ConvertUtils.getCharsetConvert(get(obj), inCharset, outCharset);
	}

	public String getFormat(String obj, String format) {

		return ConvertUtils.getFormat(get(obj), format);
	}

	public Date getDate(String obj) {

		return ConvertUtils.getDate(get(obj));
	}

	public Date getDate(String obj, Date def) {

		return ConvertUtils.getDate(get(obj), def);
	}

	public Date getDate(String obj, String pattern) {

		return ConvertUtils.getDate(get(obj), pattern);
	}

	public Date getDate(String obj, String pattern, Date def) {

		return ConvertUtils.getDate(get(obj), pattern, def);
	}

	public String getDateString(String obj) {

		return ConvertUtils.getDateString(get(obj));
	}

	public String getDateString(String obj, String def) {

		return ConvertUtils.getDateString(get(obj), def);
	}

	public String getDateTimeString(String obj) {

		return ConvertUtils.getDateTimeString(get(obj));
	}

	public String getDateTimeString(String obj, String def) {

		return ConvertUtils.getDateTimeString(get(obj), def);
	}

	public String getDateTimeFormat(String obj, String pattern) {

		return ConvertUtils.getDateTimeFormat(get(obj), pattern);
	}

	public String getDateTimeFormat(String obj, String pattern, String def) {

		return ConvertUtils.getDateTimeFormat(get(obj), pattern, def);
	}
}
