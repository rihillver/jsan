package com.jsan.mvc.resolve.suport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.MappingInfo;
import com.jsan.mvc.MvcConfig;
import com.jsan.mvc.View;
import com.jsan.mvc.resolve.Resolver;
import com.jsan.mvc.resolve.annotation.RegisterName;

@RegisterName("stream")
public class StreamResolver implements Resolver {

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

			InputStream in = null;
			try {
				in = (InputStream) data;
				OutputStream out = response.getOutputStream();
				byte[] buffer = new byte[1024 * 4];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.flush();
				out.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}

	}

}
