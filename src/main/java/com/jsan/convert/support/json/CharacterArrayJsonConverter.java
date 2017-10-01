package com.jsan.convert.support.json;

import java.lang.reflect.Type;

public class CharacterArrayJsonConverter extends AbstractArrayJsonConverter {

	@Override
	public char[] convert(Object source, Type type) {

		return getArrayConvert(char[].class, source, type);
	}

}
