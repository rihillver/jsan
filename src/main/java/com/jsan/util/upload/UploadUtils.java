package com.jsan.util.upload;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

/**
 * 文件上传工具类（基于 Apache Commons FileUpload）。
 *
 */

public class UploadUtils {

	/**
	 * 返回 DiskFileItemFactory 实例。
	 * 
	 * @return
	 */
	public static DiskFileItemFactory getFileItemFactory() {

		return getFileItemFactory(-1, (File) null);
	}

	/**
	 * 返回 DiskFileItemFactory 实例。
	 * 
	 * @param sizeThreshold
	 *            内存缓冲区大小（Byte）
	 * @param repositoryPath
	 *            临时文件夹路径
	 * @return
	 */
	public static DiskFileItemFactory getFileItemFactory(int sizeThreshold, String repositoryPath) {

		return getFileItemFactory(sizeThreshold, repositoryPath, null);
	}

	/**
	 * 返回 DiskFileItemFactory 实例。
	 * 
	 * @param sizeThreshold
	 *            内存缓冲区大小（Byte）
	 * @param repository
	 *            临时文件夹
	 * @return
	 */
	public static DiskFileItemFactory getFileItemFactory(int sizeThreshold, File repository) {

		return getFileItemFactory(sizeThreshold, repository, null);
	}

	/**
	 * 返回 DiskFileItemFactory 实例。
	 * 
	 * @param sizeThreshold
	 *            内存缓冲区大小（Byte）
	 * @param repositoryPath
	 *            临时文件夹路径
	 * @param context
	 * @return
	 */
	public static DiskFileItemFactory getFileItemFactory(int sizeThreshold, String repositoryPath,
			ServletContext context) {

		return getFileItemFactory(sizeThreshold, new File(repositoryPath), context);
	}

	/**
	 * 返回 DiskFileItemFactory 实例。
	 * <p>
	 * 如果设置了 ServletContext ，则将自动清除临时文件，建议在文件处理完的时候调用 FileItem 的 delete()
	 * 方法能更及时删除临时文件，并可忽略自动清除临时文件的设置。
	 * 
	 * @param sizeThreshold
	 *            内存缓冲区大小（Byte）
	 * @param repository
	 *            临时文件夹
	 * @param context
	 *            Servlet上下文
	 * @return
	 */
	public static DiskFileItemFactory getFileItemFactory(int sizeThreshold, File repository, ServletContext context) {

		DiskFileItemFactory factory = new DiskFileItemFactory();

		if (sizeThreshold > -1) {

			// DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD = 10240 （未设置时的默认值）
			factory.setSizeThreshold(sizeThreshold);
		}

		if (repository != null) {
			if (!repository.exists()) {
				repository.mkdirs();
			}

			// new File(System.getProperty("java.io.tmpdir")) （未设置时默认值）
			factory.setRepository(repository);
		}

		if (context != null) {
			FileCleaningTracker tracker = FileCleanerCleanup.getFileCleaningTracker(context);
			factory.setFileCleaningTracker(tracker);
		}

		return factory;
	}

	/**
	 * 返回 ServletFileUpload 实例。
	 * 
	 * @return
	 */
	public static ServletFileUpload getServletFileUpload() {

		return getServletFileUpload(-1, -1);
	}

	/**
	 * 返回 ServletFileUpload 实例。
	 * 
	 * @param fileSizeMax
	 *            单个上传文件的大小限制
	 * @param sizeMax
	 *            整个请求上传数据的大小限制（含文本表单数据）
	 * @return
	 */
	public static ServletFileUpload getServletFileUpload(long fileSizeMax, long sizeMax) {

		return getServletFileUpload(fileSizeMax, sizeMax, null);
	}

	/**
	 * 返回 ServletFileUpload 实例。
	 * 
	 * @param fileSizeMax
	 *            单个上传文件的大小限制
	 * @param sizeMax
	 *            整个请求上传数据的大小限制（含文本表单数据）
	 * @param headerEncoding
	 *            请求头字符编码
	 * @return
	 */
	public static ServletFileUpload getServletFileUpload(long fileSizeMax, long sizeMax, String headerEncoding) {

		return getServletFileUpload(fileSizeMax, sizeMax, headerEncoding, null);
	}

	/**
	 * 返回 ServletFileUpload 实例。
	 * 
	 * @param fileSizeMax
	 *            单个上传文件的大小限制
	 * @param sizeMax
	 *            整个请求上传数据的大小限制（含文本表单数据）
	 * @param headerEncoding
	 *            请求头字符编码
	 * @param fileItemFactory
	 *            FileItemFactory 实例
	 * @return
	 */
	public static ServletFileUpload getServletFileUpload(long fileSizeMax, long sizeMax, String headerEncoding,
			FileItemFactory fileItemFactory) {

		ServletFileUpload upload = new ServletFileUpload();
		if (fileItemFactory != null) {
			upload.setFileItemFactory(fileItemFactory);
		}
		upload.setHeaderEncoding(headerEncoding);
		upload.setFileSizeMax(fileSizeMax);
		upload.setSizeMax(sizeMax);

		return upload;
	}

	// =================================================================

	/**
	 * 返回 Web 应用根路径。
	 * 
	 * @return
	 */
	public static String getWebRootPath() {

		URL url = UploadUtils.class.getResource("/");
		File file = new File(url.getPath());
		try {
			return file.getParentFile().getParentFile().getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从文件路径中提取文件吗。
	 * 
	 * @param path
	 * @return
	 */
	public static String extractFileName(String path) {

		int index = path.lastIndexOf('/');
		if (index > -1) {
			path = path.substring(index + 1);
		}

		index = path.lastIndexOf('\\');
		if (index > -1) {
			path = path.substring(index + 1);
		}

		return path;
	}

	/**
	 * 从文件名中提取不含扩展名的文件名。
	 * 
	 * @param fileName
	 * @return
	 */
	public static String extractFileNameWithoutExt(String fileName) {

		int index = fileName.lastIndexOf('.');
		if (index > -1) {
			return fileName.substring(0, index);
		} else {
			return fileName;
		}
	}

	/**
	 * 提取文件类型（文件扩展名）。
	 * 
	 * @param fileName
	 * @return
	 */
	public static String extractFileType(String fileName) {

		int index = fileName.lastIndexOf('.');
		if (index > -1) {
			return fileName.substring(index + 1);
		} else {
			return "";
		}
	}

}
