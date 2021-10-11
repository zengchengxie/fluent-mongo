package fluent.mongo.common;


import java.util.Collections;
import java.util.List;

/**
 * 分页类
 * 
 */
public class FluentPage<T> {
	
	// 总记录数(非输入项)
	private Long totalCount = 0L;
	
	// 起始页,从1开始
	private Integer pageNum = 1;
	
	// 每页记录数,默认为10
	private Integer pageSize = 10;

	// 是否查询总数量
	private Boolean queryTotalCount = true;
	
	// 内容列表(非输入项)
	private List<T> contents = Collections.emptyList();
	
	public Boolean getQueryTotalCount() {
		return queryTotalCount;
	}

	public void setQueryTotalCount(Boolean queryTotalCount) {
		this.queryTotalCount = queryTotalCount;
	}

	public List<T> getContents() {
		return contents;
	}

	public void setContents(List<T> contents) {
		this.contents = contents;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
