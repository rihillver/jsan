package com.jsan.convert.support;

import java.lang.reflect.Type;

import com.jsan.convert.AbstractPrimitiveConverter;

public class CharacterConverter extends AbstractPrimitiveConverter {

	@Override
	public Character convert(Object source, Type type) {

		return getCharacterConvert(source, type);
	}

}
