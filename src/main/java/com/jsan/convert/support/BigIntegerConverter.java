package com.jsan.convert.support;

import java.lang.reflect.Type;
import java.math.BigInteger;

import com.jsan.convert.AbstractPrimitiveConverter;

public class BigIntegerConverter extends AbstractPrimitiveConverter {

	@Override
	public BigInteger convert(Object source, Type type) {

		return getNumberConvert(BigInteger.class, source, type);
	}

}
