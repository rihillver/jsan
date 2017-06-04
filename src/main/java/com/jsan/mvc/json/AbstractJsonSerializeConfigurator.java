package com.jsan.mvc.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public abstract class AbstractJsonSerializeConfigurator implements JsonSerializeConfigurator {

	protected SerializeConfig serializeConfig = SerializeConfig.globalInstance;
	protected SerializeFilter[] serializeFilters;
	protected String dateFormat;
	protected int defaultFeatures = JSON.DEFAULT_GENERATE_FEATURE;
	protected SerializerFeature[] serializerFeatures;

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
