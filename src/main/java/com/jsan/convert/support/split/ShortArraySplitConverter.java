package com.jsan.convert.support.split;

import java.lang.reflect.Type;

public class ShortArraySplitConverter extends AbstractArraySplitConverter {

	@Override
	public short[] convert(Object source, Type type) {

		return getArrayConvert(short[].class, source, type);
	}

}
