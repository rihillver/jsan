package com.jsan.util.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

public abstract class AbstractStreamingUpload extends AbstractUpload {

	public AbstractStreamingUpload(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected int handleUpload(ServletFileUpload upload) throws Exception {

		FileItemIterator iterator = upload.getItemIterator(request);

		return handleFileItem(iterator);
	}

	protected int handleFileItem(FileItemIterator iterator) throws Exception {

		int fileCount = 0;

		while (iterator.hasNext()) {

			FileItemStream item = iterator.next();
			InputStream in = item.openStream();

			try {

				if (item.isFormField()) {

					String key = item.getFieldName();
					String value = characterEncoding == null ? Streams.asString(in)
							: Streams.asString(in, characterEncoding);
					handleFormField(key, value);

				} else {

					if (fileMax > -1 && fileCount >= fileMax) { // 判断最大上传文件数量
						continue;
					}

					if (item.getName().isEmpty()) { // 空文件表单
						continue;
					}

					// 判断空文件是否允许上传，Streaming模式下无法进行
					// if (item.getSize() == 0 && !allowEmptyFile) {
					// continue;
					// }

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

					handleItemStream(in, file, info);

					// info.setSize(fileSize); Streaming模式下需要读取流的时候进行累加计算来取得文件大小

					fileInfoList.add(info);

					fileCount++;
				}

			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return fileCount;
	}

	protected abstract void handleItemStream(InputStream in, File file, FileInfo info) throws Exception;

}
