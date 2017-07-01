package com.jsan.mvc.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public abstract class AbstractJsonSerializeConfigurator implements JsonSerializeConfigurator {

	protected static SerializerFeature[] emptySerializerFeatures = new SerializerFeature[0];

	protected SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();
	protected SerializeFilter[] serializeFilters;
	protected String dateFormat;
	protected int defaultFeatures = JSON.DEFAULT_GENERATE_FEATURE;
	protected SerializerFeature[] serializerFeatures = emptySerializerFeatures; // 由于fastjson在SerializeWriter编码上的疏忽（该备注时版本1.2.33），没有对SerializerFeature为null的情况进行判断

	@Override
	public SerializeConfig getSerializeConfig() {
		return serializeConfig;
	}

	public void setSerializeConfig(SerializeConfig serializeConfig) {
		this.serializeConfig = serializeConfig;
	}

	@Override
	public SerializeFilter[] getSerializeFilters() {
		return serializeFilters;
	}

	public void setSerializeFilters(SerializeFilter... serializeFilters) {
		this.serializeFilters = serializeFilters;
	}

	@Override
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public int getDefaultFeatures() {
		return defaultFeatures;
	}

	public void setDefaultFeatures(int defaultFeatures) {
		this.defaultFeatures = defaultFeatures;
	}

	@Override
	public SerializerFeature[] getSerializerFeatures() {
		return serializerFeatures;
	}

	public void setSerializerFeatures(SerializerFeature... serializerFeatures) {
		this.serializerFeatures = serializerFeatures;
	}

}
