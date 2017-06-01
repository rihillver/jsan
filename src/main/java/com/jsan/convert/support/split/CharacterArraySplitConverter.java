package com.jsan.convert.support.split;

import java.lang.reflect.Type;

public class CharacterArraySplitConverter extends AbstractArraySplitConverter {

	@Override
	public char[] convert(Object source, Type type) {

		return getArrayConvert(char[].class, source, type);
	}

}
