package com.jsan.mvc.resolve.suport;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.Resolver;
import com.jsan.mvc.resolve.annotation.RegisterName;

@RegisterName("html")
public class HtmlResolver implements Resolver {

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setContentType("text/html");

		Object value = view.getValue();

		if (value != null) {
			PrintWriter out = response.getWriter();
			out.print(value);
			out.flush();
			out.close();
		}
	}

}
