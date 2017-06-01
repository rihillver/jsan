package com.jsan.mvc.resolve.suport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.annotation.RegisterName;

@RegisterName("jsonp")
public class JsonpResolver extends JsonResolver {

	public static final String CALLBACK = "callback";

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setContentType("application/json");

		String str = getJsonString(view);

		String callback = (String) view.getAttribute(CALLBACK);

		if (callback == null) {
			callback = CALLBACK;
		}

		str = callback + "(" + str + ")";

		output(response, str);
	}

}
