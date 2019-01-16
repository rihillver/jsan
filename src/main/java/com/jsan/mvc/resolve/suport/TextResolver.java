package com.jsan.mvc.resolve.suport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.AbstractResolver;
import com.jsan.mvc.resolve.Resolver;
import com.jsan.mvc.resolve.annotation.RegisterName;

@RegisterName(Resolver.TEXT)
public class TextResolver extends AbstractResolver {

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setStatusCode(response, view.getStatusCode());
		
		setContentType(response, view.getContentType(), "text/plain");

		print(response, view.getData());
	}

}
