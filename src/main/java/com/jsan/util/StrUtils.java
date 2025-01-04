package com.jsan.util;

import java.util.Objects;

/**
 * 简易的字符串组合连接包围的工具类。
 *
 */

public class StrUtils {

	public static String join(CharSequence delimiter, int... elements) {

		Objects.requireNonNull(delimiter);
		Objects.requireNonNull(elements);

		StringBuilder builder = new StringBuilder();

		int i = 1;

		for (Object cs : elements) {

			builder.append(cs);

			if (i++ < elements.length) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	public static String join(CharSequence delimiter, byte... elements) {

		Objects.requireNonNull(delimiter);
		Objects.requireNonNull(elements);

		StringBuilder builder = new StringBuilder();

		int i = 1;

		for (Object cs : elements) {

			builder.append(cs);

			if (i++ < elements.length) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	public static String join(CharSequence delimiter, short... elements) {

		Objects.requireNonNull(delimiter);
		Objects.requireNonNull(elements);

		StringBuilder builder = new StringBuilder();

		int i = 1;

		for (Object cs : elements) {

			builder.append(cs);

			if (i++ < elements.length) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	public static String join(CharSequence delimiter, long... elements) {

		Objects.requireNonNull(delimiter);
		Objects.requireNonNull(elements);

		StringBuilder builder = new StringBuilder();

		int i = 1;

		for (Object cs : elements) {

			builder.append(cs);

			if (i++ < elements.length) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	public static String join(CharSequence delimiter, float... elements) {

		Objects.requireNonNull(delimiter);
		Objects.requireNonNull(elements);

		StringBuilder builder = new StringBuilder();

		int i = 1;

		for (Object cs : elements) {

			builder.append(cs);

			if (i++ < elements.length) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	public static String join(CharSequence delimiter, double... elements) {

		Objects.requireNonNull(delimiter);
		Objects.requireNonNull(elements);

		StringBuilder builder = new StringBuilder();

		int i = 1;

		for (Object cs : elements) {

			builder.append(cs);

			if (i++ < elements.length) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	public static String join(CharSequence delimiter, char... elements) {

		Objects.requireNonNull(delimiter);
		Objects.requireNonNull(elements);

		StringBuilder builder = new StringBuilder();

		int i = 1;

		for (Object cs : elements) {

			builder.append(cs);

			if (i++ < elements.length) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	public static String join(CharSequence delimiter, boolean... elements) {

		Objects.requireNonNull(delimiter);
		Objects.requireNonNull(elements);

		StringBuilder builder = new StringBuilder();

		int i = 1;

		for (Object cs : elements) {

			builder.append(cs);

			if (i++ < elements.length) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	public static String join(CharSequence delimiter, CharSequence... elements) {

		Objects.requireNonNull(delimiter);
		Objects.requireNonNull(elements);

		StringBuilder builder = new StringBuilder();

		int i = 1;

		for (Object cs : elements) {

			builder.append(cs);

			if (i++ < elements.length) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	public static String join(CharSequence delimiter, Iterable<?> elements) {

		Objects.requireNonNull(delimiter);
		Objects.requireNonNull(elements);

		StringBuilder builder = new StringBuilder();

		for (Object cs : elements) {
			builder.append(cs);
			builder.append(delimiter);
		}
		String str = builder.toString();

		str = str.substring(0, str.length() - delimiter.length());

		return str;
	}

	public static String wrap(CharSequence content, CharSequence left, CharSequence right) {

		Objects.requireNonNull(content);
		Objects.requireNonNull(left);
		Objects.requireNonNull(right);

		StringBuilder builder = new StringBuilder();
		builder.append(left);
		builder.append(content);
		builder.append(right);

		return builder.toString();
	}

	public static String wrapApos(CharSequence content) {

		return wrap(content, "'", "'");
	}

	public static String wrapQuotes(CharSequence content) {

		return wrap(content, "\"", "\"");
	}

	public static String wrapMinBrackets(CharSequence content) {

		return wrap(content, "(", ")");
	}

	public static String wrapMidBrackets(CharSequence content) {

		return wrap(content, "[", "]");
	}

	public static String wrapMaxBrackets(CharSequence content) {

		return wrap(content, "{", "}");
	}

	public static String wrapMinBracketsFull(CharSequence content) {

		return wrap(content, "（", "）");
	}

	public static String wrapMidBracketsFull(CharSequence content) {

		return wrap(content, "【", "】");
	}

	public static String wrapMate(CharSequence content) {

		return wrap(content, "%", "%");
	}

}
