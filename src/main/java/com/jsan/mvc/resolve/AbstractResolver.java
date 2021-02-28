package com.jsan.mvc.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.jsan.mvc.json.JsonSerializeConfigurator;

public abstract class AbstractResolver implements Resolver {
	
	protected void setStatusCode(HttpServletResponse response, int statusCode) {

		if (statusCode > 0) {
			response.setStatus(statusCode);
		}
	}

	protected void setContentType(HttpServletResponse response, String contentType, String defaultContentType) {

		if (contentType == null) {
			response.setContentType(defaultContentType);
		} else {
			response.setContentType(contentType);
		}
	}

	protected String getJsonString(JsonSerializeConfigurator configurator, Object value) {

		return com.alibaba.fastjson.JSON.toJSONString(value, configurator.getSerializeConfig(),
				configurator.getSerializeFilters(), configurator.getDateFormat(), configurator.getDefaultFeatures(),
				configurator.getSerializerFeatures());
	}

	protected void print(HttpServletResponse response, Object value) throws Exception {

		if (value != null) {
			PrintWriter out = response.getWriter();
			out.print(value);
			out.close();
		}
	}

	protected void writeBytes(HttpServletResponse response, Object value) throws Exception {

		if (value != null) {
			byte[] bytes = (byte[]) value;
			OutputStream out = response.getOutputStream();
			out.write(bytes);
			out.close();
		}
	}

	protected void writeStream(HttpServletResponse response, Object value) throws Exception {

		if (value != null) {
			InputStream in = null;
			try {
				in = (InputStream) value;
				OutputStream out = response.getOutputStream();
				byte[] buffer = new byte[1024 * 4];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
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
