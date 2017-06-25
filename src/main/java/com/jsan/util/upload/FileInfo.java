package com.jsan.util.upload;

import java.util.Arrays;

public class FileInfo {

	private String primitiveName;

	private String name;
	private String nameWithoutExt;
	
	private String path;
	private String savePath;
	private String saveDirectory;

	private String fieldName;
	private String contentType;
	private String type;

	private long size;
	private byte[] bytes;

	private int width;
	private int height;

	private String describe;

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

	@Override
	public String toString() {
		return "FileInfo [primitiveName=" + primitiveName + ", name=" + name + ", nameWithoutExt=" + nameWithoutExt
				+ ", path=" + path + ", savePath=" + savePath + ", saveDirectory=" + saveDirectory + ", fieldName="
				+ fieldName + ", contentType=" + contentType + ", type=" + type + ", size=" + size + ", bytes="
				+ Arrays.toString(bytes) + ", width=" + width + ", height=" + height + ", describe=" + describe + "]";
	}

}
