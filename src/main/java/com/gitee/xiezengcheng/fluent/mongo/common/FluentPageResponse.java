package com.gitee.xiezengcheng.fluent.mongo.common;


import java.util.Collections;
import java.util.List;

/**
 * 分页类
 * 
 */
public class FluentPageResponse<T> {

	private long totalCount;
	
	// 起始页
	private int pageNum;
	
	// 每页记录数
	private int pageSize;
	
	// 内容
	private List<T> contents;

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getContents() {
		return contents;
	}

	public void setContents(List<T> contents) {
		this.contents = contents;
	}
}
