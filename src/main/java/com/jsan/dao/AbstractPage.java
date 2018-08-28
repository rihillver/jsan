package com.jsan.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSON;

public abstract class AbstractPage<T> implements Page<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private List<T> list;
	private int rowCount;
	private int pageSize;
	private int pageCount;
	private int pageNumber;
	private int pageRowCount;
	private int startIndex;

	// private int startIndex;
	// private int pageIndex;
	// private int totalItems;
	// private int totalPages;
	// private int itemsPerPage;
	// private int currentItemCount;

	@Override
	public Collection<T> values() {
		return list;
	}

	@Override
	public List<T> getList() {
		return list;
	}

	@Override
	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public int getPageCount() {
		return pageCount;
	}

	@Override
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	@Override
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public int getPageRowCount() {
		return pageRowCount;
	}

	@Override
	public int getStartIndex() {
		return startIndex;
	}

	@Override
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	@Override
	public void setPageRowCount(int pageRowCount) {
		this.pageRowCount = pageRowCount;
	}

	@Override
	public boolean isFirstPage() {
		return pageNumber <= 1;
	}

	@Override
	public boolean isLastPage() {
		return pageNumber >= pageCount;
	}

	@Override
	public String toJson() {
		return JSON.toJSONString(this);
	}

	@Override
	public String toDataJson() {
		return JSON.toJSONString(list);
	}

	@Override
	public String toInfoJson() {
		return "{\"rowCount\":" + rowCount + ",\"pageSize\":" + pageSize + ",\"pageCount\":" + pageCount
				+ ",\"pageNumber\":" + pageNumber + ",\"pageRowCount\":" + pageRowCount + ",\"startIndex\":"
				+ startIndex + ",\"firstPage\":" + isFirstPage() + ",\"lastPage\":" + isLastPage() + "}";
	}

	@Override
	public String toString() {
		return "Page [list=" + list + ", rowCount=" + rowCount + ", pageSize=" + pageSize + ", pageCount=" + pageCount
				+ ", pageNumber=" + pageNumber + ", pageRowCount=" + pageRowCount + ", startIndex=" + startIndex
				+ ", firstPage=" + isFirstPage() + ", lastPage=" + isLastPage() + "]";
	}

}
