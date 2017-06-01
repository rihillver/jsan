package com.jsan.convert.support.split.trim;

import java.lang.reflect.Type;

public class CharacterArraySplitTrimConverter extends AbstractArraySplitTrimConverter {

	@Override
	public char[] convert(Object source, Type type) {

		return getArrayConvert(char[].class, source, type);
	}

}
