package com.jsan.convert;

public abstract class AbstractFormatter implements Formatter, Cloneable {

	protected String pattern;

	/**
	 * 对于实现类中的 Locale 或 TimeZone，由于这两个字段仅在 Formatter
	 * 实例化的时候就确定，因此克隆的时候无需再新建副本，指向原有对象并不会因此在应用过程中造成影响。
	 * 
	 * @return
	 */
	@Override
	public Formatter clone() {

		Formatter formatter;

		try {
			formatter = (Formatter) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}

		// formatter.setPattern(pattern); // String 类型，赋值即改变对象引用，因此不会像其他对象那样造成错乱

		return formatter;
	}

	@Override
	public void setPattern(String pattern) {

		this.pattern = pattern;
	}

	@Override
	public String getPattern() {

		return this.pattern;
	}

}
