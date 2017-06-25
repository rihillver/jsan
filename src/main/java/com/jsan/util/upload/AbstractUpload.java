package com.jsan.util.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 文件上传抽象类。
 * <ul>
 * <li>Streaming 模式不会保存文件到硬盘或内存，它仅仅提供一个流，可以读取它存放到硬盘，相比传统模式，避免了创建临时文件。</li>
 * <li>Streaming 模式下只能限制请求上传数据的大小，不能限制一次请求中单个上传文件的大小。</li>
 * <li>Streaming 模式下允许上传文件大小为 0 的空文件。</li>
 * </ul>
 *
 */

public abstract class AbstractUpload {

	protected long sizeMax = -1; // 整个请求上传数据的大小限制（含文本表单数据），默认无限制
	protected long fileSizeMax = -1; // 单个上传文件的大小限制，默认无限制
	protected String headerEncoding; // 请求头字符编码

	protected int fileMax = -1; // 最大上传文件数，默认无限制
	protected String characterEncoding; // 字符编码（主要用于解决文本表单乱码问题）
	protected Set<String> allowFileTypes; // 允许上传的文件类型，例如："jpg"、"rar"、""(无扩展名文件)等
	protected boolean fileExtToUppercase; // 文件扩展名是否大写
	protected String savePath; // 保存路径，默认为Web根目录
	protected String saveDirectory; // 保存目录，相对于保存路径下的目录
	protected String[] fileNames; // 指定文件命名
	protected NamingAdapter namingAdapter; // 文件命名适配器

	protected HttpServletRequest request;

	protected String destPath;
	protected List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
	protected Map<String, String[]> parameterMap = new LinkedHashMap<String, String[]>();

	public AbstractUpload(HttpServletRequest request) {

		this.request = request;
	}

	/**
	 * 执行文件上传，返回成功执行数。
	 * <ul>
	 * <li>如果设置了单个文件大小限制，那么任意一个文件超过此大小，则整个请求都将抛出异常上传失败。</li>
	 * <li>如果设置了整个请求上传数据限制，那么总上传数据（含文本表单数据）超过此大小，则整个请求都将抛出异常上传失败。</li>
	 * </ul>
	 * 
	 * 当该方法抛出异常时一般为以下原因：
	 * <ol>
	 * <li>非 multipart 表单。</li>
	 * <li>单个文件超出大小限制。</li>
	 * <li>整个请求超出大小限制。</li>
	 * </ol>
	 * 
	 * @return
	 * @throws Exception
	 */
	public int executeUpload() throws Exception {

		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new Exception("the request doesn't contains multipart content");
		}

		ServletFileUpload upload = new ServletFileUpload();

		if (fileSizeMax > -1) {
			upload.setFileSizeMax(fileSizeMax); // 未设置时默认值为 -1，即不限制大小
		}
		if (sizeMax > -1) {
			upload.setSizeMax(sizeMax); // 未设置时默认值为 -1，即不限制大小
		}
		if (headerEncoding != null) {
			upload.setHeaderEncoding(headerEncoding);
		}

		if (savePath == null) {
			savePath = UploadUtils.getWebRootPath(); // web根目录
		}

		File destFile = saveDirectory == null ? new File(savePath) : new File(savePath, saveDirectory);
		if (!destFile.exists()) { // 文件目录若不存在则创建
			destFile.mkdirs();
		}

		destPath = destFile.getCanonicalPath();

