package com.jsan.util.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

public class ByteStreamingUpload extends AbstractStreamingUpload {

	protected static final int DEFAULT_BUFFER_SIZE = 1024 * 8; // 默认缓冲区大小

	public ByteStreamingUpload(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void handleItemStream(InputStream in, File file, FileInfo info) throws Exception {

		ByteArrayOutputStream out = null;

		try {
			out = new ByteArrayOutputStream();
			long fileSize = 0;

			byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
			int len;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
				fileSize += len;
			}

			info.setSize(fileSize);
			info.setBytes(out.toByteArray());

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
