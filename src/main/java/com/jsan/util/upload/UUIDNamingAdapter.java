package com.jsan.util.upload;

import java.util.UUID;

/**
 * 根据 UUID 字符串命名。
 *
 */

public class UUIDNamingAdapter implements NamingAdapter {

	private boolean unsigned;

	public UUIDNamingAdapter() {

	}

	public UUIDNamingAdapter(boolean unsigned) {

		this.unsigned = unsigned;
	}

	@Override
	public String getName(String fieldName, String fileName, int number) {

		String name = UUID.randomUUID().toString();

		if (unsigned) {
			return name.replaceAll("-", "");
		} else {
			return name;
		}
	}

}