		return handleUpload(upload);
	}

	protected abstract int handleUpload(ServletFileUpload upload) throws Exception;

	/**
	 * 文本表单处理。
	 * 
	 * @param key
	 * @param value
	 */
	protected void handleFormField(String key, String value) {

		if (parameterMap.containsKey(key)) {
			String[] values = parameterMap.get(key);
			int len = values.length;
			String[] newValues = new String[len + 1];
			System.arraycopy(values, 0, newValues, 0, len);
			newValues[len] = value;
			parameterMap.put(key, newValues);
		} else {
			String[] values = new String[1];
			values[0] = value;
			parameterMap.put(key, values);
		}
	}

	/**
	 * 文件重命名。
	 * 
	 * @param fieldName
	 * @param primitiveNameWithoutExt
	 * @param fileCount
	 * @return
	 */
	protected String handleFileName(String fieldName, String primitiveNameWithoutExt, int fileCount) {

		if (fileNames != null || namingAdapter != null) {
			if (fileNames != null) {
				return fileNames[fileCount];
			} else {
				return namingAdapter.getName(fieldName, primitiveNameWithoutExt, fileCount);
			}
		}

		return primitiveNameWithoutExt;
	}

	// ============================================================

	/**
	 * 返回整个请求上传数据的大小限制（含文本表单数据）。
	 * 
	 * @return
	 */
	public long getSizeMax() {
		return sizeMax;
	}

	/**
	 * 设置整个请求上传数据的大小限制（含文本表单数据）
	 * 
	 * @param sizeMax
	 */
	public void setSizeMax(long sizeMax) {
		this.sizeMax = sizeMax;
	}

	/**
	 * 返回单个上传文件的大小限制。
	 * 
	 * @return
	 */
	public long getFileSizeMax() {
		return fileSizeMax;
	}

	/**
	 * 设置单个上传文件的大小限制。
	 * 
	 * @param fileSizeMax
	 */
	public void setFileSizeMax(long fileSizeMax) {
		this.fileSizeMax = fileSizeMax;
	}

	/**
	 * 返回请求头字符编码。
	 * 
	 * @return
	 */
	public String getHeaderEncoding() {
		return headerEncoding;
	}

	/**
	 * 设置请求头字符编码。
	 * 
	 * @param headerEncoding
	 */
	public void setHeaderEncoding(String headerEncoding) {
		this.headerEncoding = headerEncoding;
	}

	/**
	 * 返回字符编码（主要用于解决文本表单乱码问题）。
	 * 
	 * @return
	 */
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	/**
	 * 设置字符编码（主要用于解决文本表单乱码问题）。
	 * 
	 * @param characterEncoding
	 */
	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	/**
	 * 返回最大上传文件数。
	 * 
	 * @return
	 */
	public int getFileMax() {
		return fileMax;
	}

	/**
	 * 设置最大上传文件数。
	 * 
	 * @param fileMax
	 */
	public void setFileMax(int fileMax) {
		this.fileMax = fileMax;
	}

	/**
	 * 返回允许上传的文件类型。
	 * 
	 * @return
	 */
	public Set<String> getAllowFileTypes() {
		return allowFileTypes;
	}

	/**
	 * 设置允许上传的文件类型，例如："jpg"、"rar"、""(无扩展名文件)等。
	 * <p>
	 * 大小写不限，内部将转换成小写。
	 * 
	 * @param allowFileTypes
	 */
	public void setAllowFileTypes(Set<String> allowFileTypes) {

		this.allowFileTypes = new HashSet<String>();
		for (String fileType : allowFileTypes) {
			this.allowFileTypes.add(fileType.toLowerCase());
		}
	}

	/**
	 * 允许上传的文件类型（数组形式），例如："jpg"、"rar"、""(无扩展名文件)等。
	 * <p>
	 * 大小写不限，内部将转换成小写。
	 * 
	 * @param allowFileTypes
	 */
	public void setAllowFileTypes(String... allowFileTypes) {

		this.allowFileTypes = new HashSet<String>();
		for (String type : allowFileTypes) {
			this.allowFileTypes.add(type.toLowerCase());
		}
	}

	/**
	 * 返回文件扩展名是否大写。
	 * 
	 * @return
	 */
	public boolean isFileExtToUppercase() {
		return fileExtToUppercase;
	}

	/**
	 * 设置文件扩展名是否大写。
	 * 
	 * @param fileExtToUppercase
	 */
	public void setFileExtToUppercase(boolean fileExtToUppercase) {
		this.fileExtToUppercase = fileExtToUppercase;
	}

	/**
	 * 返回保存路径（默认为 Web 根目录）。
	 * 
	 * @return
	 */
	public String getSavePath() {
		return savePath;
	}

	/**
	 * 设置保存路径（默认为 Web 根目录）。
	 * 
	 * @param savePath
	 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	/**
	 * 返回保存目录（相对于保存路径，与保存路径区分主要是便于在数据库中存储）。
	 * 
	 * @return
	 */
	public String getSaveDirectory() {
		return saveDirectory;
	}

	/**
	 * 设置保存目录（相对于保存路径，与保存路径区分主要是便于在数据库中存储）。
	 * 
	 * @param saveDirectory
	 */
	public void setSaveDirectory(String saveDirectory) {
		this.saveDirectory = saveDirectory;
	}

	/**
	 * 返回指定文件命名。
	 * 
	 * @return
	 */
	public String[] getFileNames() {
		return fileNames;
	}

	/**
	 * 设置指定文件命名。
	 * <p>
	 * 优先于 NamingAdapter 接口，一般建议根据指定文件命名时，同时指定 fileMax 大小，并保持文件命名数量与 fileMax
	 * 大小一致，避免数组越界。
	 * 
	 * @param fileNames
	 */
	public void setFileNames(String... fileNames) {
		this.fileNames = fileNames;
	}

	/**
	 * 返回命名适配器。
	 * 
	 * @return
	 */
	public NamingAdapter getNamingAdapter() {
		return namingAdapter;
	}

	/**
	 * 设置命名适配器。
	 * 
	 * @param namingAdapter
	 */
	public void setNamingAdapter(NamingAdapter namingAdapter) {
		this.namingAdapter = namingAdapter;
	}

	/**
	 * 返回 request。
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * 返回请求参数值。
	 * 
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {

		String[] values = parameterMap.get(name);

		if (values != null) {
			return values[0];
		} else {
			return null;
		}
	}

	/**
	 * 返回请求参数值（数组）。
	 * 
	 * @param name
	 * @return
	 */
	public String[] getParameterValues(String name) {

		return parameterMap.get(name);
	}

	/**
	 * 返回请求参数的 Map 值。
	 * 
	 * @return
	 */
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	/**
	 * 返回 FileInfo 的 List。
	 * 
	 * @return
	 */
	public List<FileInfo> getFileInfoList() {
		return fileInfoList;
	}

}
