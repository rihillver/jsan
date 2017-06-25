package com.jsan.util.upload;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUpload extends AbstractUpload {

	private FileItemFactory fileItemFactory;

	protected boolean allowEmptyFile; // 是否允许上传空文件（大小为 0 的文件）

	public FileUpload(HttpServletRequest request) {

		super(request);
	}

	@Override
	protected int handleUpload(ServletFileUpload upload) throws Exception {

		if (fileItemFactory == null) {
			fileItemFactory = new DiskFileItemFactory();
		}

		upload.setFileItemFactory(fileItemFactory); // 非Streaming模式下必须使用FileItemFactory

		List<FileItem> list = upload.parseRequest(request);

		if (list == null) {
			return 0;
		}

		return handleFileItem(list);
	}

	protected int handleFileItem(List<FileItem> list) throws Exception {

		int fileCount = 0;

		for (FileItem item : list) {

			if (item.isFormField()) {

				String key = item.getFieldName();
				String value = characterEncoding == null ? item.getString() : item.getString(characterEncoding);
				handleFormField(key, value);

			} else {

				if (fileMax > -1 && fileCount >= fileMax) { // 判断最多上传文件数量
					continue;
				}

				if (item.getName().isEmpty()) { // 空文件表单
					continue;
				}

				if (item.getSize() == 0 && !allowEmptyFile) { // 判断空文件是否允许上传
					continue;
				}

				String primitiveName = UploadUtils.extractFileName(item.getName());

				String fileType = UploadUtils.extractFileType(primitiveName).toLowerCase();

				if (allowFileTypes != null && !allowFileTypes.contains(fileType)) { // 判断文件类型
					continue;
				}

				if (isFileExtToUppercase()) { // 文件扩展名是否转为大写
					fileType = fileType.toUpperCase();
				}

				String primitiveNameWithoutExt = UploadUtils.extractFileNameWithoutExt(primitiveName);

				String fieldName = item.getFieldName();

				String fileNameWithoutExt = handleFileName(fieldName, primitiveNameWithoutExt, fileCount);

				String fileName = fileType.isEmpty() ? fileNameWithoutExt : fileNameWithoutExt + "." + fileType;

				File file = new File(destPath, fileName);

				String filePath = file.getCanonicalPath();

				String fileContentType = item.getContentType();

				long fileSize = item.getSize();

				item.write(file);

				FileInfo info = new FileInfo();

				info.setPrimitiveName(primitiveName);
				info.setName(fileName);
				info.setNameWithoutExt(fileNameWithoutExt);
				info.setPath(filePath);
				info.setSavePath(savePath);
				info.setSaveDirectory(saveDirectory);
				info.setFieldName(fieldName);
				info.setContentType(fileContentType);
				info.setType(fileType);
				info.setSize(fileSize);

				fileInfoList.add(info);

				if (!item.isInMemory()) {
					item.delete(); // 删除临时文件
				}

				fileCount++;
			}
		}

		return fileCount;
	}

	/**
	 * 返回 FileItemFactory 。
	 * 
	 * @return
	 */
	public FileItemFactory getFileItemFactory() {

		return fileItemFactory;
	}

	/**
	 * 设置 FileItemFactory 。
	 * 
	 * @param fileItemFactory
	 */
	public void setFileItemFactory(FileItemFactory fileItemFactory) {

		this.fileItemFactory = fileItemFactory;
	}

	/**
	 * 返回是否允许上传空文件（大小为 0 的文件）。
	 * 
	 * @return
	 */
	public boolean isAllowEmptyFile() {
		return allowEmptyFile;
	}

	/**
	 * 设置是否允许上传空文件（大小为 0 的文件）。
	 * 
	 * @param allowEmptyFile
	 */
	public void setAllowEmptyFile(boolean allowEmptyFile) {
		this.allowEmptyFile = allowEmptyFile;
	}

}
