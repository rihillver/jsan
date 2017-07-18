package com.jsan.convert;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractPrimitiveConverter extends AbstractRecursiveableConverter {

	private static final Set<String> trueValues = new HashSet<String>(5);
	private static final Set<String> falseValues = new HashSet<String>(5);

	static {
		trueValues.add("true");
		trueValues.add("on");
		trueValues.add("yes");
		trueValues.add("y");
		trueValues.add("1");

		falseValues.add("false");
		falseValues.add("off");
		falseValues.add("no");
		falseValues.add("n");
		falseValues.add("0");
	}

	protected <T> T getNumberConvert(Class<T> clazz, Object source, Type type) {

		Object object = null;

		source = getArrayOrCollectionFirstObject(source);

		if (source != null) {
			if (source instanceof Number) {
				object = getNumberObject(clazz, (Number) source);
			} else if (source instanceof Character) {
				object = getNumberObject(clazz, (int) (Character) source);
			} else if (source instanceof String) {

				object = parseByFormatter((Class<?>) type, (String) source); // 格式化处理

				if (!(object instanceof Number)) {
					try {
						object = new BigDecimal((String) source);
					} catch (Exception e) {
						// 字符串不能正确转换成 BigDecimal
						logger.warn("Unable to convert to BigDecimal: {}", source);
						// object = getHexNumberHandle((String) source);
						// 对于十六进制的字符串转换，暂时不做处理
						object = null;
					}
				}
				object = getNumberObject(clazz, (Number) object);
			}
		}

		object = getPrimitiveHandle(object, type);

		return clazz.cast(object);
	}

	protected Object getNumberObject(Class<?> clazz, Number number) {

		if (number == null) {
			return null;
		}

		if (clazz == Integer.class) {
			long value = number.longValue();
			if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
				// 超界
				logger.warn("Value out of range [Integer]： {}", value);
				return null;
			} else {
				return number.intValue();
			}
		} else if (clazz == Long.class) {
			BigInteger bigInteger = null;
			if (number instanceof BigInteger) {
				bigInteger = (BigInteger) number;
			} else if (number instanceof BigDecimal) {
				bigInteger = ((BigDecimal) number).toBigInteger();
			}
			if (bigInteger != null && (bigInteger.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0
					|| bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)) {
				// 超界
				logger.warn("Value out of range [Long]： {}", number);
				return null;
			} else {
				return number.longValue();
			}
		} else if (clazz == Short.class) {
			long value = number.longValue();
			if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
				// 超界
				logger.warn("Value out of range [Short]： {}", value);
				return null;
			} else {
				return number.shortValue();
			}
		} else if (clazz == Byte.class) {
			long value = number.longValue();
			if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
				// 超界
				logger.warn("Value out of range [Byte]： {}", value);
				return null;
			} else {
				return number.byteValue();
			}
		} else if (clazz == Double.class) {
			return number.doubleValue();
		} else if (clazz == Float.class) {
			return number.floatValue();
		} else if (clazz == Number.class) {
			return number;
		} else if (clazz == BigDecimal.class) {
			if (number instanceof BigDecimal) {
				return (BigDecimal) number;
			} else {
				return new BigDecimal(number.toString());
			}
		} else if (clazz == BigInteger.class) {
			if (number instanceof BigInteger) {
				return (BigInteger) number;
			} else if (number instanceof BigDecimal) {
				return ((BigDecimal) number).toBigInteger();
			} else {
				// 这样转换的方式兼容性比较好，可以将浮点数正常转换，反正大于 BigDecimal 的数值常规应用几乎不会涉及
				return new BigDecimal(number.toString()).toBigInteger();
			}
		} else {
			return number;
		}
	}

	/**
	 * 判断是否为基本数据类型，如果是则加上默认值，如果不是则允许为 null。
	 * 
	 * @param object
	 * @param type
	 * @return
	 */
	protected Object getPrimitiveHandle(Object object, Type type) {

		if (object == null && ((Class<?>) type).isPrimitive()) {
			object = getPrimitiveDefault(type);
		}

		return object;
	}

	/**
	 * 返回基本数据类型的默认值。
	 * 
	 * @param type
	 * @return
	 */
	protected Object getPrimitiveDefault(Type type) {

		if (type == Integer.TYPE) {
			return Integer.valueOf(0);
		} else if (type == Long.TYPE) {
			return Long.valueOf(0L);
		} else if (type == Double.TYPE) {
			return Double.valueOf(0d);
		} else if (type == Float.TYPE) {
			return Float.valueOf(0f);
		} else if (type == Short.TYPE) {
			return Short.valueOf((short) 0);
		} else if (type == Byte.TYPE) {
			return Byte.valueOf((byte) 0);
		} else if (type == Boolean.TYPE) {
			return Boolean.FALSE;
		} else if (type == Character.TYPE) {
			return Character.valueOf((char) 0);
		} else {
			return null;
		}
	}

	protected Character getCharacterConvert(Object source, Type type) {

		Character object = null;

		source = getArrayOrCollectionFirstObject(source);

		if (source instanceof Character) {
			object = (Character) source;
		} else if (source instanceof Number) {
			long value = ((Number) source).longValue();
			if (value < Character.MIN_VALUE || value > Character.MAX_VALUE) {
				// 超界
				logger.warn("Value out of range [Character]： {}", value);
				object = null;
			} else {
				object = (char) value;
			}
		} else if (source instanceof String) {
			String str = (String) source;
			if (str.length() == 1) { // 字符串长度为 1 的时候才转换为 Character
				object = str.charAt(0);
			}
		}

		object = (Character) getPrimitiveHandle(object, type);

		return object;
	}

	protected Boolean getBooleanConvert(Object source, Type type) {

		Boolean object = null;

		source = getArrayOrCollectionFirstObject(source);

		if (source instanceof Boolean) {
			object = (Boolean) source;
		} else if (source instanceof String || source instanceof Number || source instanceof Character) {
			object = getBooleanObject(source.toString());
		}

		object = (Boolean) getPrimitiveHandle(object, type);

		return object;
	}

	protected Boolean getBooleanObject(String str) {

		str = str.toLowerCase();

		if (trueValues.contains(str)) {
			return Boolean.TRUE;
		} else if (falseValues.contains(str)) {
			return Boolean.FALSE;
		}

		return null;
	}

	/**
	 * 判断是否为二进制字符串，并转换。
	 * 
	 * @param clazz
	 * @param source
	 * @return
	 */
	protected Number getHexNumberHandle(String source) {

		if (isHexNumber(source)) {
			return getNumberObjectByHex(source);
		} else {
			return null;
		}
	}

	/**
	 * 判断是否为十六进制形式的字符串。
	 * 
	 * @param value
	 * @return
	 */
	protected boolean isHexNumber(String value) {

		int index = (value.startsWith("-") ? 1 : 0);
		return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
	}

	/**
	 * 对于十六进制的字符串转换，暂时不做处理。
	 * 
	 * @param clazz
	 * @param source
	 * @return
	 */
	protected Number getNumberObjectByHex(String source) {

		try {
			return decodeBigInteger(source);
		} catch (Exception e) {
			// 十六进制转换异常
			logger.warn("Unable to convert to Number: {}", source);
			return null;
		}
	}

	/**
	 * 将十六进制的字符串转换成 BigInteger。
	 * 
	 * @param value
	 * @return
	 */
	protected BigInteger decodeBigInteger(String value) throws Exception {

		int radix = 10;
		int index = 0;
		boolean negative = false;

		if (value.startsWith("-")) {
			negative = true;
			index++;
		}

		if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
			index += 2;
			radix = 16;
		} else if (value.startsWith("#", index)) {
			index++;
			radix = 16;
		} else if (value.startsWith("0", index) && value.length() > 1 + index) {
			index++;
			radix = 8;
		}

		BigInteger result = new BigInteger(value.substring(index), radix);

		return (negative ? result.negate() : result);
	}

}
