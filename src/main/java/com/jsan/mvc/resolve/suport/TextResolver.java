package com.jsan.mvc.resolve.suport;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.Resolver;
import com.jsan.mvc.resolve.annotation.RegisterName;

@RegisterName("text")
public class TextResolver implements Resolver {

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String contentType = (String) view.getAttribute(CONTENT_TYPE);

		if (contentType != null) {
			response.setContentType(contentType);
		} else {
			response.setContentType("text/plain");
		}

		Object value = view.getValue();

		if (value != null) {
			PrintWriter out = response.getWriter();
			out.print(value);
			out.flush();
			out.close();
		}
	}

}
