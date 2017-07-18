package com.jsan.convert;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsan.convert.annotation.RegisterType;
import com.jsan.convert.support.BigDecimalConverter;
import com.jsan.convert.support.BigIntegerConverter;
import com.jsan.convert.support.BooleanConverter;
import com.jsan.convert.support.ByteConverter;
import com.jsan.convert.support.CalendarConverter;
import com.jsan.convert.support.CalendarFormatter;
import com.jsan.convert.support.CharacterConverter;
import com.jsan.convert.support.DateConverter;
import com.jsan.convert.support.DateFormatter;
import com.jsan.convert.support.DoubleConverter;
import com.jsan.convert.support.EnumConverter;
import com.jsan.convert.support.FloatConverter;
import com.jsan.convert.support.IntegerConverter;
import com.jsan.convert.support.LongConverter;
import com.jsan.convert.support.NumberConverter;
import com.jsan.convert.support.NumberFormatter;
import com.jsan.convert.support.ObjectConverter;
import com.jsan.convert.support.ShortConverter;
import com.jsan.convert.support.SqlDateConverter;
import com.jsan.convert.support.SqlDateFormatter;
import com.jsan.convert.support.SqlTimeConverter;
import com.jsan.convert.support.SqlTimeFormatter;
import com.jsan.convert.support.SqlTimestampConverter;
import com.jsan.convert.support.SqlTimestampFormatter;
import com.jsan.convert.support.StringConverter;

/**
 * 转换服务抽象类。
 * <p>
 * 数组类型支持多个维度的转换以及与多个嵌套集合的转换。
 *
 */

public abstract class AbstractConvertService implements ConvertService, Cloneable {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private Map<Class<?>, Converter> converterMap = new HashMap<Class<?>, Converter>();
	private Map<Class<?>, Formatter> formatterMap = new HashMap<Class<?>, Formatter>();
	private Map<Class<?>, Class<? extends Formatter>> formatterClassMap = new HashMap<Class<?>, Class<? extends Formatter>>();

	{
		// ==================== Converter ====================

		registerConverter(new IntegerConverter());
		registerConverter(new LongConverter());
		registerConverter(new DoubleConverter());
		registerConverter(new FloatConverter());
		registerConverter(new ShortConverter());
		registerConverter(new ByteConverter());
		registerConverter(new BooleanConverter());
		registerConverter(new CharacterConverter());

		registerConverter(new ObjectConverter());
		registerConverter(new StringConverter());
		registerConverter(new EnumConverter());

		registerConverter(new DateConverter());
		registerConverter(new CalendarConverter());
		registerConverter(new SqlDateConverter());
		registerConverter(new SqlTimeConverter());
		registerConverter(new SqlTimestampConverter());

		registerConverter(new NumberConverter());
		registerConverter(new BigDecimalConverter());
		registerConverter(new BigIntegerConverter());

		// ==================== Formatter ====================

		declareFormatterClass(DateFormatter.class);
		declareFormatterClass(SqlDateFormatter.class);
		declareFormatterClass(SqlTimeFormatter.class);
		declareFormatterClass(SqlTimestampFormatter.class);
		declareFormatterClass(CalendarFormatter.class);

		declareFormatterClass(NumberFormatter.class);

		// ==================================================

		defaultRegisterConverter();
	}

	protected abstract void defaultRegisterConverter();

	/**
	 * 深克隆，避免每次实例化一个 ConvertService 的时候都要执行一堆构造代码块。
	 * 
	 */
	@Override
	public ConvertService clone() {

		ConvertService service;

		try {
			service = (ConvertService) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}

		service.reinitializeConverterMap(converterMap);
		service.reinitializeFormatterClassMap(formatterClassMap);
		service.reinitializeFormatterMap(formatterMap);

		return service;
	}

