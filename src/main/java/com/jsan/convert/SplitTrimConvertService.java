package com.jsan.convert;

import com.jsan.convert.support.split.trim.ArrayDequeSplitTrimConverter;
import com.jsan.convert.support.split.trim.ArrayListSplitTrimConverter;
import com.jsan.convert.support.split.trim.BooleanArraySplitTrimConverter;
import com.jsan.convert.support.split.trim.ByteArraySplitTrimConverter;
import com.jsan.convert.support.split.trim.CharacterArraySplitTrimConverter;
import com.jsan.convert.support.split.trim.CollectionSplitTrimConverter;
import com.jsan.convert.support.split.trim.DequeSplitTrimConverter;
import com.jsan.convert.support.split.trim.DoubleArraySplitTrimConverter;
import com.jsan.convert.support.split.trim.FloatArraySplitTrimConverter;
import com.jsan.convert.support.split.trim.HashMapSplitTrimConverter;
import com.jsan.convert.support.split.trim.HashSetSplitTrimConverter;
import com.jsan.convert.support.split.trim.IntegerArraySplitTrimConverter;
import com.jsan.convert.support.split.trim.LinkedHashMapSplitTrimConverter;
import com.jsan.convert.support.split.trim.LinkedHashSetSplitTrimConverter;
import com.jsan.convert.support.split.trim.LinkedListSplitTrimConverter;
import com.jsan.convert.support.split.trim.ListSplitTrimConverter;
import com.jsan.convert.support.split.trim.LongArraySplitTrimConverter;
import com.jsan.convert.support.split.trim.MapSplitTrimConverter;
import com.jsan.convert.support.split.trim.ObjectArraySplitTrimConverter;
import com.jsan.convert.support.split.trim.QueueSplitTrimConverter;
import com.jsan.convert.support.split.trim.SetSplitTrimConverter;
import com.jsan.convert.support.split.trim.ShortArraySplitTrimConverter;

/**
 * 转换服务。
 * <p>
 * 对于数组、集合类型使用逗号(,)分割，对于 Map 类型使用逗号(,)和冒号(:)分割，并去除首尾空白。
 *
 */

public class SplitTrimConvertService extends AbstractConvertService {

	@Override
	protected void defaultRegisterConverter() {

		registerConverter(new IntegerArraySplitTrimConverter());
		registerConverter(new LongArraySplitTrimConverter());
		registerConverter(new DoubleArraySplitTrimConverter());
		registerConverter(new FloatArraySplitTrimConverter());
		registerConverter(new ShortArraySplitTrimConverter());
		registerConverter(new ByteArraySplitTrimConverter());
		registerConverter(new BooleanArraySplitTrimConverter());
		registerConverter(new CharacterArraySplitTrimConverter());

		registerConverter(new ObjectArraySplitTrimConverter());

		registerConverter(new CollectionSplitTrimConverter());
		registerConverter(new ListSplitTrimConverter());
		registerConverter(new SetSplitTrimConverter());
		registerConverter(new QueueSplitTrimConverter());
		registerConverter(new DequeSplitTrimConverter());

		registerConverter(new MapSplitTrimConverter());

		// ==================================================

		registerConverter(new ArrayListSplitTrimConverter());
		registerConverter(new LinkedListSplitTrimConverter());
		registerConverter(new HashSetSplitTrimConverter());
		registerConverter(new LinkedHashSetSplitTrimConverter());
		registerConverter(new ArrayDequeSplitTrimConverter());
		registerConverter(new HashMapSplitTrimConverter());
		registerConverter(new LinkedHashMapSplitTrimConverter());
	}
}
