package com.jsan.convert;

import com.jsan.convert.support.json.ArrayDequeJsonConverter;
import com.jsan.convert.support.json.ArrayListJsonConverter;
import com.jsan.convert.support.json.BooleanArrayJsonConverter;
import com.jsan.convert.support.json.ByteArrayJsonConverter;
import com.jsan.convert.support.json.CharacterArrayJsonConverter;
import com.jsan.convert.support.json.CollectionJsonConverter;
import com.jsan.convert.support.json.DequeJsonConverter;
import com.jsan.convert.support.json.DoubleArrayJsonConverter;
import com.jsan.convert.support.json.FloatArrayJsonConverter;
import com.jsan.convert.support.json.HashMapJsonConverter;
import com.jsan.convert.support.json.HashSetJsonConverter;
import com.jsan.convert.support.json.IntegerArrayJsonConverter;
import com.jsan.convert.support.json.LinkedHashMapJsonConverter;
import com.jsan.convert.support.json.LinkedHashSetJsonConverter;
import com.jsan.convert.support.json.LinkedListJsonConverter;
import com.jsan.convert.support.json.ListJsonConverter;
import com.jsan.convert.support.json.LongArrayJsonConverter;
import com.jsan.convert.support.json.MapJsonConverter;
import com.jsan.convert.support.json.ObjectArrayJsonConverter;
import com.jsan.convert.support.json.QueueJsonConverter;
import com.jsan.convert.support.json.SetJsonConverter;
import com.jsan.convert.support.json.ShortArrayJsonConverter;

/**
 * 转换服务。
 * <p>
 * 基于 Gson 对 json 进行转换。
 *
 */

public class JsonConvertService extends AbstractConvertService {

	@Override
	protected void defaultRegisterConverter() {

		registerConverter(new IntegerArrayJsonConverter());
		registerConverter(new LongArrayJsonConverter());
		registerConverter(new DoubleArrayJsonConverter());
		registerConverter(new FloatArrayJsonConverter());
		registerConverter(new ShortArrayJsonConverter());
		registerConverter(new ByteArrayJsonConverter());
		registerConverter(new BooleanArrayJsonConverter());
		registerConverter(new CharacterArrayJsonConverter());

		registerConverter(new ObjectArrayJsonConverter());

		registerConverter(new CollectionJsonConverter());
		registerConverter(new ListJsonConverter());
		registerConverter(new SetJsonConverter());
		registerConverter(new QueueJsonConverter());
		registerConverter(new DequeJsonConverter());

		registerConverter(new MapJsonConverter());

		// ==================================================

		registerConverter(new ArrayListJsonConverter());
		registerConverter(new LinkedListJsonConverter());
		registerConverter(new HashSetJsonConverter());
		registerConverter(new LinkedHashSetJsonConverter());
		registerConverter(new ArrayDequeJsonConverter());
		registerConverter(new HashMapJsonConverter());
		registerConverter(new LinkedHashMapJsonConverter());
	}

}
