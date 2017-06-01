package com.jsan.convert.support.split;

import java.lang.reflect.Type;

public class BooleanArraySplitConverter extends AbstractArraySplitConverter {

	@Override
	public boolean[] convert(Object source, Type type) {

		return getArrayConvert(boolean[].class, source, type);
	}

}
