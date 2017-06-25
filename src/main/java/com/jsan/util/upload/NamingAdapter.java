package com.jsan.util.upload;

/**
 * 上传文件命名适配器接口。
 * <p>
 * 应确保实现类在单例下线程安全。
 *
 */

public interface NamingAdapter {

	/**
	 * 返回文件名（不含扩展名）。
	 * 
	 * @param fieldName
	 * @param fileName
	 * @param number
	 * @return
	 */
	public String getName(String fieldName, String fileName, int number);
}