	/**
	 * 需要将 Converter 对象的宿主 ConvertService 重新指向新克隆的 ConvertService 。
	 * 
	 * @param converterMap
	 */
	@Override
	public void reinitializeConverterMap(Map<Class<?>, Converter> converterMap) {

		this.converterMap = new HashMap<Class<?>, Converter>();
		for (Map.Entry<Class<?>, Converter> entry : converterMap.entrySet()) {
			Converter converter = entry.getValue().clone(); // 克隆 Converter
			setRecursive(converter); // 重新指向新宿主 ConvertService
			this.converterMap.put(entry.getKey(), converter);
		}
	}

	@Override
	public void reinitializeFormatterMap(Map<Class<?>, Formatter> formatterMap) {

		this.formatterMap = new HashMap<Class<?>, Formatter>();
		for (Map.Entry<Class<?>, Formatter> entry : formatterMap.entrySet()) {
			this.formatterMap.put(entry.getKey(), entry.getValue().clone());
		}
	}

	@Override
	public void reinitializeFormatterClassMap(Map<Class<?>, Class<? extends Formatter>> formatterClassMap) {

		this.formatterClassMap = new HashMap<Class<?>, Class<? extends Formatter>>();
		this.formatterClassMap.putAll(formatterClassMap);
	}

	/**
	 * 为 Converter 设置递归操作的寄生 ConvertService 。
	 * 
	 * @param converter
	 */
	private void setRecursive(Converter converter) {

		if (converter instanceof Recursiveable) {
			Recursiveable recursiveConverter = (Recursiveable) converter;
			recursiveConverter.setConvertService(this);
		}
	}

	/**
	 * 注册转换器时，如果转换器同时也是 Recursiveable 接口的实现类，则通过关联所属 ConvertService
	 * 的方式获取其他转换器实现同一转换服务下共享所有转换器的并实现递归转换功能。
	 * 
	 * @param converter
	 */
	@Override
	public void registerConverter(Converter converter) {

		setRecursive(converter);
		converterMap.put(getRegisterType(converter.getClass()), converter);
	}

