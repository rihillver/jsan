package com.jsan.dao;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<T> list;
	private int rowCount;
	private int pageSize;
	private int pageCount;
	private int pageNumber;
	private int pageRowCount;

	public List<T> values() {
		return getList();
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageRowCount() {
		return pageRowCount;
	}

	public void setPageRowCount(int pageRowCount) {
		this.pageRowCount = pageRowCount;
	}

	public boolean isFirstPage() {
		return pageNumber == 1;
	}

	public boolean isLastPage() {
		return pageNumber == pageCount;
	}

	@Override
	public String toString() {
		return "Page [list=" + list + ", rowCount=" + rowCount + ", pageSize=" + pageSize + ", pageCount=" + pageCount
				+ ", pageNumber=" + pageNumber + ", pageRowCount=" + pageRowCount + "]";
	}

}
