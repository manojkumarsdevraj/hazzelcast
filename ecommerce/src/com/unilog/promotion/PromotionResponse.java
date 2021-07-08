package com.unilog.promotion;

public class PromotionResponse {
	private PromotionData data;
	  private Status status;

	  public PromotionData getData()
	  {
	    return this.data;
	  }

	  public void setData(PromotionData data) {
	    this.data = data;
	  }

	  public void setStatus(Status status) {
	    this.status = status;
	  }

	  public Status getStatus() {
	    return this.status;
	  }
}