	@Override
	public void registerConverter(Class<? extends Converter> converterClass) {

		try {
			Converter converter = BeanProxyUtils.newInstance(converterClass);
			registerConverter(converter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Converter lookupConverter(Class<?> type) {

		Converter converter = converterMap.get(type);

		if (converter == null) {
			Class<?> commonType = lookupCommonConverterType(type);
			if (commonType != null) {
				converter = converterMap.get(commonType);
			}
		}

		if (converter == null) {
			// 抛出转换器未注册异常
			String msg = "Converter not registered: " + type.getName();
			logger.error(msg);
			throw new RuntimeException(msg);
		}

		return converter;
	}

	@Override
	public void declareFormatterClass(Class<? extends Formatter> formatterClass) {

		formatterClassMap.put(getRegisterType(formatterClass), formatterClass);
	}

	@Override
	public void registerFormatter(Formatter formatter) {

		formatterMap.put(getRegisterType(formatter.getClass()), formatter);
	}

	@Override
	public void registerFormatter(Class<? extends Formatter> formatterClass) {

		try {
			Formatter formatter = BeanProxyUtils.newInstance(formatterClass);
			registerFormatter(formatter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Formatter lookupFormatter(Class<?> type) {

		Formatter formatter = formatterMap.get(type);

		if (formatter == null) {
			Class<?> commonType = lookupCommonFormatterType(type);
			if (commonType != null) {
				formatter = formatterMap.get(commonType);
			}
		}

		return formatter;
	}

	/**
	 * 查找通用的转换类型。
	 * 
	 * <ul>
	 * <li>以基本数据类型作为 convert(Object, Type) 方法的返回值不能够实现 Converter 接口中定义的
	 * convert(Object, Type) 方法，因此基本数据类型由其包装对象的类型转换器一起处理。</li>
	 * <li>基本数据类型的一维数组须逐个注册，无法由通用的对象数组类型转换器处理，因为基本数据类型的一维数组无法强制转换成对象数组。</li>
	 * <li>所有非基本数据类型的一维数组均由一个通用的对象数组类型转换器处理，二维以上的基本数据类型数组是可以视作一个对象数组类型的，可以强制转换成对象数组。</li>
	 * <li>所有枚举类型均由一个通用的枚举类型转换器处理。</li>
	 * </ul>
	 * 
	 * @param type
	 * @return
	 */
	private Class<?> lookupCommonConverterType(Class<?> type) {

		if (type.isPrimitive()) {
			if (type == Integer.TYPE) {
				return Integer.class;
			} else if (type == Long.TYPE) {
				return Long.class;
			} else if (type == Double.TYPE) {
				return Double.class;
			} else if (type == Boolean.TYPE) {
				return Boolean.class;
			} else if (type == Float.TYPE) {
				return Float.class;
			} else if (type == Character.TYPE) {
				return Character.class;
			} else if (type == Byte.TYPE) {
				return Byte.class;
			} else if (type == Short.TYPE) {
				return Short.class;
			}
		} else if (type.isArray()) {
			return Object[].class;
		} else if (type.isEnum()) {
			return Enum.class;
		}

		return null;
	}

	/**
	 * 查找通用的格式化类型。
	 * 
	 * <ul>
	 * <li>一般情况下需要进行格式化的主要有日期时间和数字，并且一般都是基于字符串方式的进行的，因此主要对这两大类作格式化处理。</li>
	 * <li>为了同时适应 Date 和 Calendar 类型的格式化，实际使用时建议一并注册这两个类型的格式化器。</li>
	 * </ul>
	 * 
	 * @param type
	 * @return
	 */
	private Class<?> lookupCommonFormatterType(Class<?> type) {

		if (type.isPrimitive() || Number.class.isAssignableFrom(type)) {
			return Number.class;
		} else if (Date.class.isAssignableFrom(type)) {
			return Date.class;
		} else if (Calendar.class.isAssignableFrom(type)) {
			return Calendar.class;
		} else {
			return null;
		}
	}

	/**
	 * 通过方法的返回类型确定转换器或格式化器的注册类型。
	 * 
	 * @param clazz
	 * @return
	 */
	private Class<?> getConvertMethodReturnType(Class<?> clazz) {

		try {
			Method method = null;
			if (Converter.class.isAssignableFrom(clazz)) {
				method = clazz.getMethod("convert", Object.class, Type.class);
			} else if (Formatter.class.isAssignableFrom(clazz)) {
				method = clazz.getMethod("parse", String.class);
			}
			method.setAccessible(true);
			return method.getReturnType();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 返回转换器或格式化器的注册类型。
	 * 
	 * @param clazz
	 * @return
	 */
	private Class<?> getRegisterType(Class<?> clazz) {

		RegisterType type = clazz.getAnnotation(RegisterType.class);
		return type == null ? getConvertMethodReturnType(clazz) : type.value();
	}

	@Override
	public Class<? extends Formatter> lookupDeclareFormatterClass(Class<?> type) {

		return formatterClassMap.get(type);
	}

	@Override
	public Map<Class<?>, Class<? extends Formatter>> getDeclareFormatterClassMap() {

		return formatterClassMap;
	}

	@Override
	public Set<Class<? extends Formatter>> getDateTimeDeclareFormatterClassSet() {

		Set<Class<? extends Formatter>> set = new HashSet<Class<? extends Formatter>>();

		for (Map.Entry<Class<?>, Class<? extends Formatter>> entry : formatterClassMap.entrySet()) {
			Class<?> clazz = entry.getKey();
			if (Date.class.isAssignableFrom(clazz) || Calendar.class.isAssignableFrom(clazz)) {
				set.add(entry.getValue());
			}
		}

		return set;
	}

	@Override
	public Set<Class<? extends Formatter>> getNumberDeclareFormatterClassSet() {

		Set<Class<? extends Formatter>> set = new HashSet<Class<? extends Formatter>>();

		for (Map.Entry<Class<?>, Class<? extends Formatter>> entry : formatterClassMap.entrySet()) {
			if (Number.class.isAssignableFrom(entry.getKey())) {
				set.add(entry.getValue());
			}
		}

		return set;
	}

}
