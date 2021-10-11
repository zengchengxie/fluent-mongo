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
	private Integer curr = 1;
	
	// 每页记录数,默认为10
	private Integer limit = 10;

	// 是否查询总数量
	private Boolean queryTotalCount = true;
	
	// 内容列表(非输入项)
	private List<T> list = Collections.emptyList();
	
	public Boolean getQueryTotalCount() {
		return queryTotalCount;
	}

	public void setQueryTotalCount(Boolean queryTotalCount) {
		this.queryTotalCount = queryTotalCount;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getCurr() {
		return curr;
	}

	public void setCurr(Integer curr) {
		this.curr = curr;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
