package com.jsan.convert;

import java.lang.reflect.Type;

/**
 * 类型转换器接口。
 * <p>
 * 由于大多数情况下 Converter 作为共享实例注册在 ConvertService 中，因此构建 Converter
 * 实现类时请确保其在单例下线程安全。
 * 
 */

public interface Converter {

	Converter clone();

	Object convert(Object source, Type type);

}
