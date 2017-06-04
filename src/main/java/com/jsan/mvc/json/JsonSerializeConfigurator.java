package com.jsan.mvc.json;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 基于 Fastjson 的 json 序列化配置器接口。
 *
 */

public interface JsonSerializeConfigurator {

	SerializeConfig getSerializeConfig();

	SerializeFilter[] getSerializeFilters();

	String getDateFormat();

	int getDefaultFeatures();

	SerializerFeature[] getSerializerFeatures();

}
