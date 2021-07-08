package com.unilog.promotion;

import java.util.List;

public class PromotionRequest {
	 private int page;
	  private int pageSize;
	  private String status = "A";
	  private List<Criteria> criteria;
	  private List<Sort> sort;
	  
		public int getPage() {
			return page;
		}
		public void setPage(int page) {
			this.page = page;
		}
		public int getPageSize() {
			return pageSize;
		}
		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public List<Criteria> getCriteria() {
			return criteria;
		}
		public void setCriteria(List<Criteria> criteria) {
			this.criteria = criteria;
		}
		public List<Sort> getSort() {
			return sort;
		}
		public void setSort(List<Sort> sort) {
			this.sort = sort;
		}
	  
	  
}
