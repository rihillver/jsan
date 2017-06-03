package com.jsan.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 基于 Fastjson 的 json 序列化配置类。
 *
 */

public class JsonConfig {

	private SerializeConfig serializeConfig = SerializeConfig.globalInstance;
	private SerializeFilter[] serializeFilters;
	private String dateFormat;
	private int defaultFeatures = JSON.DEFAULT_GENERATE_FEATURE;
	private SerializerFeature[] serializerFeatures;

	public JsonConfig() {

	}

	public JsonConfig(String dateFormat) {

		this(dateFormat, null, null);
	}

	public JsonConfig(SerializeFilter[] serializeFilters) {

		this(null, serializeFilters, null);

	}

	public JsonConfig(SerializerFeature[] serializerFeatures) {

		this(null, null, serializerFeatures);
	}

	public JsonConfig(String dateFormat, SerializeFilter[] serializeFilters, SerializerFeature[] serializerFeatures) {

		this.dateFormat = dateFormat;
		this.serializeFilters = serializeFilters;
		this.serializerFeatures = serializerFeatures;
	}

	public SerializeConfig getSerializeConfig() {
		return serializeConfig;
	}

	public void setSerializeConfig(SerializeConfig serializeConfig) {
		this.serializeConfig = serializeConfig;
	}

	public SerializeFilter[] getSerializeFilters() {
		return serializeFilters;
	}

	public void setSerializeFilters(SerializeFilter... serializeFilters) {
		this.serializeFilters = serializeFilters;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int getDefaultFeatures() {
		return defaultFeatures;
	}

	public void setDefaultFeatures(int defaultFeatures) {
		this.defaultFeatures = defaultFeatures;
	}

	public SerializerFeature[] getSerializerFeatures() {
		return serializerFeatures;
	}

	public void setSerializerFeatures(SerializerFeature... serializerFeatures) {
		this.serializerFeatures = serializerFeatures;
	}

}
