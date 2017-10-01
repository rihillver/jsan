package com.jsan.mvc.adapter;

import javax.servlet.http.HttpServletRequest;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;

/**
 * 请求 URL 映射（路由）适配器。
 *
 */

public interface MappingAdapter {

	public MappingInfo getMappingInfo(MvcConfig config, HttpServletRequest request);

}
