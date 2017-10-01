package com.jsan.convert;

import com.jsan.convert.support.ArrayDequeConverter;
import com.jsan.convert.support.ArrayListConverter;
import com.jsan.convert.support.BooleanArrayConverter;
import com.jsan.convert.support.ByteArrayConverter;
import com.jsan.convert.support.CharacterArrayConverter;
import com.jsan.convert.support.CollectionConverter;
import com.jsan.convert.support.DequeConverter;
import com.jsan.convert.support.DoubleArrayConverter;
import com.jsan.convert.support.FloatArrayConverter;
import com.jsan.convert.support.HashMapConverter;
import com.jsan.convert.support.HashSetConverter;
import com.jsan.convert.support.IntegerArrayConverter;
import com.jsan.convert.support.LinkedHashMapConverter;
import com.jsan.convert.support.LinkedHashSetConverter;
import com.jsan.convert.support.LinkedListConverter;
import com.jsan.convert.support.ListConverter;
import com.jsan.convert.support.LongArrayConverter;
import com.jsan.convert.support.MapConverter;
import com.jsan.convert.support.ObjectArrayConverter;
import com.jsan.convert.support.QueueConverter;
import com.jsan.convert.support.SetConverter;
import com.jsan.convert.support.ShortArrayConverter;

/**
 * 一般转换服务。
 *
 */

public class GeneralConvertService extends AbstractConvertService {

	@Override
	protected void defaultRegisterConverter() {

		registerConverter(new IntegerArrayConverter());
		registerConverter(new LongArrayConverter());
		registerConverter(new DoubleArrayConverter());
		registerConverter(new FloatArrayConverter());
		registerConverter(new ShortArrayConverter());
		registerConverter(new ByteArrayConverter());
		registerConverter(new BooleanArrayConverter());
		registerConverter(new CharacterArrayConverter());

		registerConverter(new ObjectArrayConverter());

		registerConverter(new CollectionConverter());
		registerConverter(new ListConverter());
		registerConverter(new SetConverter());
		registerConverter(new QueueConverter());
		registerConverter(new DequeConverter());

		registerConverter(new MapConverter());

		// ==================================================

		registerConverter(new ArrayListConverter());
		registerConverter(new LinkedListConverter());
		registerConverter(new HashSetConverter());
		registerConverter(new LinkedHashSetConverter());
		registerConverter(new ArrayDequeConverter());
		registerConverter(new HashMapConverter());
		registerConverter(new LinkedHashMapConverter());

	}

}
