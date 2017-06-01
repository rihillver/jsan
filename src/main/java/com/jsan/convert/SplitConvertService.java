package com.jsan.convert;

import com.jsan.convert.support.split.ArrayDequeSplitConverter;
import com.jsan.convert.support.split.ArrayListSplitConverter;
import com.jsan.convert.support.split.BooleanArraySplitConverter;
import com.jsan.convert.support.split.ByteArraySplitConverter;
import com.jsan.convert.support.split.CharacterArraySplitConverter;
import com.jsan.convert.support.split.CollectionSplitConverter;
import com.jsan.convert.support.split.DequeSplitConverter;
import com.jsan.convert.support.split.DoubleArraySplitConverter;
import com.jsan.convert.support.split.FloatArraySplitConverter;
import com.jsan.convert.support.split.HashMapSplitConverter;
import com.jsan.convert.support.split.HashSetSplitConverter;
import com.jsan.convert.support.split.IntegerArraySplitConverter;
import com.jsan.convert.support.split.LinkedHashMapSplitConverter;
import com.jsan.convert.support.split.LinkedHashSetSplitConverter;
import com.jsan.convert.support.split.LinkedListSplitConverter;
import com.jsan.convert.support.split.ListSplitConverter;
import com.jsan.convert.support.split.LongArraySplitConverter;
import com.jsan.convert.support.split.MapSplitConverter;
import com.jsan.convert.support.split.ObjectArraySplitConverter;
import com.jsan.convert.support.split.QueueSplitConverter;
import com.jsan.convert.support.split.SetSplitConverter;
import com.jsan.convert.support.split.ShortArraySplitConverter;

/**
 * 转换服务。
 * <p>
 * 对于数组、集合类型使用逗号(,)分割，对于 Map 类型使用逗号(,)和冒号(:)分割。
 *
 */

public class SplitConvertService extends AbstractConvertService {

	@Override
	protected void defaultRegisterConverter() {

		registerConverter(new IntegerArraySplitConverter());
		registerConverter(new LongArraySplitConverter());
		registerConverter(new DoubleArraySplitConverter());
		registerConverter(new FloatArraySplitConverter());
		registerConverter(new ShortArraySplitConverter());
		registerConverter(new ByteArraySplitConverter());
		registerConverter(new BooleanArraySplitConverter());
		registerConverter(new CharacterArraySplitConverter());

		registerConverter(new ObjectArraySplitConverter());

		registerConverter(new CollectionSplitConverter());
		registerConverter(new ListSplitConverter());
		registerConverter(new SetSplitConverter());
		registerConverter(new QueueSplitConverter());
		registerConverter(new DequeSplitConverter());

		registerConverter(new MapSplitConverter());

		// ==================================================

		registerConverter(new ArrayListSplitConverter());
		registerConverter(new LinkedListSplitConverter());
		registerConverter(new HashSetSplitConverter());
		registerConverter(new LinkedHashSetSplitConverter());
		registerConverter(new ArrayDequeSplitConverter());
		registerConverter(new HashMapSplitConverter());
		registerConverter(new LinkedHashMapSplitConverter());
	}

}
