package com.jsan.mvc.json;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;

/**
 * 基于 Fastjson 的 json 反序列化配置器接口。
 *
 */

public interface JsonParserConfigurator {

	ParserConfig getParserConfig();

	ParseProcess getParseProcess();

	int getFeatureValues();

	Feature[] getFeatures();

}
