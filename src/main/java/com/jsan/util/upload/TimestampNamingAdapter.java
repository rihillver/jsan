package com.jsan.util.upload;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据时间戳命名。
 * <p>
 * 默认格式为 "yyyyMMddHHmmssS" 。
 *
 */

public class TimestampNamingAdapter implements NamingAdapter {

	private SimpleDateFormat sdf = null;

	public TimestampNamingAdapter() {

		sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
	}

	public TimestampNamingAdapter(String pattern) {

		sdf = new SimpleDateFormat(pattern);
	}

	@Override
	public String getName(String fieldName, String fileName, int number) {

		return sdf.format(new Date());
	}

}
