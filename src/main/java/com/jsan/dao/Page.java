package com.jsan.dao;

import java.util.Collection;
import java.util.List;

public interface Page<T> {

	Collection<T> values();

	List<T> getList();

	void setList(List<T> list);

	int getRowCount();

	void setRowCount(int rowCount);

	int getPageSize();

	void setPageSize(int pageSize);

	int getPageCount();

	void setPageCount(int pageCount);

	int getPageNumber();

	void setPageNumber(int pageNumber);

	int getPageRowCount();

	void setPageRowCount(int pageRowCount);

	boolean isFirstPage();

	boolean isLastPage();

	String toPageJson();

	String toPageDataJson();

	String toPageInfoJson();

}
