package com.unilog.model;

import java.util.List;

import com.unilog.promotion.Sort;

public class PromotionIntRequest {

	  private int page;
	  private int pageSize;
	  private String status = "A";
	  private List<CriteriaIntValue> criteria;
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
		
		public List<CriteriaIntValue> getCriteria() {
			return criteria;
		}
		public void setCriteria(List<CriteriaIntValue> criteria) {
			this.criteria = criteria;
		}
		public List<Sort> getSort() {
			return sort;
		}
		public void setSort(List<Sort> sort) {
			this.sort = sort;
		}

}
