package com.jsan.mvc.resolve.suport;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.Resolver;
import com.jsan.mvc.resolve.annotation.RegisterName;

@RegisterName("byte")
public class ByteResolver implements Resolver {

	@Override
	public void execute(View view, MvcConfig mvcConfig, MappingInfo mappingInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String contentType = (String) view.getAttribute(CONTENT_TYPE);

		if (contentType != null) {
			response.setContentType(contentType);
		} else {
			response.setContentType("application/octet-stream");
		}

		Object data = view.getValue();

		if (data != null) {
			byte[] bytes = (byte[]) data;
			OutputStream out = response.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
		}
	}

}
