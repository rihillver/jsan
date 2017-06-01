package com.jsan.convert;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractRecursiveableConverter implements Converter, Recursiveable, Cloneable {

	private ConvertService convertService;

	@Override
	public Converter clone() {

		Converter converter;

		try {
			converter = (Converter) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}

		return converter;
	}

	@Override
	public void setConvertService(ConvertService convertService) {

		this.convertService = convertService;
	}

	@Override
	public ConvertService getConvertService() {

		return this.convertService;
	}

	@Override
	public Converter lookupConverter(Class<?> type) {

		if (convertService == null) {
			return null;
		} else {
			return convertService.lookupConverter(type);
		}
	}

	@Override
	public Formatter lookupFormatter(Class<?> type) {

		if (convertService == null) {
			return null;
		} else {
			return convertService.lookupFormatter(type);
		}
	}

	/**
	 * 判断是否为数组或集合，如是则取第一个元素，如果是空的数组或集合，则返回 null。
	 * 
	 * @param source
	 * @return
	 */
	protected Object getArrayOrCollectionFirstObject(Object source) {

		if (source != null) {
			if (source.getClass().isArray()) {
				if (Array.getLength(source) > 0) {
					source = Array.get(source, 0);
				} else {
					source = null;
				}
			} else if (Collection.class.isAssignableFrom(source.getClass())) {
				Collection<?> collection = (Collection<?>) source;
				Iterator<?> iterator = collection.iterator();
				if (iterator.hasNext()) {
					source = iterator.next();
				} else {
					source = null;
				}
			}
		}

		return source;
	}

	/**
	 * 格式化输出处理。
	 * 
	 * @param source
	 * @return
	 */
	protected Object printByFormatter(Object source) {

		if (source == null) {
			return source;
		}

		Formatter formatter = lookupFormatter(source.getClass());
		if (formatter != null) {
			try {
				return formatter.print(source);
			} catch (Exception e) {
				// logging...
				// e.printStackTrace();
				return null; // 无法 print 的时候返回 null
			}
		} else {
			return source;
		}
	}

	/**
	 * 格式化输入处理。
	 * 
	 * @param clazz
	 * @param source
	 * @return
	 */
	protected Object parseByFormatter(Class<?> clazz, String source) {

		if (source == null) {
			return source;
		}

		Formatter formatter = lookupFormatter(clazz);
		if (formatter != null) {
			try {
				return formatter.parse(source);
			} catch (Exception e) {
				// logging...
				// e.printStackTrace();
				return null; // 无法 parse 的时候返回 null
			}
		} else {
			return source;
		}
	}

}
