package com.jsan.util.upload;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

/**
 * Web 图片上传(默认允许格式 "jpg", "jpeg", "png", "gif", "bmp")。
 * <ul>
 * <li>通过设置 extractDimension 和 extractDimensionTypes
 * 可以提取全部文件类型或指定文件类型的图片尺寸，extractDimension 优先级别高于extractDimensionTypes。</li>
 * <li>当设置了对 gif 格式的文件提取图片尺寸时，如果 gif 图片为动态图片则会导致动画失效（仅保存到第一帧），因此对于 gif
 * 动态图片的上传处理最好不要提取图片尺寸，以避免 gif 动画失效，或者通过结合其他类库进行处理（比如 im4java）。</li>
 * </ul>
 *
 */

public class WebImageStreamingUpload extends FileStreamingUpload {

	{
		setAllowFileTypes("jpg", "jpeg", "png", "gif", "bmp");
	}

	protected boolean extractDimension; // 是否提取图片尺寸
	protected Set<String> extractDimensionTypes; // 指定需要提取图片尺寸的文件类型

	public WebImageStreamingUpload(HttpServletRequest request) {
		super(request);

	}

	@Override
	protected void handleItemStream(InputStream in, File file, FileInfo info) throws Exception {

		if (isExtractDimension()
				|| (extractDimensionTypes != null && extractDimensionTypes.contains(info.getType().toLowerCase()))) {

			try {

				BufferedImage bi = ImageIO.read(in);

				info.setWidth(bi.getWidth());
				info.setHeight(bi.getHeight());

				ImageIO.write(bi, info.getType(), file);// 将图片保存为文件

			} catch (Exception e) {
				WebImageIOException ex = new WebImageIOException(e);
				ex.setFieldName(info.getFieldName());
				ex.setFileName(info.getPrimitiveName());
				throw ex;
			}

			info.setSize(file.length());

		} else {

			super.handleItemStream(in, file, info);
		}

	}

	/**
	 * 返回是否提取图片尺寸。
	 * 
	 * @return
	 */
	public boolean isExtractDimension() {
		return extractDimension;
	}

	/**
	 * 设置是否提取图片尺寸。
	 * 
	 * @param extractDimension
	 */
	public void setExtractDimension(boolean extractDimension) {
		this.extractDimension = extractDimension;
	}

	/**
	 * 返回需要提取图片尺寸的文件类型。
	 * 
	 * @return
	 */
	public Set<String> getExtractDimensionTypes() {
		return extractDimensionTypes;
	}

	/**
	 * 设置需要提取图片尺寸的文件类型。
	 * 
	 * @param extractDimensionTypes
	 */
	public void setExtractDimensionTypes(Set<String> extractDimensionTypes) {
		this.extractDimensionTypes = extractDimensionTypes;
	}

	/**
	 * 设置需要提取图片尺寸的文件类型。
	 * 
	 * @param extractDimensionTypes
	 */
	public void setExtractDimensionTypes(String... extractDimensionTypes) {

		this.extractDimensionTypes = new HashSet<String>();
		for (String type : extractDimensionTypes) {
			this.extractDimensionTypes.add(type.toLowerCase());
		}
	}

	/**
	 * Web 图片处理的异常类。
	 *
	 */
	public static class WebImageIOException extends IOException {

		private static final long serialVersionUID = 1L;

		public WebImageIOException() {
			super();
		}

		public WebImageIOException(String message) {
			super(message);
		}

		public WebImageIOException(String message, Throwable cause) {
			super(message, cause);
		}

		public WebImageIOException(Throwable cause) {
			super(cause);
		}

		private String fileName;
		private String fieldName;

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

	}

}
