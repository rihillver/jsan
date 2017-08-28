package com.jsan.dao;

public class RowMetaData {

	private boolean autoIncrement;

	private boolean caseSensitive;

	private boolean searchable;

	private boolean currency;

	private int nullable;

	private boolean signed;

	private int columnDisplaySize;

	private String columnLabel;

	private String columnName;

	private String schemaName;

	private int precision;

	private int scale;

	private String tableName;

	private String catalogName;

	private int columnType;

	private String columnTypeName;

	private boolean readOnly;

	private boolean writable;

	private boolean definitelyWritable;

	// --------------------------JDBC 2.0-----------------------------------

	private String columnClassName;

	// =====================================================================

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public boolean isCurrency() {
		return currency;
	}

	public void setCurrency(boolean currency) {
		this.currency = currency;
	}

	public int getNullable() {
		return nullable;
	}

	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}

	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}

	public String getColumnLabel() {
		return columnLabel;
	}

	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public int getColumnType() {
		return columnType;
	}

	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	public String getColumnTypeName() {
		return columnTypeName;
	}

	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isWritable() {
		return writable;
	}

	public void setWritable(boolean writable) {
		this.writable = writable;
	}

	public boolean isDefinitelyWritable() {
		return definitelyWritable;
	}

	public void setDefinitelyWritable(boolean definitelyWritable) {
		this.definitelyWritable = definitelyWritable;
	}

	public String getColumnClassName() {
		return columnClassName;
	}

	public void setColumnClassName(String columnClassName) {
		this.columnClassName = columnClassName;
	}

	@Override
	public String toString() {
		return "RowMetaData [autoIncrement=" + autoIncrement + ", caseSensitive=" + caseSensitive + ", searchable="
				+ searchable + ", currency=" + currency + ", nullable=" + nullable + ", signed=" + signed
				+ ", columnDisplaySize=" + columnDisplaySize + ", columnLabel=" + columnLabel + ", columnName="
				+ columnName + ", schemaName=" + schemaName + ", precision=" + precision + ", scale=" + scale
				+ ", tableName=" + tableName + ", catalogName=" + catalogName + ", columnType=" + columnType
				+ ", columnTypeName=" + columnTypeName + ", readOnly=" + readOnly + ", writable=" + writable
				+ ", definitelyWritable=" + definitelyWritable + ", columnClassName=" + columnClassName + "]";
	}

}
