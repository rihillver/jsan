package com.jsan.mvc.resolve.suport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.AbstractResolver;
import com.jsan.mvc.resolve.Resolver;
import com.jsan.mvc.resolve.annotation.RegisterName;

@RegisterName(Resolver.JSON)
public class JsonResolver extends AbstractResolver {

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setContentType(response, view.getContentType(), "application/json");

		print(response, getJsonString(view.getJsonSerializeConfigurator(), view.getValue()));
	}

}
