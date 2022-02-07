package com.jsan.util.upload;

import java.util.Arrays;

public class FileInfo {

	private String primitiveName; // 原始文件名
	private String primitiveNameWithoutExt; // 原始文件名（无后缀）

	private String name; // 新文件名
	private String nameWithoutExt; // 新文件名（无后缀）
	
	private String path; // 文件全路径
	private String savePath; // 保存路径
	private String saveDirectory; // 保存目录（相对于保存路径下的目录）

	private String fieldName; // 文件字段名
	private String contentType; // 文件ContentType
	private String type; // 文件类型（扩展名）

	private long size; // 文件大小（单位：Byte）
	private byte[] bytes; // 文件的byte数据，当使用ByteStreamingUpload时，不会存储到文件

	private int width; // 图片宽度
	private int height; // 图片高度

	private String describe; // 描述

	public String getPrimitiveName() {
		return primitiveName;
	}

	public void setPrimitiveName(String primitiveName) {
		this.primitiveName = primitiveName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameWithoutExt() {
		return nameWithoutExt;
	}

	public void setNameWithoutExt(String nameWithoutExt) {
		this.nameWithoutExt = nameWithoutExt;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getSaveDirectory() {
		return saveDirectory;
	}

	public void setSaveDirectory(String saveDirectory) {
		this.saveDirectory = saveDirectory;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getPrimitiveNameWithoutExt() {
		return primitiveNameWithoutExt;
	}

	public void setPrimitiveNameWithoutExt(String primitiveNameWithoutExt) {
		this.primitiveNameWithoutExt = primitiveNameWithoutExt;
	}

	@Override
	public String toString() {
		return "FileInfo [primitiveName=" + primitiveName + ", primitiveNameWithoutExt=" + primitiveNameWithoutExt + ", name=" + name + ", nameWithoutExt=" + nameWithoutExt + ", path=" + path + ", savePath=" + savePath + ", saveDirectory=" + saveDirectory + ", fieldName=" + fieldName + ", contentType=" + contentType + ", type=" + type + ", size=" + size + ", bytes=" + Arrays.toString(bytes) + ", width=" + width + ", height=" + height + ", describe=" + describe + "]";
	}

	

}
