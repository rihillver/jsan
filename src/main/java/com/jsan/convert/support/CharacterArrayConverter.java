package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractArrayConverter;

public class CharacterArrayConverter extends AbstractArrayConverter {

	@Override
	public char[] convert(Object source, Type type) {

		return getArrayConvert(char[].class, source, type);
	}

}
