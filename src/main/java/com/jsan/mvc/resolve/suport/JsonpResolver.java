package com.jsan.mvc.resolve.suport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.AbstractResolver;
import com.jsan.mvc.resolve.Resolver;
import com.jsan.mvc.resolve.annotation.RegisterName;

@RegisterName(Resolver.JSONP)
public class JsonpResolver extends AbstractResolver {

	protected static final String DEFAULT_JSONP_CALLBACK = "callback";

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setContentType(response, view.getContentType(), "application/json");

		String json = getJsonString(view.getJsonConfig(), view.getValue());

		String jsonpCallback = view.getJsonpCallback();

		if (jsonpCallback == null) {
			jsonpCallback = DEFAULT_JSONP_CALLBACK;
		}

		json = jsonpCallback + "(" + json + ")";

		print(response, json);
	}

}
