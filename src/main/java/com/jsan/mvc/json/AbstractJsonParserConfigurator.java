package com.jsan.mvc.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;

public abstract class AbstractJsonParserConfigurator implements JsonParserConfigurator {

	protected static Feature[] emptyFeatures = new Feature[0];

	protected ParserConfig parserConfig = ParserConfig.getGlobalInstance();
	protected ParseProcess parseProcess;
	protected int featureValues = JSON.DEFAULT_PARSER_FEATURE;
	protected Feature[] features = emptyFeatures; // 由于fastjson相关的方法没有对Feature为null的情况进行判断

	@Override
	public ParserConfig getParserConfig() {
		return parserConfig;
	}

	public void setParserConfig(ParserConfig parserConfig) {
		this.parserConfig = parserConfig;
	}

	@Override
	public ParseProcess getParseProcess() {
		return parseProcess;
	}

	public void setParseProcess(ParseProcess parseProcess) {
		this.parseProcess = parseProcess;
	}

	@Override
	public int getFeatureValues() {
		return featureValues;
	}

	public void setFeatureValues(int featureValues) {
		this.featureValues = featureValues;
	}

	@Override
	public Feature[] getFeatures() {
		return features;
	}

	public void setFeatures(Feature... features) {
		this.features = features;
	}

}
